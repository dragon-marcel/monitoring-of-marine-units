package backend.Marine.units.service;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import backend.Marine.units.entity.Point;
import backend.Marine.units.entity.Ship;
import backend.Marine.units.entity.Type;
import backend.Marine.units.entity.User;
import backend.Marine.units.exception.UserNotFoundException;
import backend.Marine.units.mail.ContainerObserverShip;
import backend.Marine.units.mail.ShipMailObserver;
import backend.Marine.units.mapper.ShipMapper;
import backend.Marine.units.model.TrackShip;
import backend.Marine.units.repository.ShipRepository;
import backend.Marine.units.repository.TypeRepository;
import backend.Marine.units.repository.UserRepository;
import backend.Marine.units.utils.Utils;

@Service
public class ShipServiceImpl implements ShipService {
	private final ShipRepository shipRepository;
	private final UserRepository userRepository;
	private final ContainerObserverShip observer;
	private final ImageShipDataFetcherService imageShipApiService;
	private final TypeRepository typeRepository;
	private final SimpMessagingTemplate template;

	public ShipServiceImpl(ShipRepository shipRepository, UserRepository userRepository, ContainerObserverShip observer,
			ImageShipDataFetcherService imimageShipApiService, SimpMessagingTemplate template,
			TypeRepository typeRepository) {
		this.shipRepository = shipRepository;
		this.userRepository = userRepository;
		this.observer = observer;
		this.imageShipApiService = imimageShipApiService;
		this.template = template;
		this.typeRepository = typeRepository;
	}

	public void trackShipParsetoShips(TrackShip[] tracks) {
		List<Ship> ships = shipRepository.findAll();
		List<Ship> newShips = handleTrackShips(tracks);
		updateShipOutAreaStatus(ships, newShips);
		sendShipsInAreaToWebSocket(ships);
		sendShipsTrackedByUserName();
	}

	private List<Ship> handleTrackShips(TrackShip[] tracks) {
		ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

		List<Ship> newShips = Arrays.stream(tracks).map(track -> executorService.submit(() -> handleTrackShip(track)))
				.map(future -> {
					try {
						return future.get();
					} catch (Exception e) {
						e.printStackTrace();
						return null;
					}
				}).filter(Objects::nonNull).collect(Collectors.toList());
		executorService.shutdown();
		return newShips;
	}

	private Ship handleTrackShip(TrackShip track) {
		Ship existingShip = shipRepository.findByMmsi(track.getMmsi()).orElse(null);
		Ship currentShip = ShipMapper.INSTANCE.toShip(track);

		if (existingShip == null)
			return handleNewShip(currentShip, track.getShipType());
		else
			return handleExistingShip(existingShip, currentShip);
	}

	private Ship handleNewShip(Ship ship, int idTypeShip) {
		String url = imageShipApiService.getGoogleSearchEngineForImagePathOfQuery(String.valueOf(ship.getMmsi()));
		Type type = typeRepository.findById(idTypeShip).orElse(typeRepository.findById(1).orElse(null));

		ship.setType(type);
		ship.setImg(url);
		shipRepository.save(ship);
		sendInfoStatusToWebSocket(ship.getMmsi(), "in");
		notifyObservers(ship);
		return ship;
	}

	private Ship handleExistingShip(Ship existingShip, Ship currentShip) {
		existingShip.setActive(isActive(currentShip.getCurrentPoint(), getLastPoint(existingShip.getTrack())));
		existingShip.setCurrentPoint(currentShip.getCurrentPoint());
		existingShip.addPoint(currentShip.getCurrentPoint());
		existingShip.setInArea(true);

		if (existingShip.getTrack().size() > 2) {
			existingShip.setDistance(Utils.getDistance(existingShip));
			double timeMS = Utils.getTimeDifferenceInMilliseconds(System.currentTimeMillis(),
					(existingShip.getTrack().get(existingShip.getTrack().size() - 2).getCreateDateTime().getTime()));
			System.out.println("TIME1:"+ System.currentTimeMillis() + "   "+ (existingShip.getTrack().get(existingShip.getTrack().size() - 2).getCreateDateTime().getTime()));

			System.out.println("TIME:"+ timeMS);
			existingShip.setSpeed(Utils.calculateSpeedInKmH(existingShip.getDistance(), timeMS));
		}
		return shipRepository.save(existingShip);
	}

	private void notifyObservers(Ship ship) {
		Set<ShipMailObserver> observers = observer.getObserversShip();
		observers.forEach(observer -> observer.notifyAboutShipEvent(ship));
	}

	private void sendShipsTrackedByUserName() {
		List<User> users = userRepository.findAll();
		users.forEach(user -> {
			List<Ship> ships = shipRepository.findTrackedShipsByUsername(user.getUsername());
			template.convertAndSend("/topic/ships/tracked/" + user.getUsername(), ships);
		});

	}

	private void sendShipsInAreaToWebSocket(List<Ship> ships) {
		List<Ship> shipsInArea = ships.stream().filter(ship -> ship.isInArea()).collect(Collectors.toList());
		template.convertAndSend("/topic/ships", shipsInArea);
	}

	private void updateShipOutAreaStatus(List<Ship> ships, List<Ship> newShips) {
		ships.forEach(ship -> {

			boolean stillInArea = newShips.stream().anyMatch(newShip -> ship.getMmsi() == newShip.getMmsi());

			if (!stillInArea && ship.isInArea()) {
				ship.setInArea(false);
				shipRepository.save(ship);
				sendInfoStatusToWebSocket(ship.getMmsi(), "out");
			}
		});
	}

	private void sendInfoStatusToWebSocket(int mmsi, String statusMessage) {
		template.convertAndSend("/topic/info", "Ship mmsi: " + mmsi + " " + statusMessage + " area.");
	}

	private boolean isActive(Point currentPoint, Point lastPoint) {
		return lastPoint != null && !currentPoint.equals(lastPoint);
	}

	public Point getLastPoint(List<Point> points) {
		return points == null || points.isEmpty() ? null : points.get(points.size() - 1);
	}

	public void addTrackedShip(String username, int mmsi) {
		User user = userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException(username));
		Ship ship = shipRepository.findByMmsi(mmsi).orElse(null);

		if (ship != null) {
			ship.addUser(user);
			shipRepository.save(ship);
		}
	}

	public void deleteTrackedShip(String username, int mmsi) {
		User user = userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException(username));
		Ship ship = shipRepository.findByMmsi(mmsi).orElse(null);

		ship.getTrackedBy().remove(user);
		shipRepository.save(ship);

	}
}

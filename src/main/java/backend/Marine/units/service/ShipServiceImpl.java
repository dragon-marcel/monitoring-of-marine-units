package backend.Marine.units.service;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import backend.Marine.units.entity.Point;
import backend.Marine.units.entity.Ship;
import backend.Marine.units.entity.Type;
import backend.Marine.units.entity.User;
import backend.Marine.units.exception.UserNotFoundException;
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
	private final ShipMessagingService shipMessagingService;
	private final ImageShipDataFetcherService imageShipApiService;
	private final TypeRepository typeRepository;

	@Autowired
	public ShipServiceImpl(ShipRepository shipRepository, UserRepository userRepository, ShipMessagingService shipMessagingService,
			ImageShipDataFetcherService imageShipApiService,TypeRepository typeRepository) {
		this.shipRepository = shipRepository;
		this.userRepository = userRepository;
		this.shipMessagingService = shipMessagingService;
		this.imageShipApiService = imageShipApiService;
		this.typeRepository = typeRepository;
	}

	public void trackShipParseToShips(TrackShip[] tracks) {
		List<Ship> ships = shipRepository.findAll();
		List<Ship> newShips = handleTrackShips(tracks);
		updateShipOutAreaStatus(ships, newShips);
		shipMessagingService.sendShipsInAreaToWebSocket(ships);

		List<User> users = userRepository.findAll();
		users.forEach(user -> {
			List<Ship> shipsTrackedByUserName = shipRepository.findTrackedShipsByUsername(user.getUsername());
			shipMessagingService.sendShipsTrackedByUserName(user.getUsername(),shipsTrackedByUserName);
		});
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
		shipMessagingService.sendInfoStatusToWebSocket(ship.getMmsi(), "in");
		shipMessagingService.notifyObservers(ship);
		return ship;
	}

	private Ship handleExistingShip(Ship existingShip, Ship currentShip) {
		existingShip.setActive(isActiveShip(currentShip.getCurrentPoint(), getLastShipPosition(existingShip.getTrack())));
		existingShip.setCurrentPoint(currentShip.getCurrentPoint());
		existingShip.addPoint(currentShip.getCurrentPoint());
		existingShip.setInArea(true);
		existingShip.setDistance(Utils.getDistance(existingShip));
		existingShip.setSpeed(calculateSpeedShipInKmH(existingShip));

		return shipRepository.save(existingShip);
	}
	private void updateShipOutAreaStatus(List<Ship> ships, List<Ship> newShips) {
		ships.forEach(ship -> {

			boolean stillInArea = newShips.stream().anyMatch(newShip -> ship.getMmsi() == newShip.getMmsi());

			if (!stillInArea && ship.isInArea()) {
				ship.setInArea(false);
				shipRepository.save(ship);
				shipMessagingService.sendInfoStatusToWebSocket(ship.getMmsi(), "out");
			}
		});
	}
	private int calculateSpeedShipInKmH(Ship ship){
		List<Point> track = ship.getTrack();
		if (track.size() <= 1)
			return 0;

		double time = Utils.getTimeDifferenceInMilliseconds(System.currentTimeMillis(), getTimeSecondLastShipPosition(ship));
		double distance = Optional.of(ship.getDistance()).orElse(0.0);
		return Utils.calculateSpeedInKmH(distance, time);
	}
	private Long getTimeSecondLastShipPosition(Ship ship) {
		List<Point> track = ship.getTrack();
		if (track.size() < 2) {
			throw new IllegalArgumentException("Ship track should have at least two points.");
		}
		Point point = ship.getTrack().get(ship.getTrack().size() - 2);
		return point.getCreateDateTime().getTime();
	}
	private boolean isActiveShip(Point currentPoint, Point lastPoint) {
		return lastPoint != null && !currentPoint.equals(lastPoint);
	}

	public Point getLastShipPosition(List<Point> points) {
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

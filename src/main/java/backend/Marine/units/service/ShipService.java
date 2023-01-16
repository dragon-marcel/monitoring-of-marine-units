package backend.Marine.units.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import backend.Marine.units.entity.Point;
import backend.Marine.units.entity.Ship;
import backend.Marine.units.entity.Type;
import backend.Marine.units.entity.User;
import backend.Marine.units.exception.UserNotFoundException;
import backend.Marine.units.mail.ContainerObserverShip;
import backend.Marine.units.mail.ObserverShip;
import backend.Marine.units.model.TrackShip;
import backend.Marine.units.repository.ShipRepository;
import backend.Marine.units.repository.TypeRepository;
import backend.Marine.units.repository.UserRepository;

@Service
public class ShipService {
	private final ShipRepository shipRepository;
	private final UserRepository userRepository;
	private final ContainerObserverShip observer;
	private final TypeRepository typeRepository;
	private final ImageShipApiService im;

	private final SimpMessagingTemplate template;

	public ShipService(ShipRepository shipRepository, UserRepository userRepository, ContainerObserverShip observer,
			TypeRepository typeRepository, ImageShipApiService im, SimpMessagingTemplate template) {
		this.shipRepository = shipRepository;
		this.userRepository = userRepository;
		this.observer = observer;
		this.typeRepository = typeRepository;
		this.im = im;
		this.template = template;
	}

	public void trackShipParsetoShips(TrackShip[] tracks) {
		Set<ObserverShip> observers = observer.getObserversShip();
		System.out.println("OBSERWATORZY:" + observers.size());
		List<Ship> ships = shipRepository.findAll();
		List<Ship> newShip = new ArrayList<Ship>();

		for (int i = 0; i < tracks.length; i++) {
			TrackShip track = tracks[i];

			if (i == 0)
				template.convertAndSend("/topic/message", "Ship in " + track.getMmsi());

			Ship ship = shipRepository.findByMmsi(track.getMmsi()).orElse(null);
			Point currentPoint = new Point(track.getGeometry().getCoordinates().get(1),
					track.getGeometry().getCoordinates().get(0));
			Type type = typeRepository.findById(track.getShipType()).orElse(null);

			Ship currentShip = new Ship(track.getMmsi(), track.getName(), track.getCountry(), track.getDestination(),
					type);

			if (ship == null) {
				String url = im.getGoogleSearchEngineForImagePathOfQuery(String.valueOf(track.getMmsi()));
				currentShip.setImg(url);
				currentShip.addPoint(currentPoint);
				currentShip.setCurrentPoint(currentPoint);
				shipRepository.save(currentShip);

				if (ships != null && !ships.isEmpty()) {
					template.convertAndSend("/ship_ws/info", "Ship in " + track.getMmsi());

					for (ObserverShip observer : observers)
						observer.sendMail(currentShip);
				}

			} else {

				ship.setActive(isActive(currentPoint, getlastPoint(ship.getTrack())));
				ship.setCurrentPoint(currentPoint);
				ship.addPoint(currentPoint);
				ship.setInArea(true);
				shipRepository.save(ship);
			}
			newShip.add(currentShip);
		}
		template.convertAndSend("/topic/ships", newShip);

		isInAreaShip(ships, newShip);
	}

	private void isInAreaShip(List<Ship> ships, List<Ship> newShips) {
		ships.forEach(ship -> {
			boolean inArea = false;
			for (int i = 0; i < newShips.size(); i++) {
				if (ship.getMmsi() == newShips.get(i).getMmsi()) {
					inArea = true;
					return;
				}
			}
			if (!inArea && ship.isInArea()) {
				ship.setInArea(false);
				shipRepository.save(ship);
				template.convertAndSend("/ship_ws/info", "Ship out " + ship.getMmsi());

			}
		});
	}

	private boolean isActive(Point currentPoint, Point lastPoint) {
		if (lastPoint != null && !currentPoint.equals(lastPoint))
			return true;
		else
			return false;
	}

	public Point getlastPoint(List<Point> points) {
		return points == null || points.isEmpty() ? null : points.get(points.size() - 1);
	}

	public List<Ship> findTrackedShipsByUsername(String username) {
		return null;
	}

	public void addTrackedShip(String username, int mmsi) {
		User user = userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException(username));
		Ship ship = shipRepository.findByMmsi(mmsi).orElse(null);

		if (ship != null) {
			ship.addUser(user);
			shipRepository.save(ship);
			ship.getTrackedBy().forEach(a -> System.out.println(a));

		}
	}

	public void deleteTrackedShip(String username, int mmsi) {
		User user = userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException(username));
		Ship ship = shipRepository.findByMmsi(mmsi).orElse(null);

		ship.getTrackedBy().remove(user);
		shipRepository.save(ship);

	}
//	public void addTracketdShip(String username, int mmsi) {
//		User user = userRepository.findByUsernameIgnoreCase(username)
//				.orElseThrow(() -> new UserNotFoundException(username));
//		shipRepository.findByMmsi(mmsi)
//				.orElseThrow(() -> new EntityExistsException("Ship by mmsi: " + mmsi + " not exist"));
//		user.addTrackedShip(mmsi);
//		userRepository.save(user);
//	}
//
//	public void stopTracketdShip(String username, int mmsi) {
//		User user = userRepository.findByUsernameIgnoreCase(username)
//				.orElseThrow(() -> new UserNotFoundException(username));
//		shipRepository.findByMmsi(mmsi)
//				.orElseThrow(() -> new EntityExistsException("Ship by mmsi: " + mmsi + " not exist"));
//		Set<Integer> trackedShips = user.getTrackedShip();
//		if (trackedShips != null) {
//			trackedShips.remove(mmsi);
//			userRepository.save(user);
//		}
//
//	}
}

package backend.Marine.units.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import backend.Marine.units.entity.Point;
import backend.Marine.units.entity.Ship;
import backend.Marine.units.entity.Type;
import backend.Marine.units.entity.User;
import backend.Marine.units.exception.UserNotFoundException;
import backend.Marine.units.mail.ContainerObserverShip;
import backend.Marine.units.mail.ObserverShip;
import backend.Marine.units.mapper.ShipMapper;
import backend.Marine.units.model.TrackShip;
import backend.Marine.units.repository.ShipRepository;
import backend.Marine.units.repository.TypeRepository;
import backend.Marine.units.repository.UserRepository;

@Service
public class ShipService {
	private final ShipRepository shipRepository;
	private final UserRepository userRepository;
	private final ContainerObserverShip observer;
	private final ImageShipApiService im;
	private final TypeRepository typeRepository;
	private final SimpMessagingTemplate template;

	public ShipService(ShipRepository shipRepository, UserRepository userRepository, ContainerObserverShip observer,
			ImageShipApiService im, SimpMessagingTemplate template, TypeRepository typeRepository) {
		this.shipRepository = shipRepository;
		this.userRepository = userRepository;
		this.observer = observer;
		this.im = im;
		this.template = template;
		this.typeRepository = typeRepository;
	}

	public void trackShipParsetoShips(TrackShip[] tracks) {
		Set<ObserverShip> observers = observer.getObserversShip();
		List<Ship> ships = shipRepository.findAll();
		List<Ship> newShip = new ArrayList<Ship>();

		for (int i = 0; i < tracks.length; i++) {
			TrackShip track = tracks[i];
			Ship ship = shipRepository.findByMmsi(track.getMmsi()).orElse(null);

			Ship currentShip = ShipMapper.INSTANCE.toShip(track);

			if (ship == null) {
				String url = im.getGoogleSearchEngineForImagePathOfQuery(String.valueOf(track.getMmsi()));
				Type type = typeRepository.findById(track.getShipType()).orElse(null);
				currentShip.setType(type);
				currentShip.setImg(url);
				shipRepository.save(currentShip);

				if (ships != null && !ships.isEmpty()) {
					template.convertAndSend("/topic/info", "Ship in " + track.getMmsi());

					for (ObserverShip observer : observers)
						observer.sendMail(currentShip);
				}

			} else {

				ship.setActive(isActive(currentShip.getCurrentPoint(), getlastPoint(ship.getTrack())));
				ship.setCurrentPoint(currentShip.getCurrentPoint());
				ship.addPoint(currentShip.getCurrentPoint());
				ship.setInArea(true);
				if (ship.getTrack().size() > 2) {
					ship.setDistance(getDistance(ship));
					ship.setSpeed(getSpeed(ship));
				}
				shipRepository.save(ship);

			}
			newShip.add(currentShip);

		}

		isInAreaShip(ships, newShip);
		sendShipsWS(ships);
		sednShipByUsersName();
	}

	private void sednShipByUsersName() {
		List<User> users = userRepository.findAll();
		users.forEach(user -> {
			List<Ship> ships = shipRepository.findTrackedShipsByUsername(user.getUsername());
			template.convertAndSend("/topic/ships/tracked/" + user.getUsername(), ships);
		});

	}

	private void sendShipsWS(List<Ship> ships) {
		List<Ship> shipsInArea = ships.stream().filter(ship -> ship.isInArea() == true).collect(Collectors.toList());
		template.convertAndSend("/topic/ships", shipsInArea);
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
				template.convertAndSend("/topic/info", "Ship out " + ship.getMmsi());

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

	private double getDistance(Ship ship) {

		double lat1 = ship.getCurrentPoint().getXCoordinate();
		double lon1 = ship.getCurrentPoint().getYCoordinate();
		double lat2 = ship.getTrack().get(ship.getTrack().size() - 2).getXCoordinate();
		double lon2 = ship.getTrack().get(ship.getTrack().size() - 2).getYCoordinate();

		double R = 6371; // Radius of the earth in km
		double dLat = deg2rad(lat2 - lat1); // deg2rad below
		double dLon = deg2rad(lon2 - lon1);
		double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
				+ Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.sin(dLon / 2) * Math.sin(dLon / 2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		double d = R * c; // Distance in km
		return d;

	}

	public double deg2rad(double deg) {
		return deg * (Math.PI / 180);
	}

	public int getSpeed(Ship ship) {
		Date date1 = ship.getCurrentPoint().getCreateDateTime();
		Date date2 = ship.getTrack().get(ship.getTrack().size() - 2).getCreateDateTime();
		double milisec = date1.getTime() - date2.getTime();
		double sec = milisec / 1000;
		double min = sec / 60;
		double hour = min / 60;

		int speedKmH = (int) (ship.getDistance() / hour);

		return speedKmH;

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

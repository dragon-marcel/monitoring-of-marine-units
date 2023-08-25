package backend.Marine.units.mapper;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import backend.Marine.units.entity.Point;
import backend.Marine.units.entity.Ship;
import backend.Marine.units.model.TrackShip;

public class ShipMapperImpl implements ShipMapper {

	@Override
	public Ship toShip(TrackShip track) {
		if (track == null) {
			return null;
		}
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		Date timestamp = null;
		try {
			timestamp = dateFormat.parse(track.getTimeStamp());
		} catch (ParseException e) {
			throw new IllegalArgumentException("Invalid timestamp format: " + track.getTimeStamp(), e);
		}
		Point point = new Point(track.getGeometry().getCoordinates().get(1),
				track.getGeometry().getCoordinates().get(0), timestamp);

		Ship ship = new Ship();
		ship.setMmsi(track.getMmsi());
		ship.setName(track.getName());
		ship.setCountry(track.getCountry());
		ship.setDestination(track.getDestination());
		ship.setCurrentPoint(point);
		return ship;
	}

}

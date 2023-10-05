package backend.Marine.units.service;

import backend.Marine.units.entity.Point;
import backend.Marine.units.model.TrackShip;

import java.util.List;

public interface ShipService {
	void trackShipParseToShips(TrackShip[] tracks);

	void addTrackedShip(String username, int mmsi);

	void deleteTrackedShip(String username, int mmsi);

	Point getLastShipPosition(List<Point> points);

}

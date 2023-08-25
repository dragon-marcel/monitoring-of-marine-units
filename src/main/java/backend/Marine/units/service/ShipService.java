package backend.Marine.units.service;

import backend.Marine.units.model.TrackShip;

public interface ShipService {
	void trackShipParsetoShips(TrackShip[] tracks);

	void addTrackedShip(String username, int mmsi);

	void deleteTrackedShip(String username, int mmsi);

}

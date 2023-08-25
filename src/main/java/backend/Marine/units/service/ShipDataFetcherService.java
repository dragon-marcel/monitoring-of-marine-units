package backend.Marine.units.service;

import backend.Marine.units.model.Area;

public interface ShipDataFetcherService {
	void getAccessToken();

	void fetchShipsFromArea();

	void fetchShip(Area area);

	Area getArea();

	void setArena();
}

package backend.Marine.units.service;

import java.util.List;

import backend.Marine.units.model.ChartShip;

public interface ChartShipService {
	List<ChartShip> getDataShipTypeChart();

	Integer getCountShipOutArea();

	Integer getCountShipInArea();

	Integer getCountShipTracked(String username);

	Integer getCountShipMovable();

	Integer getCountShipUnMovable();

	List<ChartShip> getCountShipByHour();
}

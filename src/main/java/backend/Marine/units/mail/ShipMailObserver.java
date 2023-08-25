package backend.Marine.units.mail;

import backend.Marine.units.entity.Ship;

public interface ShipMailObserver {
	void notifyAboutShipEvent(Ship ship);
}

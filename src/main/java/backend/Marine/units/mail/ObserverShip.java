package backend.Marine.units.mail;

import backend.Marine.units.entity.Ship;

public interface ObserverShip {
	void sendMail(Ship ship);
}

package backend.Marine.units.service;

import backend.Marine.units.entity.Ship;

import java.util.List;

public interface ShipMessagingService {

    void notifyObservers(Ship ship);

    void setNotificationEnabled(boolean enabled);

    void sendShipsInAreaToWebSocket(List<Ship> ships);

    void sendInfoStatusToWebSocket(int mmsi, String statusMessage);

    void sendShipsTrackedByUserName(String userName, List<Ship> ships);
}

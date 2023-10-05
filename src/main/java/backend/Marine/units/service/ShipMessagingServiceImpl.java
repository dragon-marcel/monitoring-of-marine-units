package backend.Marine.units.service;

import backend.Marine.units.entity.Ship;
import backend.Marine.units.mail.ContainerObserverShip;
import backend.Marine.units.mail.ShipMailObserver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ShipMessagingServiceImpl implements ShipMessagingService {
    private final ContainerObserverShip observer;
    private final SimpMessagingTemplate template;

    @Value("${notificationEnabled}")
    private boolean notificationEnabled;

    @Autowired
    public ShipMessagingServiceImpl(ContainerObserverShip observer, SimpMessagingTemplate template) {
        this.observer = observer;
        this.template = template;
    }

    public void notifyObservers(Ship ship) {
        if(isNotificationEnabled()) {
            Set<ShipMailObserver> observers = observer.getObserversShip();
            observers.forEach(observer -> observer.notifyAboutShipEvent(ship));
        }
    }
    private boolean isNotificationEnabled(){
        return notificationEnabled;
    }
    public void setNotificationEnabled(boolean enabled){
        this.notificationEnabled = enabled;
    }
    public void sendShipsInAreaToWebSocket(List<Ship> ships) {
        List<Ship> shipsInArea = ships.stream().filter(Ship::isInArea).collect(Collectors.toList());
        template.convertAndSend("/topic/ships", shipsInArea);
    }

    public void sendInfoStatusToWebSocket(int mmsi, String statusMessage) {
        template.convertAndSend("/topic/info", "Ship mmsi: " + mmsi + " " + statusMessage + " area.");
    }

    public void sendShipsTrackedByUserName(String userName, List<Ship> ships) {
        template.convertAndSend("/topic/ships/tracked/" + userName, ships);
    }
}

package backend.Marine.units.service;

import backend.Marine.units.entity.Ship;
import backend.Marine.units.entity.Type;
import backend.Marine.units.mail.ContainerObserverShip;
import backend.Marine.units.mail.ShipMailObserver;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.mockito.Mockito.*;

public class ShipMessagingServiceTest {

    private ContainerObserverShip containerObserverShip;

    private ShipMailObserver shipMailObserver1;
    private ShipMailObserver shipMailObserver2;
    private SimpMessagingTemplate template;
    private ShipMessagingService shipMessagingService;
    private Ship ship;

    @Before
    public void setUp() throws Exception {
        containerObserverShip = mock(ContainerObserverShip.class);
        template = mock(SimpMessagingTemplate.class);
        shipMailObserver1 = mock(ShipMailObserver.class);
        shipMailObserver2 = mock(ShipMailObserver.class);
        shipMessagingService = new ShipMessagingServiceImpl(containerObserverShip, template);
        ship = new Ship(122, "Natos", "Poland", "Gdańsk", new Type("Military"));
    }

    @Test
    public void should_notify_observers_when_notification_enabled() {
        //give

        //when
        when(containerObserverShip.getObserversShip()).thenReturn(getObservers());
        shipMessagingService.setNotificationEnabled(true);
        shipMessagingService.notifyObservers(ship);

        //then
        Mockito.verify(shipMailObserver1, Mockito.times(1)).notifyAboutShipEvent(ship);
        Mockito.verify(shipMailObserver2, Mockito.times(1)).notifyAboutShipEvent(ship);
        Mockito.verify(shipMailObserver1).notifyAboutShipEvent(ship);
    }

    @Test
    public void should_notify_observers_when_notification_disabled() {
        //give

        //when
        when(containerObserverShip.getObserversShip()).thenReturn(getObservers());
        shipMessagingService.setNotificationEnabled(false);
        shipMessagingService.notifyObservers(ship);

        //then
        Mockito.verify(shipMailObserver1, never()).notifyAboutShipEvent(ship);
        Mockito.verify(shipMailObserver2, never()).notifyAboutShipEvent(ship);
    }

    @Test
    public void should_send_info_status_to_web_socket() {
        //given
        int mmsi = 123;
        String statusMessage = "Status message";

        //when
        shipMessagingService.sendInfoStatusToWebSocket(mmsi, statusMessage);

        //then
        verify(template, times(1)).convertAndSend("/topic/info", "Ship mmsi: " + mmsi + " " + statusMessage + " area.");
    }

    @Test
    public void should_send_ships_tracked_by_userName() {
        //given
        String userName = "testUser";
        List<Ship> ships = new ArrayList<>();

        //when
        shipMessagingService.sendShipsTrackedByUserName(userName, ships);

        //then
        verify(template, times(1)).convertAndSend("/topic/ships/tracked/" + userName, ships);
    }

    private HashSet<ShipMailObserver> getObservers() {
        HashSet<ShipMailObserver> observerShips = new HashSet<>();
        observerShips.add(shipMailObserver1);
        observerShips.add(shipMailObserver2);
        return observerShips;
    }
}
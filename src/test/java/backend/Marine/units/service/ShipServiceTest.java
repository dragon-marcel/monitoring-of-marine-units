package backend.Marine.units.service;

import backend.Marine.units.entity.Point;
import backend.Marine.units.entity.Ship;
import backend.Marine.units.entity.User;
import backend.Marine.units.exception.UserNotFoundException;
import backend.Marine.units.repository.ShipRepository;
import backend.Marine.units.repository.TypeRepository;
import backend.Marine.units.repository.UserRepository;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ShipServiceTest {

    @Mock
    private ShipRepository shipRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ShipMessagingService shipMessagingService;
    @Mock
    private ImageShipDataFetcherService imageShipApiService;
    @Mock
    private TypeRepository typeRepository;
    private ShipService shipService;
    private List<Point> points;

    @Before
    public void setUp() throws Exception {
        shipService = new ShipServiceImpl(shipRepository, userRepository, shipMessagingService,
                imageShipApiService, typeRepository);
        points = new ArrayList<>();
        points.add(new Point(1.0, 2.0));
        points.add(new Point(3.0, 4.0));
        points.add(new Point(5.0, 6.0));
    }

    @Test
    public void should_return_last_point() {
        // given

        // when
        Point result = shipService.getLastShipPosition(points);

        // then
        assertEquals(new Point(5.0, 6.0), result);
        assertNotEquals(new Point(3.0, 4.0), result);

    }

    @Test
    public void should_return_null_for_empty_list() {
        // given
        List<Point> points = new ArrayList<>();

        // when
        Point result = shipService.getLastShipPosition(points);

        // then
        assertNull(result);
    }

    @Test
    public void should_delete_tracked_ship() {
        // Given
        String username = "testUser";
        int mmsi = 123456;

        User user = new User();
        user.setUsername(username);

        Ship ship = new Ship();
        ship.setMmsi(mmsi);
        Set<User> trackedByUsers = new HashSet<>();
        trackedByUsers.add(user);
        ship.setTrackedBy(trackedByUsers);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(shipRepository.findByMmsi(mmsi)).thenReturn(Optional.of(ship));

        // When
        shipService.deleteTrackedShip(username, mmsi);

        // Then
        assertTrue("User should be removed from trackedBy", ship.getTrackedBy().isEmpty());

    }

    @Test(expected = UserNotFoundException.class)
    public void should_throw_exception_when_user_not_found() {
        // Given
        String username = "nonExistentUser";
        int mmsi = 123456;

        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        // When
        shipService.deleteTrackedShip(username, mmsi);

        // Then
        // UserNotFoundException should be thrown
    }

    @Test
    public void should_add_tracked_ship_when_user_and_ship_exist() {
        // Given
        String username = "testUser";
        int mmsi = 12345;
        User user = new User();
        user.setUsername(username);
        Ship ship = new Ship();
        ship.setMmsi(mmsi);
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(shipRepository.findByMmsi(mmsi)).thenReturn(Optional.of(ship));

        // When
        shipService.addTrackedShip(username, mmsi);

        // Then
        Mockito.verify(shipRepository, times(1)).save(ship);
        assertTrue(ship.getTrackedBy().contains(user));
        assertThat(ship.getTrackedBy(), Matchers.hasSize(1));
    }

}
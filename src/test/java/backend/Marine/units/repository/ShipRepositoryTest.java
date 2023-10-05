package backend.Marine.units.repository;

import backend.Marine.units.entity.Ship;
import backend.Marine.units.entity.Type;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

@DataJpaTest
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
public class ShipRepositoryTest {

    @Autowired
    private ShipRepository shipRepository;

    @Test
    public void should_return_ship_in_area() {
        //when
        List<Ship> shipInArea = shipRepository.findAllInArea();

        //then
        assertNotNull(shipInArea);
        assertFalse(shipInArea.isEmpty());
        assertThat(shipInArea, Matchers.hasSize(2));
        assertTrue(shipInArea.stream().allMatch(s->s.isInArea()));
    }

    @Test
    public void should_return_ship_out_area() {
        //when
        List<Ship> shipInArea = shipRepository.findAllOutArea();

        //then
        assertNotNull(shipInArea);
        assertFalse(shipInArea.isEmpty());
        assertThat(shipInArea, Matchers.hasSize(1));
        assertFalse(shipInArea.get(0).isInArea());
    }

    @Test
    public void should_return_movable_ships() {
        //when
        List<Ship> movableShips = shipRepository.findAllMovable();

        //then
        assertNotNull(movableShips);
        assertFalse(movableShips.isEmpty());
        assertThat(movableShips, Matchers.hasSize(1));
        assertTrue(movableShips.get(0).isInArea());
        assertTrue(movableShips.get(0).isActive());
    }
    @Test
    public void should_return_unmovable_ships() {
        //when
        List<Ship> movableShips = shipRepository.findAllUnMovable();

        //then
        assertNotNull(movableShips);
        assertFalse(movableShips.isEmpty());
        assertThat(movableShips, Matchers.hasSize(1));
        assertTrue(movableShips.get(0).isInArea());
        assertFalse(movableShips.get(0).isActive());
    }
    @Test
    public void should_return_ship_by_mmsi() {
        //given
        int mmsi = 257599700;
        //when
        Optional<Ship> ship  = shipRepository.findByMmsi(mmsi);

        //then
        assertNotNull(ship);
        assertEquals(257599700,ship.get().getMmsi());
    }

    @Test
    public void should_return_ships_tracked_by_userName() {
        //given
        String userName = "admin";

        //when
        List<Ship> trackedShipsByUsername = shipRepository.findTrackedShipsByUsername(userName);

        //then
        assertThat(trackedShipsByUsername, Matchers.hasSize(1));
        assertTrue(trackedShipsByUsername.get(0).getTrackedBy().stream().anyMatch(u->u.getUsername() .equals(userName)));
    }
}

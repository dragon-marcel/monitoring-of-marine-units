package utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;

import backend.Marine.units.entity.Point;
import backend.Marine.units.entity.Ship;
import backend.Marine.units.utils.Utils;

public class UtlisTest {

	@Test
	public void should_get_speed() {
		// given
		double time = 60000;// ms
		double distance = 1;// km
		// when
		// then
		assertEquals(60, Utils.calculateSpeedInKmH(distance, 60000));
		assertEquals(30, Utils.calculateSpeedInKmH(distance, 120000));
		assertNotEquals(10, Utils.calculateSpeedInKmH(distance, 120000));
	}

	@Test
	public void should_no_get_speed_by_time_zero() {
		// given
		double time = 0;// ms
		double distance = 1;// km
		// when
		// then
		assertThrows(IllegalArgumentException.class, () -> {
			Utils.calculateSpeedInKmH(distance, time);
		});
	}

	@Test
	public void should_get_deg2rad() {
		// given
		double deg = 45.0;
		// when
		double rad = Utils.deg2rad(deg);
		// then
		assertEquals(0.7853981633974483, rad);
	}

	@Test
	public void should_get_distance() {
		// given
		Ship ship = mock(Ship.class);
		Point currentPoint = new Point(63.437988, 10.396793, new Date());
		Point previousPoint = new Point(63.437992, 10.396785, new Date());
		List<Point> track = Arrays.asList(previousPoint, currentPoint);

		// when
		when(ship.getCurrentPoint()).thenReturn(currentPoint);
		when(ship.getTrack()).thenReturn(track);
		double distance = Utils.getDistance(ship);

		// then
		assertEquals(5.967064416905403E-4, distance, 0.001);
	}
}
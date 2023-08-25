package backend.Marine.units.utils;

import backend.Marine.units.entity.Ship;

public class Utils {

	private final static double RADIUS = 6371;

	public static double getDistance(Ship ship) {

		double lat1 = ship.getCurrentPoint().getXCoordinate();
		double lon1 = ship.getCurrentPoint().getYCoordinate();
		double lat2 = ship.getTrack().get(ship.getTrack().size() - 2).getXCoordinate();
		double lon2 = ship.getTrack().get(ship.getTrack().size() - 2).getYCoordinate();

		double dLat = deg2rad(lat2 - lat1);
		double dLon = deg2rad(lon2 - lon1);
		double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
				+ Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.sin(dLon / 2) * Math.sin(dLon / 2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

		return RADIUS * c;

	}

	public static double getTimeDifferenceInMilliseconds(long timeEnd, long timeStart) {
		return timeEnd - timeStart;
	}

	public static int calculateSpeedInKmH(double distance, double time) {
		if (time <= 0) {
			throw new IllegalArgumentException("Time must be greater than zero.");
		}

		double hour = time / (1000 * 60 * 60);
		return (int) (distance / hour);
	}

	public static double deg2rad(double deg) {
		return deg * (Math.PI / 180);
	}
}

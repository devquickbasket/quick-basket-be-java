package com.quickbasket.quickbasket.customs.Utils;

public class DistanceCalculator {

    private static final double EARTH_RADIUS_KM = 6371.0088; // Earth's radius in kilometers (WGS84)

    /**
     * Calculates the great-circle distance between two coordinates using the Haversine formula.
     *
     * @param originLat  latitude of the origin point
     * @param originLon  longitude of the origin point
     * @param targetLat  latitude of the target point
     * @param targetLon  longitude of the target point
     * @return distance in kilometers
     */
    public static double calculateDistance(double originLat, double originLon,
                                           double targetLat, double targetLon) {
        double dLat = Math.toRadians(targetLat - originLat);
        double dLon = Math.toRadians(targetLon - originLon);

        double lat1Rad = Math.toRadians(originLat);
        double lat2Rad = Math.toRadians(targetLat);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(lat1Rad) * Math.cos(lat2Rad)
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS_KM * c;
    }
}

package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.Preconditions;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.ClosedInterval;
import ch.epfl.rigel.math.RightOpenInterval;

import java.util.Locale;

/**
 * @author Bastien Faivre (310929)
 * @author Kamil Mellouk (312327)
 */

public final class GeographicCoordinates extends SphericalCoordinates {

    /**
     * constructor of the geographic coordinates
     *
     * @param longitude the longitude
     * @param latitude  the latitude
     */
    private GeographicCoordinates(double longitude, double latitude) {
        super(longitude, latitude);
    }

    /**
     * Creating geographic coordinates from given longitude and latitude in deg
     *
     * @param lonDeg the longitude in deg
     * @param latDeg the latitude in deg
     * @return the geographic coordinates of given longitude and latitude in deg
     *
     * @throws IllegalArgumentException if lonDeg isn't in [-180, 190[ or latDeg isn't in [90, 90]
     */
    public static GeographicCoordinates ofDeg(double lonDeg, double latDeg) {
        // check exceptions
        Preconditions.checkArgument(isValidLonDeg(lonDeg));
        Preconditions.checkArgument(isValidLatDeg(latDeg));

        return new GeographicCoordinates(Angle.ofDeg(lonDeg), Angle.ofDeg(latDeg));
    }

    /**
     * Checks if lonDeg is contained in [-180, 180[
     *
     * @param lonDeg longitude in deg to check
     * @return {@code true} if and only if the longitude is contained in [-180, 180[
     */
    public static boolean isValidLonDeg(double lonDeg) {
        return RightOpenInterval.symmetric(360).contains(lonDeg);
    }

    /**
     * Checks if latDeg is contained in [-90, 90]
     *
     * @param latDeg latitude in deg to check
     * @return {@code true} if and only if the longitude is contained in [-90, 90]
     */
    public static boolean isValidLatDeg(double latDeg) {
        return ClosedInterval.symmetric(180).contains(latDeg);
    }

    @Override
    public double lon() {
        return super.lon();
    }

    @Override
    public double lonDeg() {
        return super.lonDeg();
    }

    @Override
    public double lat() {
        return super.lat();
    }

    @Override
    public double latDeg() {
        return super.latDeg();
    }

    @Override
    public String toString() {
        return String.format(Locale.ROOT, "(lon=%.4f°, lat=%.4f°)", lonDeg(), latDeg());
    }
}

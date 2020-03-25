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
public final class EclipticCoordinates extends SphericalCoordinates {

    /**
     * constructor of the ecliptic coordinates
     *
     * @param longitude the longitude
     * @param latitude  the latitude
     */
    private EclipticCoordinates(double longitude, double latitude) {
        super(longitude, latitude);
    }

    /**
     * Creating ecliptic coordinates from given ecliptic longitude and ecliptic latitude in rad
     *
     * @param lon the ecliptic longitude in rad
     * @param lat the ecliptic latitude in rad
     * @return the ecliptic coordinates of given ecliptic longitude and ecliptic latitude in rad
     * @throws IllegalArgumentException if lon isn't in [0, 2PI[ or lat isn't in [-PI/2, PI/2]
     */
    public static EclipticCoordinates of(double lon, double lat) {
        return new EclipticCoordinates(
                Preconditions.checkInInterval(RightOpenInterval.of(0, Angle.TAU), lon),
                Preconditions.checkInInterval(ClosedInterval.symmetric(Math.PI), lat)
        );
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
        return String.format(Locale.ROOT, "(\u03BB=%.4f°, \u03B2=%.4f°)", lonDeg(), latDeg());
    }
}

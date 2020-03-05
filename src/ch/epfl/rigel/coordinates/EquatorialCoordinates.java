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
public final class EquatorialCoordinates extends SphericalCoordinates {

    /**
     * constructor of the equatorial coordinates
     *
     * @param longitude the longitude
     * @param latitude  the latitude
     */
    private EquatorialCoordinates(double longitude, double latitude) {
        super(longitude, latitude);
    }

    /**
     * Creating equatorial coordinates from given right ascension and declination in rad
     *
     * @param ra  the right ascension in rad
     * @param dec the declination in rad
     * @return the equatorial coordinates of given right ascension and declination in rad
     */
    public static EquatorialCoordinates of(double ra, double dec) {
        return new EquatorialCoordinates(
                Preconditions.checkInInterval(RightOpenInterval.of(0, Angle.TAU), ra),
                Preconditions.checkInInterval(ClosedInterval.symmetric(Math.PI), dec)
        );
    }

    /**
     * @return the right ascension in rad
     */
    public double ra() {
        return super.lon();
    }

    /**
     * @return the right ascension in deg
     */
    public double raDeg() {
        return super.lonDeg();
    }

    /**
     * @return the right ascension in hr
     */
    public double raHr() {
        return Angle.toHr(super.lon());
    }

    /**
     * @return the declination in rad
     */
    public double dec() {
        return super.lat();
    }

    /**
     * @return the declination in deg
     */
    public double decDeg() {
        return super.latDeg();
    }

    @Override
    public String toString() {
        return String.format(Locale.ROOT, "(ra=%.4fh, dec=%.4f°)", raHr(), decDeg());
    }
}

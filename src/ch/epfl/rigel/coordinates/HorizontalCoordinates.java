package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.Preconditions;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.ClosedInterval;
import ch.epfl.rigel.math.RightOpenInterval;

import java.util.Locale;

/**
 * Representation of a position on a sphere using horizontal coordinates
 *
 * @author Bastien Faivre (310929)
 * @author Kamil Mellouk (312327)
 * @see EquatorialToHorizontalConversion
 */
public final class HorizontalCoordinates extends SphericalCoordinates {

    private static final RightOpenInterval RIGHT_OPEN_INTERVAL_ZERO_TO_TAU = RightOpenInterval.of(0, Angle.TAU);
    private static final ClosedInterval CLOSED_INTERVAL_SYM_PI = ClosedInterval.symmetric(Math.PI);

    /**
     * Constructor of the horizontal coordinates
     *
     * @param longitude the longitude
     * @param latitude  the latitude
     */
    private HorizontalCoordinates(double longitude, double latitude) {
        super(longitude, latitude);
    }

    /**
     * Create horizontal coordinates from given azimuth and altitude in rad
     *
     * @param az  the azimuth in rad
     * @param alt the altitude in rad
     * @return the horizontal coordinates of given azimuth and altitude in rad
     * @throws IllegalArgumentException if az isn't in [0, 2PI[ or alt isn't in [-PI/2, PI/2]
     */
    public static HorizontalCoordinates of(double az, double alt) {
        return new HorizontalCoordinates(
                Preconditions.checkInInterval(RIGHT_OPEN_INTERVAL_ZERO_TO_TAU, az),
                Preconditions.checkInInterval(CLOSED_INTERVAL_SYM_PI, alt)
        );
    }

    /**
     * Create horizontal coordinates from given azimuth and altitude in deg
     *
     * @param azDeg  the azimuth in deg
     * @param altDeg the altitude in deg
     * @return the horizontal coordinates of given azimuth and altitude in deg
     */
    public static HorizontalCoordinates ofDeg(double azDeg, double altDeg) {
        return of(Angle.ofDeg(azDeg), Angle.ofDeg(altDeg));
    }

    /**
     * Getter for the azimuth in rad
     *
     * @return the azimuth in rad
     */
    public double az() {
        return super.lon();
    }

    /**
     * Getter for the azimuth in deg
     *
     * @return the azimuth in deg
     */
    public double azDeg() {
        return super.lonDeg();
    }

    /**
     * Return the octant name where the azimuth is
     *
     * @param n the north
     * @param e the east
     * @param s the south
     * @param w the west
     * @return the octant where the azimuth is
     */
    public String azOctantName(String n, String e, String s, String w) {
        // compute the index of the direction in the array at line 77
        int index = (int) (az() / (Math.PI / 4)) + ((int) ((az() % (Math.PI / 4)) / (Math.PI / 8)) == 0 ? 0 : 1);
        return new String[]{n, n + e, e, s + e, s, s + w, w, n + w}[index % 8];
    }

    /**
     * Getter for the altitude in rad
     *
     * @return the altitude in rad
     */
    public double alt() {
        return super.lat();
    }

    /**
     * Getter for the altitude in deg
     *
     * @return the altitude in deg
     */
    public double altDeg() {
        return super.latDeg();
    }

    /**
     * Return the angular distance with the given point
     *
     * @param that the given point
     * @return the angular distance with the given point
     */
    public double angularDistanceTo(HorizontalCoordinates that) {
        return Math.acos(Math.sin(alt()) * Math.sin(that.alt()) + Math.cos(alt()) * Math.cos(that.alt()) * Math.cos(az() - that.az()));
    }

    @Override
    public String toString() {
        return String.format(Locale.ROOT, "(az=%.4f°, alt=%.4f°)", azDeg(), altDeg());
    }

}

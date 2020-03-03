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

public final class HorizontalCoordinates extends SphericalCoordinates {

    /**
     * constructor of the horizontal coordinates
     *
     * @param longitude the longitude
     * @param latitude  the latitude
     */
    private HorizontalCoordinates(double longitude, double latitude) {
        super(longitude, latitude);
    }

    /**
     * Creating horizontal coordinates from given azimuth and altitude in rad
     *
     * @param az  the azimuth in rad
     * @param alt the altitude in rad
     * @return the horizontal coordinates of given azimuth and altitude in rad
     */
    public static HorizontalCoordinates of(double az, double alt) {
        return new HorizontalCoordinates(
                Preconditions.checkInInterval(RightOpenInterval.of(0, Angle.TAU), az),
                Preconditions.checkInInterval(ClosedInterval.symmetric(Math.PI), alt)
        );
    }

    /**
     * Creating horizontal coordinates from given azimuth and altitude in deg
     *
     * @param azDeg  the azimuth in deg
     * @param altDeg the altitude in deg
     * @return the horizontal coordinates of given azimuth and altitude in deg
     */
    public static HorizontalCoordinates ofDeg(double azDeg, double altDeg) {
        return of(Angle.ofDeg(azDeg), Angle.ofDeg(altDeg));
    }

    /**
     * @return the azimuth in rad
     */
    public double az() {
        return super.lon();
    }

    /**
     * @return the azimuth in deg
     */
    public double azDeg() {
        return super.lonDeg();
    }

    /**
     * @param n the north
     * @param e the east
     * @param s the south
     * @param w the west
     * @return the octant where the azimuth is
     */
    public String azOctantName(String n, String e, String s, String w) {
        int index = (int) (az() / (Math.PI / 4)) + ((int) ((az() % (Math.PI / 4)) / (Math.PI / 8)) == 0 ? 0 : 1);
        return new String[]{n, n + e, e, s + e, s, s + w, w, n + w}[index == 8 ? 0 : index];
    }

    /**
     * @return the altitude in rad
     */
    public double alt() {
        return super.lat();
    }

    /**
     * @return the altitude in deg
     */
    public double altDeg() {
        return super.latDeg();
    }

    /**
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

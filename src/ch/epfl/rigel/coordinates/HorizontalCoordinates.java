package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.ClosedInterval;
import ch.epfl.rigel.math.RightOpenInterval;

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
     * or throws an exception if at least one of the two value is invalid
     */
    public static HorizontalCoordinates of(double az, double alt) {
        if (RightOpenInterval.of(0, Angle.TAU).contains(az) && ClosedInterval.of(-Math.PI / 2, Math.PI / 2).contains(alt)) {
            return new HorizontalCoordinates(az, alt);
        } else {
            throw new IllegalArgumentException();
        }
    }

    /**
     * Creating horizontal coordinates from given azimuth and altitude in deg
     *
     * @param azDeg  the azimuth in deg
     * @param altDeg the altitude in deg
     * @return the horizontal coordinates of given azimuth and altitude in deg
     * or throws an exception if at least one of the two value is invalid
     */
    // TODO: 22/02/2020 what is the unit of default longitude and latitude of the super class ?
    public static HorizontalCoordinates ofDeg(double azDeg, double altDeg) {
        if (RightOpenInterval.of(0, 360).contains(azDeg) && ClosedInterval.of(-90, 90).contains(altDeg)) {
            return new HorizontalCoordinates(Angle.ofDeg(azDeg), Angle.ofDeg(altDeg));
        } else {
            throw new IllegalArgumentException();
        }
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
    double azDeg() {
        return super.lonDeg();
    }

    public String azOctantName(String n, String e, String s, String w) {
        return "";
    }

    /**
     * @return the altitude in rad
     */
    double alt() {
        return super.lat();
    }

    /**
     * @return the altitude in deg
     */
    double altDeg() {
        return super.latDeg();
    }


}

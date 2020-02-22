package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.ClosedInterval;
import ch.epfl.rigel.math.RightOpenInterval;

/**
 * @author Bastien Faivre (310929)
 * @author Kamil Mellouk (312327)
 */

public final class HorizontalCoordinates extends SphericalCoordinates {

    private HorizontalCoordinates(double longitude, double latitude) {
        super(longitude, latitude);
    }

    public static HorizontalCoordinates of(double az, double alt) {
        if (RightOpenInterval.of(0, Angle.TAU).contains(az) && ClosedInterval.of(-Math.PI / 2, Math.PI / 2).contains(alt)) {
            return new HorizontalCoordinates(az, alt);
        } else {
            throw new IllegalArgumentException();
        }
    }

    public static HorizontalCoordinates ofDeg(double azDeg, double altDeg) {
        if (RightOpenInterval.of(0, 360).contains(azDeg) && ClosedInterval.of(-90, 90).contains(altDeg)) {
            return new HorizontalCoordinates(azDeg, altDeg);
        } else {
            throw new IllegalArgumentException();
        }
    }

    public double az() {
        return super.lon();
    }

    double azDeg() {
        return super.lonDeg();
    }

    double alt() {
        return super.lat();
    }

    double altDeg() {
        return super.latDeg();
    }


}

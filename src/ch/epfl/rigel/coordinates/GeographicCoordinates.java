package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.ClosedInterval;
import ch.epfl.rigel.math.RightOpenInterval;

/**
 * @author Mohamed Kamil MELLOUK
 * 19.02.20
 */
public final class GeographicCoordinates extends SphericalCoordinates {

    private GeographicCoordinates(double longitude, double latitude) {
        super(longitude, latitude);
    }

    GeographicCoordinates ofDeg(double lonDeg, double latDeg) {
        if(isValidLongDeg(lonDeg) && isValidLatDeg(latDeg)){
            return new GeographicCoordinates(lonDeg, latDeg);
        } else {
            throw new IllegalArgumentException();
        }
    }

    public static boolean isValidLongDeg(double lonDeg) {
        return RightOpenInterval.of(-180,180).contains(lonDeg);
    }

    public static boolean isValidLatDeg(double latDeg) {
        return ClosedInterval.of(-90,90).contains(latDeg);
    }


    /*
    @Override
    public double lon() {
        return longitude;
    }


    double lonDeg() {
        return Angle.toDeg(longitude);
    }

    double lat() {
        return latitude;
    }


    double latDeg() {
        return Angle.toDeg(latitude);
    }
    */
}

package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.math.Angle;

/**
 * @author Mohamed Kamil MELLOUK
 * 19.02.20
 */
abstract class SphericalCoordinates {

    /// Longitude and latitude, used to represent any position on a sphere
    private double longitude;
    private double latitude;

    SphericalCoordinates(double longitude, double latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    /**
     * @return longitude, in rad
     */
    double lon() {
        return longitude;
    }

    /**
     * @return longitude, in deg
     */
    double lonDeg() {
        return Angle.toDeg(longitude);
    }

    /**
     * @return latitude, in rad
     */
    double lat() {
        return latitude;
    }

    /**
     * @return latitude, in deg
     */
    double latDeg() {
        return Angle.toDeg(latitude);
    }

    /**
     * @param obj the object
     * @return nothing
     * @throws UnsupportedOperationException to guarantee that no subclass redefines the method
     */
    @Override
    public final boolean equals(Object obj) throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    /**
     * @return nothing
     * @throws UnsupportedOperationException to guarantee that no subclass redefines the method
     */
    @Override
    public final int hashCode() throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }
}

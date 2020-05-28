package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.math.Angle;

/**
 * Abstract representation of a position on a sphere using spherical coordinates
 *
 * @author Bastien Faivre (310929)
 * @author Kamil Mellouk (312327)
 * @see EclipticCoordinates
 * @see EquatorialCoordinates
 * @see GeographicCoordinates
 * @see HorizontalCoordinates
 */
abstract class SphericalCoordinates {

    // Longitude and latitude, the spherical coordinates used to represent any position on a sphere
    private final double longitude;
    private final double latitude;

    /**
     * Constructor of the spherical coordinates
     *
     * @param longitude the longitude
     * @param latitude  the latitude
     */
    SphericalCoordinates(double longitude, double latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    /**
     * Getter for the longitude in rad
     *
     * @return longitude, in rad
     */
    double lon() {
        return longitude;
    }

    /**
     * Getter for the longitude in deg
     *
     * @return longitude, in deg
     */
    double lonDeg() {
        return Angle.toDeg(longitude);
    }

    /**
     * Getter for the latitude in rad
     *
     * @return latitude, in rad
     */
    double lat() {
        return latitude;
    }

    /**
     * Getter for tha latitude in deg
     *
     * @return latitude, in deg
     */
    double latDeg() {
        return Angle.toDeg(latitude);
    }

    /**
     * @return nothing
     * @throws UnsupportedOperationException to guarantee that no subclass redefines the method
     */
    @Override
    public final int hashCode() {
        throw new UnsupportedOperationException();
    }

    /**
     * @param obj the object
     * @return nothing
     * @throws UnsupportedOperationException to guarantee that no subclass redefines the method
     */
    @Override
    public final boolean equals(Object obj) {
        throw new UnsupportedOperationException();
    }
}

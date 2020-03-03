package ch.epfl.rigel.coordinates;

import java.util.Locale;

/**
 * @author Bastien Faivre (310929)
 * @author Kamil Mellouk (312327)
 */
public final class CartesianCoordinates {

    private double x, y;

    /**
     * Constructing a new set of cartesian coordinates
     * @param x x-coordinate
     * @param y y-coordinate
     */
    private CartesianCoordinates(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Constructing a new set of cartesian coordinates
     * @param x x-coordinate
     * @param y y-coordinate
     * @return a new CartesianCoordinates of coordinates x and y
     */
    public static CartesianCoordinates of(double x, double y) {
        return new CartesianCoordinates(x, y);
    }

    /**
     * Getter for the x-coordinate
     * @return x-coordinate of this
     */
    public double x() { return this.x; }

    /**
     * Getter for the y-coordinate
     * @return y-coordinate of this
     */
    public double y() { return this.y; }

    @Override
    public String toString() {
        return String.format(Locale.ROOT, "(x, y) = (%s, %s)", this.x, this.y);
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
package ch.epfl.rigel.coordinates;

import java.util.Locale;

/**
 * Representation of a position on the x-y plane
 *
 * @author Bastien Faivre (310929)
 * @author Kamil Mellouk (312327)
 */
public final class CartesianCoordinates {

    // The cartesian coordinates
    private final double x, y;

    /**
     * Constructor of a new set of cartesian coordinates
     *
     * @param x x-coordinate
     * @param y y-coordinate
     */
    private CartesianCoordinates(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Construct a new set of cartesian coordinates
     *
     * @param x x-coordinate
     * @param y y-coordinate
     * @return a new CartesianCoordinates of coordinates x and y
     */
    public static CartesianCoordinates of(double x, double y) {
        return new CartesianCoordinates(x, y);
    }

    /**
     * Getter for the x-coordinate
     *
     * @return x-coordinate of this
     */
    public double x() {
        return this.x;
    }

    /**
     * Getter for the y-coordinate
     *
     * @return y-coordinate of this
     */
    public double y() {
        return this.y;
    }

    /**
     * @return nothing
     * @throws UnsupportedOperationException the exception to throw
     */
    @Override
    public final int hashCode() {
        throw new UnsupportedOperationException();
    }

    /**
     * @param obj the object
     * @return nothing
     * @throws UnsupportedOperationException the exception to throw
     */
    @Override
    public final boolean equals(Object obj) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String toString() {
        return String.format(Locale.ROOT, "(x=%.4f, y=%.4f)", this.x, this.y);
    }

}

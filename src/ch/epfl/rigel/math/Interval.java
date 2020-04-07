package ch.epfl.rigel.math;

/**
 * Representation of a mathematical interval
 *
 * @see ClosedInterval
 * @see RightOpenInterval
 *
 * @author Bastien Faivre (310929)
 * @author Kamil Mellouk (312327)
 */

public abstract class Interval {

    // Bounds of the interval
    private final double lowBound;
    private final double highBound;

    /**
     * Constructor of the interval
     *
     * @param lowBound  the low bound
     * @param highBound the high bound
     */
    protected Interval(double lowBound, double highBound) {
        this.lowBound = lowBound;
        this.highBound = highBound;
    }

    /**
     * Getter for the low bound
     *
     * @return the low bound
     */
    public double low() {
        return lowBound;
    }

    /**
     * Getter for the high bound
     *
     * @return the high bound
     */
    public double high() {
        return highBound;
    }

    /**
     * Getter for the size of the interval
     *
     * @return the size of the interval
     */
    public double size() {
        return highBound - lowBound;
    }

    /**
     * Return true if and only if the value belongs to the interval
     *
     * @param v the value to check
     * @return {@code true} if and only if the value belongs to the interval
     */
    public abstract boolean contains(double v);

    /**
     * @return nothing
     * @throws UnsupportedOperationException to guarantee that no subclass redefines the method
     */
    @Override
    public final int hashCode() throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
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
}

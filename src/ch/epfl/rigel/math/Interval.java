package ch.epfl.rigel.math;

/**
 * @author Bastien Faivre (310929)
 * @author Kamil Mellouk ()
 */

public abstract class Interval {

    // bounds of the interval
    private final double lowBound;
    private final double highBound;

    /**
     * constructor of the interval
     *
     * @param lowBound
     *      the low bound
     * @param highBound
     *      the high bound
     */
    protected Interval(double lowBound, double highBound) {
        this.lowBound = lowBound;
        this.highBound = highBound;
    }

    /**
     * @return the low bound
     */
    public double low() {
        return lowBound;
    }

    /**
     * @return the high bound
     */
    public double high() {
        return highBound;
    }

    /**
     * @return the size of the interval
     */
    public double size() {
        return highBound - lowBound;
    }

    /**
     * @param v
     *      the value to check
     * @return {@code true} if and only if the value belongs to the interval
     */
    public abstract boolean contains(double v);

    /**
     * @param obj
     *      the object
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

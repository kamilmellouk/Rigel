package ch.epfl.rigel.math;

import ch.epfl.rigel.Preconditions;

import java.util.Locale;

/**
 * Representation of mathematical closed interval
 *
 * @author Bastien Faivre (310929)
 * @author Kamil Mellouk (312327)
 */
public final class ClosedInterval extends Interval {

    /**
     * ClosedInterval constructor, only calls super constructor
     *
     * @param lowBound  the low bound
     * @param highBound the high bound
     */
    private ClosedInterval(double lowBound, double highBound) {
        super(lowBound, highBound);
    }

    /**
     * Returning a closed interval given its bounds
     *
     * @param lowBound  the low bound
     * @param highBound the high bound
     * @return a closed interval
     * @throws IllegalArgumentException if lowBound >= highBound
     */
    public static ClosedInterval of(double lowBound, double highBound) {
        // check exception
        Preconditions.checkArgument(lowBound < highBound);

        return new ClosedInterval(lowBound, highBound);
    }

    /**
     * Returning an interval of a given size, centered around 0
     *
     * @param size the size of the interval
     * @return a closed interval of the specified size, centered around 0
     * @throws IllegalArgumentException if size <= 0
     */
    public static ClosedInterval symmetric(double size) {
        // check exception
        Preconditions.checkArgument(size > 0);

        return new ClosedInterval(-size / 2, size / 2);
    }

    /**
     * Return true if and only if the value belongs to the closed interval
     *
     * @param v the value to check
     * @return {@code true} if and only if the value belongs to the closed interval
     */
    @Override
    public boolean contains(double v) {
        return v >= low() && v <= high();
    }

    /**
     * Clipping the given value to the interval, ie returning the value in the interval that is closest to it
     *
     * @param v the value to clip
     * @return the clipped value, v if it is contained in the interval, and one of the two bounds if not
     */
    public double clip(double v) {
        return Math.max(low(), Math.min(v, high()));
    }

    /**
     * @return the textual representation of of the interval
     */
    @Override
    public String toString() {
        return String.format(Locale.ROOT, "[%s, %s]", low(), high());
    }
}

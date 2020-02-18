package ch.epfl.rigel.math;

import java.util.Locale;

/**
 * @author Mohamed Kamil MELLOUK
 * 17.02.20
 */
public final class ClosedInterval extends Interval {

    /**
     * ClosedInterval constructor, only calls super constructor
     *
     * @param lowBound      the low bound
     * @param highBound     the high bound
     */
    private ClosedInterval(double lowBound, double highBound) {
        super(lowBound, highBound);
    }

    /**
     * Returning a closed interval given its bounds
     *
     * @param lowBound      the low bound
     * @param highBound     the high bound
     * @return a closed interval or throws an exception if lowBound >= highBound
     */
    public static ClosedInterval of(double lowBound, double highBound) {
        if(lowBound < highBound) {
            return new ClosedInterval(lowBound, highBound);
        } else {
            throw new IllegalArgumentException();
        }
    }

    /**
     * Returning an interval of a given size, centered around 0
     *
     * @param size of the interval
     * @return a closed interval of the specified size, centered around 0, or throws an exception if size <= 0
     */
    public static ClosedInterval symmetric(double size) {
        if(size > 0) {
            return new ClosedInterval(- size/2,size/2);
        } else {
            throw new IllegalArgumentException();
        }
    }

    /**
     *
     * @param v     the value to check
     * @return {@code true} if and only if the value belongs to the closed interval
     */
    @Override
    public boolean contains(double v) {
        return v >= low() && v <= high();
    }

    /**
     * Clipping the given value to the interval, ie returning the value in the interval that is closest to it
     *
     * @param v     the value to clip
     * @return the clipped value, v if it is contained in the interval, and one of the two bounds if not
     */
    public double clip(double v) {
        return Math.max(low(), Math.min(v, high()));
    }

    /**
     *
     * @return the textual representation of of the interval
     */
    @Override
    public String toString() {
        return String.format(Locale.ROOT,"[%s, %s]", low(), high());
    }
}

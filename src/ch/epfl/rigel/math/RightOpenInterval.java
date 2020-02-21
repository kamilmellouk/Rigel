package ch.epfl.rigel.math;

import java.util.Locale;

public final class RightOpenInterval extends Interval {

    /**
     * RightOpenInterval constructor, only calls super constructor
     *
     * @param lowBound  the low bound
     * @param highBound the high bound
     */
    private RightOpenInterval(double lowBound, double highBound) {
        super(lowBound, highBound);
    }

    /**
     * Returning a right open interval given its bounds
     *
     * @param lowBound  the low bound
     * @param highBound the high bound
     * @return a right open interval, or throws an exception if lowBound >= highBound
     */
    public static RightOpenInterval of(double lowBound, double highBound) {
        if (lowBound < highBound) {
            return new RightOpenInterval(lowBound, highBound);
        } else {
            throw new IllegalArgumentException();
        }
    }

    /**
     * Returning a right open interval of a given size, centered around 0
     *
     * @param size of the interval
     * @return a right open interval of the specified size, centered around 0, or throws an exception if size <= 0
     */
    public static RightOpenInterval symmetric(double size) {
        if (size > 0) {
            return new RightOpenInterval(-size / 2, size / 2);
        } else {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public boolean contains(double v) {
        return (v >= low() && v < high());
    }

    /**
     * Reducing a given value to a given interval
     *
     * @param v the value to reduce
     * @return the reduced value, which is v mod the interval
     */
    public double reduce(double v) {
        return low() + (v - low()) - (high() - low()) * Math.floor((v - low()) / (high() - low()));
    }

    @Override
    public String toString() {
        return String.format(Locale.ROOT, "[%s, %s[", low(), high());
    }
}

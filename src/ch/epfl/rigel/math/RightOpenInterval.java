package ch.epfl.rigel.math;

import ch.epfl.rigel.Preconditions;

import java.util.Locale;

/**
 * @author Bastien Faivre (310929)
 * @author Kamil Mellouk (312327)
 */

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
     * @return a right open interval
     * @throws IllegalArgumentException if lowBound >= highBound
     */
    public static RightOpenInterval of(double lowBound, double highBound) {
        // check exception
        Preconditions.checkArgument(lowBound < highBound);

        return new RightOpenInterval(lowBound, highBound);
    }

    /**
     * Returning a right open interval of a given size, centered around 0
     *
     * @param size the size of the interval
     * @return a right open interval of the specified size, centered around 0
     * @throws IllegalArgumentException if size <= 0
     */
    public static RightOpenInterval symmetric(double size) {
        // check exception
        Preconditions.checkArgument(size > 0);

        return new RightOpenInterval(-size / 2, size / 2);
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

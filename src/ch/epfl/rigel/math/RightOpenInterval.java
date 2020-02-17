package ch.epfl.rigel.math;

import java.util.Locale;

/**
 * @author Mohamed Kamil MELLOUK
 * 17.02.20
 */
public final class RightOpenInterval extends Interval {

    /**
     * RightOpenInterval constructor, only calls super constructor
     *
     * @param lowBound      the low bound
     * @param highBound     the high bound
     */
    private RightOpenInterval(double lowBound, double highBound) {
        super(lowBound, highBound);
    }

    /**
     * Returning a right open interval given its bounds
     *
     * @param lowBound      the low bound
     * @param highBound     the high bound
     * @return a right open interval, or throws an exception if lowBound >= highBound
     */
    public static RightOpenInterval of(double lowBound, double highBound) {
        if (lowBound < highBound) {
            return new RightOpenInterval(lowBound, highBound);
        } else {
            throw new IllegalArgumentException();
        }
        // TODO: 17/02/2020 see why the exception is false
        //return lowBound < highBound ? new RightOpenInterval(lowBound, highBound) : throw new IllegalArgumentException();
    }

    /**
     * Returning a right open interval of a given size, centered around 0
     *
     * @param size of the interval
     * @return a right open interval of the specified size, centered around 0, or throws an exception if size <= 0
     */
    public static RightOpenInterval symmetric(double size) {
        if (size > 0) {
            return new RightOpenInterval(- size / 2,size / 2);
        } else {
            throw new IllegalArgumentException();
        }
        // TODO: 17/02/2020 see why the exception is false
        //return size > 0 ? new RightOpenInterval(- size/2, size/2) : throw new IllegalArgumentException();
    }

    /**
     *
     * @param v     the value to check
     * @return {@code true} if and only if the value belongs to the closed interval
     */
    @Override
    public boolean contains(double v) {
        // TODO: 17/02/2020 check the sign for right bound to be sure
        return (v >= low() && v < high());
    }

    /**
     * Reducing a given value to a given interval
     *
     * @param v     the value to reduce
     * @return the reduced value, which is v mod the interval
     */
    public double reduce(double v) {
        /*double reduceValue = v;
        while (!contains(reduceValue)) {
            if(reduceValue < low()) reduceValue += size();
            else if(reduceValue > high()) reduceValue -= size();
        }
        return reduceValue;*/
        // TODO: 17/02/2020 check with Kamil its code that I don't understand
        return low() + (v - low()) - (high() - low())*Math.floor((v - low())/(high() - low()));
    }

    /**
     *
     * @return the textual representation of of the interval
     */
    @Override
    public String toString() {
        return String.format(Locale.ROOT,"[%s, %s[", low(), high());
    }
}

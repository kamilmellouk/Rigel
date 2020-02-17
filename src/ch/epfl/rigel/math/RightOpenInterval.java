package ch.epfl.rigel.math;

import java.util.Locale;

/**
 * @author Mohamed Kamil MELLOUK
 * 17.02.20
 */
public final class RightOpenInterval extends Interval {

    /**
     * RightOpenInterval constructor, only calls superconstructor
     *
     * @param LOW_BOUND
     * @param HIGH_BOUND
     */
    private RightOpenInterval(double LOW_BOUND, double HIGH_BOUND) {
        super(LOW_BOUND, HIGH_BOUND);
    }

    /**
     * Returning a closed interval given its bounds
     *
     * @param low  bound
     * @param high bound
     * @return Right Open Interval, and throws an exception if low >= high
     */
    public static RightOpenInterval of(double low, double high) {
        if (low < high) {
            return new RightOpenInterval(low, high);
        } else {
            throw new IllegalArgumentException();
        }
    }

    /**
     * Returning an interval of a given size, centered around 0
     *
     * @param size of the interval
     * @return Right Open Interval of that size, centered around 0, and throws an exception if size <= 0
     */
    public static RightOpenInterval symmetric(double size) {
        if (size > 0) {
            return new RightOpenInterval(0 - size / 2, 0 + size / 2);
        } else {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public boolean contains(double v) {
        return (v >= this.low() && v < this.high());
    }

    /**
     * Reducing a given value to a given interval
     * @param v value to reduce
     * @return reducedValue, which is v mod the interval
     */
    public double reduce(double v) {
        double reducedV = v;
        while (!contains(reducedV)) {
            if(reducedV < low()) reducedV += size();
            else if(reducedV > high()) reducedV -= size();
        }
        return reducedV;
    }

    @Override
    public String toString() {
        return String.format(Locale.ROOT,"[%s, %s[", low(), high());
    }
}

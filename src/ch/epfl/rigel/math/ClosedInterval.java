package ch.epfl.rigel.math;

import java.util.Locale;

/**
 * @author Mohamed Kamil MELLOUK
 * 17.02.20
 */
public final class ClosedInterval extends Interval {

    /**
     * ClosedInterval constructor, only calls superconstructor
     * @param LOW_BOUND
     * @param HIGH_BOUND
     */
    private ClosedInterval(double LOW_BOUND, double HIGH_BOUND) {
        super(LOW_BOUND, HIGH_BOUND);
    }

    /**
     * Returning a closed interval given its bounds
     * @param low bound
     * @param high bound
     * @return Closed Interval, and throws an exception if low >= high
     */
    public static ClosedInterval of(double low, double high) {
        if(low < high) {
            return new ClosedInterval(low, high);
        } else {
            throw new IllegalArgumentException();
        }
    }

    /**
     * Returning an interval of a given size, centered around 0
     * @param size of the interval
     * @return Closed Interval of that size, centered around 0, and throws an exception if size <= 0
     */
    public static ClosedInterval symmetric(double size) {
        if(size > 0) {
            return new ClosedInterval(0 - size/2, 0 + size/2);
        } else {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public boolean contains(double v) {
        return (v >= this.low() && v <= this.high());
    }

    /**
     * Clipping a given value to the interval, ie returning the value in the interval that is closest to it
     * @param v value to clip
     * @return clipped value, v if v is contained in the interval, and one of the two bounds if not
     */
    public double clip(double v) {
        if(v <= this.low()) {
            return this.low();
        } else if(v >= this.high()) {
            return this.high();
        } else {
            return v;
        }
    }

    @Override
    public String toString() {
        return String.format(Locale.ROOT,"[%s, %s]", low(), high());
    }
}

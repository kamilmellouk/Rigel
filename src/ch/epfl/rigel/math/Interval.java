package ch.epfl.rigel.math;

/**
 * @author Bastien Faivre
 * 17/02/2020
 */
public abstract class Interval {

    // bounds of the interval
    private final double low_bound;
    private final double high_bound;

    /**
     * constructor of the interval
     *
     * @param low_bound     the low bound
     * @param high_bound    the high bound
     */
    protected Interval(double low_bound, double high_bound) {
        this.low_bound = low_bound;
        this.high_bound = high_bound;
    }

    /**
     *
     * @return the low bound
     */
    public double low() {
        return low_bound;
    }

    /**
     *
     * @return the high bound
     */
    public double high() {
        return high_bound;
    }

    /**
     *
     * @return the size of the interval
     */
    public double size() {
        return high_bound - low_bound;
    }

}

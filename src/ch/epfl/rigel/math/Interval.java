package ch.epfl.rigel.math;

/**
 * @author Bastien Faivre
 * 17/02/2020
 */
public abstract class Interval {

    // bounds of the interval
    private final double lowBound;
    private final double highBound;

    /**
     * constructor of the interval
     *
     * @param lowBound     the low bound
     * @param highBound    the high bound
     */
    protected Interval(double lowBound, double highBound) {
        this.lowBound = lowBound;
        this.highBound = highBound;
    }

    /**
     *
     * @return the low bound
     */
    public double low() {
        return lowBound;
    }

    /**
     *
     * @return the high bound
     */
    public double high() {
        return highBound;
    }

    /**
     *
     * @return the size of the interval
     */
    public double size() {
        return highBound - lowBound;
    }

    public abstract boolean contains(double v);

}

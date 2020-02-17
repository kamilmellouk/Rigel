package ch.epfl.rigel.math;

/**
 * @author Bastien Faivre
 * 17/02/2020
 */
public abstract class Interval {

    /// Bounds of the interval
    private final double LOW_BOUND;
    private final double HIGH_BOUND;

    /**
     * Constructor of the interval
     * @param LOW_BOUND     the low bound
     * @param HIGH_BOUND    the high bound
     */
    protected Interval(double LOW_BOUND, double HIGH_BOUND) {
        this.LOW_BOUND = LOW_BOUND;
        this.HIGH_BOUND = HIGH_BOUND;
    }

    /**
     * Returns the low bound
     * @return the low bound
     */
    public double low() {
        return LOW_BOUND;
    }

    /**
     * Returns the high bound
     * @return the high bound
     */
    public double high() {
        return HIGH_BOUND;
    }

    /**
     * Returns the size of the interval
     * @return the size of the interval
     */
    public double size() {
        return HIGH_BOUND - LOW_BOUND;
    }

    /**
     * Testing if a certain value is contained in the interval
     * @param v value to test
     * @return true if and only if v in contained in the interval
     */
    public abstract boolean contains(double v);

    /*
    @Override
    public final int hashCode() {
        return super.hashCode();
    }
     */
}

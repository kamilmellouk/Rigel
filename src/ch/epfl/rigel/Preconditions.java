package ch.epfl.rigel;

import ch.epfl.rigel.math.Interval;

public final class Preconditions {

    private Preconditions() {}

    /**
     * Trowing an IllegalArgumentException if a condition is not satisfied
     * @param isTrue condition to check
     */
    public static void checkArgument(boolean isTrue) {
        if(!isTrue)
            throw new IllegalArgumentException();
    }

    /**
     * Checking if a certain value is contained in an interval
     * @param interval used
     * @param value to check
     * @return value if it is contained in interval, and throws an IllegalArgumentException if not
     */
    public static double checkInInterval(Interval interval, double value) {
        if(interval.contains(value))
            return value;
        else
            throw new IllegalArgumentException();
    }
}

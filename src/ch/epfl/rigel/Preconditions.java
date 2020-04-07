package ch.epfl.rigel;

import ch.epfl.rigel.math.Interval;

/**
 * @author Bastien Faivre (310929)
 * @author Kamil Mellouk (312327)
 */

public final class Preconditions {

    private Preconditions() {
    }

    /**
     * Throwing an IllegalArgumentException if a condition is not satisfied
     *
     * @param isTrue the condition to check
     * @throws IllegalArgumentException if the condition to check is false
     */
    public static void checkArgument(boolean isTrue) {
        if (!isTrue) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * Checking if a certain value is contained in an interval
     *
     * @param interval the interval used
     * @param value    the value to check
     * @return the value if it is contained in the interval
     * @throws IllegalArgumentException if value isn't contained in interval
     */
    public static double checkInInterval(Interval interval, double value) {
        checkArgument(interval.contains(value));
        return value;
    }
}

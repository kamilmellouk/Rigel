package ch.epfl.rigel;

import ch.epfl.rigel.math.Interval;

/**
 * @author Bastien Faivre (310929)
 * @author Kamil Mellouk ()
 */

public final class Preconditions {

    private Preconditions() {
    }

    /**
     * Throwing an IllegalArgumentException if a condition is not satisfied
     *
     * @param isTrue condition to check
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
     * @return the value if it is contained in the interval, or throws an IllegalArgumentException if not
     */
    public static double checkInInterval(Interval interval, double value) {
        if (interval.contains(value)) {
            return value;
        } else {
            throw new IllegalArgumentException();
        }
    }
}

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

    public static double checkInInterval(Interval interval, double value) {
        return value;
    }
}

package ch.epfl.rigel;

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

}

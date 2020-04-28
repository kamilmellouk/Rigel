package ch.epfl.rigel.math;

import ch.epfl.rigel.Preconditions;

/**
 * Useful methods to manipulate angles
 *
 * @author Bastien Faivre (310929)
 * @author Kamil Mellouk (312327)
 */
public final class Angle {

    // The constant tau
    public static final double TAU = 2 * Math.PI;

    // Constants for conversion (could be declared public, but only used locally so left private)
    private static final double HR_PER_RAD = 24.0 / TAU;
    private static final double RAD_PER_HR = 1 / HR_PER_RAD;
    private static final double MIN_PER_DEG = 60;
    private static final double SEC_PER_MIN = 60;
    private static final double SEC_PER_DEG = MIN_PER_DEG * SEC_PER_MIN;
    private static final double DEG_PER_TAU = 360;
    private static final double RAD_PER_SEC = TAU / (DEG_PER_TAU * MIN_PER_DEG * SEC_PER_MIN);

    private static final RightOpenInterval RIGHT_OPEN_INTERVAL_ZERO_TO_TAU = RightOpenInterval.of(0, TAU);
    private static final RightOpenInterval RIGHT_OPEN_INTERVAL_ZERO_TO_SIXTY = RightOpenInterval.of(0, 60);

    /**
     * Reducing a given angle to the right open interval [0,TAU[
     *
     * @param rad the angle to reduce (in rad)
     * @return the reduced angle in [0,TAU[
     */
    public static double normalizePositive(double rad) {
        return RIGHT_OPEN_INTERVAL_ZERO_TO_TAU.reduce(rad);
    }

    /**
     * Converting an angle given in arc seconds to rad
     *
     * @param sec the given angle (in arc sec)
     * @return the same angle expressed in rad
     */
    public static double ofArcsec(double sec) {
        return sec * RAD_PER_SEC;
    }

    /**
     * Converting an angle given in deg° min' sec" to rad
     *
     * @param deg the degrees value
     * @param min the minutes value
     * @param sec the seconds value
     * @return the same angle expressed in rad
     * @throws IllegalArgumentException if min or sec aren't in [0, 60[
     */
    public static double ofDMS(int deg, int min, double sec) {
        // check exceptions
        Preconditions.checkArgument(Math.abs(deg) == deg);
        Preconditions.checkInInterval(RIGHT_OPEN_INTERVAL_ZERO_TO_SIXTY, min);
        Preconditions.checkInInterval(RIGHT_OPEN_INTERVAL_ZERO_TO_SIXTY, sec);

        return ofArcsec(deg * SEC_PER_DEG + min * MIN_PER_DEG + sec);
    }

    /**
     * Converting an angle given in deg to rad
     *
     * @param deg the given angle
     * @return the same angle expressed in rad
     */
    public static double ofDeg(double deg) {
        return Math.toRadians(deg);
    }

    /**
     * Converting an angle given in rad to deg
     *
     * @param rad the given angle
     * @return the same angle expressed in deg
     */
    public static double toDeg(double rad) {
        return Math.toDegrees(rad);
    }

    /**
     * Converting an angle given in hours to rad
     *
     * @param hr the given angle
     * @return the same angle expressed in rad
     */
    public static double ofHr(double hr) {
        return hr * RAD_PER_HR;
    }

    /**
     * Converting an angle given in rad to hours
     *
     * @param rad the given angle
     * @return the same angle expressed in hours
     */
    public static double toHr(double rad) {
        return rad * HR_PER_RAD;
    }
}

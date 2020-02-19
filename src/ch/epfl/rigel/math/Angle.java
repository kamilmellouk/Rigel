package ch.epfl.rigel.math;

/**
 * @author Mohamed Kamil MELLOUK
 * 18.02.20
 */
public final class Angle {

    // the constant tau
    public static final double TAU = 2 * Math.PI;

    // constants for conversion
    private static final double HR_PER_RAD = 24.0 / TAU;
    private static final double RAD_PER_HR = 1 / HR_PER_RAD;
    private static final double RAD_PER_SEC = TAU / (60 * 60 * 360);

    /**
     * Reducing a given angle to the right open interval [0,TAU[
     *
     * @param rad the angle to reduce (in rad)
     * @return the reduced angle in [0,TAU[
     */
    public static double normalizePositive(double rad) {
        return RightOpenInterval.of(0, TAU).reduce(rad);
    }

    /**
     * Converting an angle given in arc seconds to rad
     *
     * @param sec the given angle (in arc sec)
     * @return the same angle expressed in rad
     */
    public static double ofArcSec(double sec) {
        return sec * RAD_PER_SEC;
    }

    /**
     * Converting an angle given in deg° min' sec" to rad
     *
     * @param deg the degrees value
     * @param min the minutes value
     * @param sec the seconds value
     * @return the same angle expressed in rad or throws an exception if the seconds or minutes value
     * do not belong to the interval [0,60[
     */
    public static double ofDMS(int deg, int min, double sec) {
        if ((RightOpenInterval.of(0, 60).contains(min)) &&
                (RightOpenInterval.of(0, 60).contains(sec))) {
            return ofArcSec(deg * 60 * 60 + min * 60 + sec);
        } else {
            throw new IllegalArgumentException();
        }
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

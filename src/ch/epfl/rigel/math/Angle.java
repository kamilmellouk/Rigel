package ch.epfl.rigel.math;

/**
 * @author Mohamed Kamil MELLOUK
 * 18.02.20
 */
public final class Angle {

    public final static double TAU = 2 * Math.PI;

    /**
     * Reducing a given angle to the right open interval [0,TAU[
     * @param rad angle to reduce (in rad)
     * @return reduced angle to [0,TAU[
     */
    public static double nozmalizePositive(double rad) {
        return RightOpenInterval.of(0, TAU).reduce(rad);
    }

    /**
     * Converting an angle given in arc seconds to rad
     * @param sec angle given (in arc sec)
     * @return same angle, expressed in rad
     */
    public static double ofArcSec(double sec) {
        return (sec * 24*60*60)/TAU;
    }

    /**
     * Converting an angle given in deg°min'sec'' to rad
     * @param deg
     * @param min
     * @param sec
     * @return same angle, expressed in rad
     */
    public static double ofDMS(int deg, int min, double sec) {
        if((RightOpenInterval.of(0, 60).contains(min)) &&
                (RightOpenInterval.of(0, 60).contains(sec))) {
            return ofArcSec(deg*24*60 + min*60 + sec);
        } else {
            throw new IllegalArgumentException();
        }
    }

    /**
     * Converting an angle given in deg to rad
     * @param deg angle given
     * @return same angle, expressed in rad
     */
    public static double ofDeg(double deg) {
        return (deg*360)/TAU;
    }

    /**
     * Converting an angle given in rad to deg
     * @param rad angle given
     * @return same angle, expressed in deg
     */
    public static double toDeg(double rad) {
        return (rad*TAU)/360;
    }

    /**
     * Converting an angle given in hours to rad
     * @param hr angle given
     * @return same angle, expressed in rad
     */
    public static double ofHr(double hr) {
        return (hr*24)/TAU;
    }

    /**
     * Converting an angle given in rad to hours
     * @param rad angle given
     * @return same angle, expressed in hours
     */
    public static double toHr(double rad) {
        return (rad*TAU)/24;
    }
}

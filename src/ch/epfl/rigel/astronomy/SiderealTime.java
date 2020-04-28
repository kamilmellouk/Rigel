package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.GeographicCoordinates;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.Polynomial;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import static java.time.temporal.ChronoUnit.DAYS;
import static java.time.temporal.ChronoUnit.MILLIS;

/**
 * Representing sidereal time with some methods (not instanciable class)
 *
 * @author Bastien Faivre (310929)
 * @author Kamil Mellouk (312327)
 */
public final class SiderealTime {

    // Polynomials used to compute the sidereal time
    private static final Polynomial S0_POLYNOMIAL = Polynomial.of(0.000025862, 2400.051336, 6.697374558);
    private static final Polynomial S1_POLYNOMIAL = Polynomial.of(1.002737909, 0);
    // Number of milliseconds in an hour
    private static final double MILLISECONDS_PER_HOUR = 3_600_000;

    /**
     * Private default constructor
     * The class is not instantiable
     */
    private SiderealTime() {
    }

    /**
     * Compute the sidereal time with respect to greenwich and J2000
     *
     * @param when date-time couple with time zone of observation
     * @return sidereal time (in rad) of this point of observation, with respect to greenwich and J2000
     */
    public static double greenwich(ZonedDateTime when) {
        ZonedDateTime whenInUTC = when.withZoneSameInstant(ZoneOffset.UTC);
        ZonedDateTime whenInUTCTruncatedToDays = whenInUTC.truncatedTo(DAYS);

        double T = Epoch.J2000.julianCenturiesUntil(whenInUTCTruncatedToDays);
        double t = whenInUTCTruncatedToDays.until(whenInUTC, MILLIS) / MILLISECONDS_PER_HOUR;
        double s0 = S0_POLYNOMIAL.at(T);
        double s1 = S1_POLYNOMIAL.at(t);

        return Angle.normalizePositive(Angle.ofHr(s0 + s1));
    }

    /**
     * Compute the sidereal time with respect to a given position and J2000
     *
     * @param when  date-time couple with time zone of observation
     * @param where position of observation
     * @return sidereal time of this point of observation, with respect to the given position and J2000
     */
    public static double local(ZonedDateTime when, GeographicCoordinates where) {
        return Angle.normalizePositive(greenwich(when) + where.lon());
    }

}

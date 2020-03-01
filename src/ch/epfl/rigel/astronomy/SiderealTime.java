package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.GeographicCoordinates;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.Polynomial;
import ch.epfl.rigel.math.RightOpenInterval;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

/**
 * @author Bastien Faivre (310929)
 * @author Kamil Mellouk (312327)
 */

public final class SiderealTime {
    private static final Polynomial s0Formula = Polynomial.of(0.000025862, 2400.051336, 6.697374558);
    private static final Polynomial s1Formula = Polynomial.of(1.002737909, 0);
    private static final double milisecondsPerDay = 3600000;

    // TODO: 29/02/2020 check with Oscar
    private SiderealTime() {
    }

    /**
     * Compute the sidereal time with respect to greenwich and J2000
     *
     * @param when date-time couple with time zone of observation
     * @return sidereal time (in rad) of this point of observation, with respect to greenwich and J2000
     */
    // TODO: 29/02/2020 check access and remove print
    public static double greenwich(ZonedDateTime when) {
        // Express the  given time in greenwich (UTC) time zone
        ZonedDateTime whenInUTC = when.withZoneSameInstant(ZoneOffset.UTC);
        System.out.println("when = "+when);
        System.out.println("whenInUTC = "+whenInUTC);
        System.out.println("trunc = "+whenInUTC.truncatedTo(ChronoUnit.DAYS));

        // Compute the number of julian centuries between J2000 and the given time expressed in UTC
        double T = Epoch.J2000.julianCenturiesUntil(whenInUTC.truncatedTo(ChronoUnit.DAYS));
        System.out.println("T = "+T);
        // Compute the number of hours between the beginning of the day containing the specific moment and the moment itself
        double t = whenInUTC.truncatedTo(ChronoUnit.DAYS).until(whenInUTC,ChronoUnit.MILLIS)/milisecondsPerDay;
        System.out.println("t = "+t);

        // compute the two values of the given polynomial at T and t
        double s0 = s0Formula.at(T);
        System.out.println("s0 = "+s0);
        double s1 = s1Formula.at(t);
        System.out.println("s1 = "+s1);
        System.out.println("s0+s1 = " + (s0+s1));

        // Reduce s0 + s1 to the interval [0, 24[ and convert it to rad
        return Angle.normalizePositive(Angle.ofHr(RightOpenInterval.of(0, 24).reduce(s0 + s1)));
    }

    /**
     * Compute the sidereal time with respect to a given position and J2000
     *
     * @param when  date-time couple with time zone of observation
     * @param where position of observation
     * @return sidereal time of this point of observation, with respect to the given position and J2000
     */
    // TODO: 29/02/2020 check access and interval
    public static double local(ZonedDateTime when, GeographicCoordinates where) {
        return RightOpenInterval.of(0,Angle.TAU).reduce(greenwich(when) + where.lon());
    }
}

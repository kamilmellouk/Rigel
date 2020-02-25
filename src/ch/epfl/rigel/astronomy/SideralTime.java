package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.GeographicCoordinates;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.RightOpenInterval;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;

/**
 * @author Bastien Faivre (310929)
 * @author Kamil Mellouk (312327)
 */
public final class SideralTime {

    /**
     * Computing the sideral time with respect to greenwich and J2000
     * @param when time and time zone of observation
     * @return sideral time of this point of observation, with respect to greenwich and J2000
     */
    public static double greenwich(ZonedDateTime when) {
        ZonedDateTime greenWhen = when.withZoneSameInstant(ZoneOffset.UTC);

        double t0 = Epoch.J2000.julianCenturiesUntil(greenWhen.truncatedTo(ChronoUnit.DAYS));
        double t1 = when.getHour();

        double s0 = 0.000025862*t0*t0 + 2400.051336*t0 + 6.697374558;
        double s1 = 1.002737909*t1;

        return Angle.ofHr(RightOpenInterval.of(0, 24).reduce(s0 + s1));
    }

    /**
     * Computing the sideral time with respect to a given position and J2000
     * @param when time and time zone of observation
     * @param where position of observation
     * @return sideral time of this point of observation, with respect to the given position and J2000
     */
    public static double local(ZonedDateTime when, GeographicCoordinates where) {
        return greenwich(when) + where.lon();
    }
}

package ch.epfl.rigel.astronomy;

import java.time.*;
import java.time.temporal.ChronoUnit;

/**
 * @author Bastien Faivre (310929)
 * @author Kamil Mellouk (312327)
 */

public enum Epoch {

    J2000(ZonedDateTime.of(LocalDate.of(2000, Month.JANUARY, 1),
            LocalTime.of(12, 0),
            ZoneOffset.UTC)),

    J2010(ZonedDateTime.of(LocalDate.of(2010, Month.JANUARY, 1).minusDays(1),
            LocalTime.of(0, 0),
            ZoneOffset.UTC));

    private ZonedDateTime date;

    // TODO: 29/02/2020 useful or not ?
    private static final double MILLIS_PER_DAY = 24 * 60 * 60 * 1000;

    Epoch(ZonedDateTime date) {
        this.date = date;
    }

    /**
     * Compute the difference between an Epoch and a given date-time couple with time zone
     *
     * @param when date-time couple (with time zone) to compare
     * @return difference in days (can be decimal)
     */
    public double daysUntil(ZonedDateTime when) {
        return date.until(when, ChronoUnit.MILLIS) / MILLIS_PER_DAY;
    }

    /**
     * Compute the difference between an Epoch and a given date-time couple with time zone
     *
     * @param when date-time couple (with time zone) to compare
     * @return difference in julian centuries (can be decimal)
     */
    public double julianCenturiesUntil(ZonedDateTime when) {
        return daysUntil(when) / (double) 36525;
    }

}

package ch.epfl.rigel.astronomy;

import java.time.*;
import java.time.temporal.ChronoUnit;

/**
 * Various epochs and useful methods
 *
 * @author Bastien Faivre (310929)
 * @author Kamil Mellouk (312327)
 */
public enum Epoch {

    // 01.01.2000 12am UTC
    J2000(ZonedDateTime.of(LocalDate.of(2000, Month.JANUARY, 1),
            LocalTime.of(12, 0),
            ZoneOffset.UTC)),

    // 00.01.2010 0am UTC
    J2010(ZonedDateTime.of(LocalDate.of(2010, Month.JANUARY, 1).minusDays(1),
            LocalTime.of(0, 0),
            ZoneOffset.UTC));

    // the specific date
    private ZonedDateTime date;

    /**
     * constructor of epoch
     *
     * @param date the specific date
     */
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
        return date.until(when, ChronoUnit.MILLIS) / (24d * 60 * 60 * 1000);
    }

    /**
     * Compute the difference between an Epoch and a given date-time couple with time zone
     *
     * @param when date-time couple (with time zone) to compare
     * @return difference in julian centuries (can be decimal)
     */
    public double julianCenturiesUntil(ZonedDateTime when) {
        return daysUntil(when) / 36525d;
    }

}

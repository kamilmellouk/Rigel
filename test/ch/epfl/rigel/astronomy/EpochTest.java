package ch.epfl.rigel.astronomy;

import org.junit.jupiter.api.Test;

import java.time.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Bastien Faivre (310929)
 * @author Kamil Mellouk (312327)
 */
public class EpochTest {

    @Test
    void daysUntilWorks() {
        // J2000
        assertEquals(2.25, Epoch.J2000.daysUntil(ZonedDateTime.of(LocalDate.of(2000, Month.JANUARY, 3),
                LocalTime.of(18, 0), ZoneOffset.UTC)));
        assertEquals(5.125, Epoch.J2000.daysUntil(ZonedDateTime.of(LocalDate.of(2000, Month.JANUARY, 6),
                LocalTime.of(15, 0), ZoneOffset.UTC)));
        // J2010
        assertEquals(2.5, Epoch.J2010.daysUntil(ZonedDateTime.of(LocalDate.of(2010, Month.JANUARY, 2),
                LocalTime.of(12, 0), ZoneOffset.UTC)));
        assertEquals(6.0625, Epoch.J2010.daysUntil(ZonedDateTime.of(LocalDate.of(2010, Month.JANUARY, 6),
                LocalTime.of(1, 30), ZoneOffset.UTC)));
    }

    @Test
    void daysUntilWorksOnNegativeDifference() {
        // J2000
        assertEquals(-365, Epoch.J2000.daysUntil(ZonedDateTime.of(LocalDate.of(1999, Month.JANUARY, 1),
                LocalTime.of(12, 0), ZoneOffset.UTC)));
        assertEquals(-1.75, Epoch.J2000.daysUntil(ZonedDateTime.of(LocalDate.of(1999, Month.DECEMBER, 30),
                LocalTime.of(18, 0), ZoneOffset.UTC)));
        // J2010
        assertEquals(-365, Epoch.J2010.daysUntil(ZonedDateTime.of(LocalDate.of(2009, Month.JANUARY, 1).minusDays(1),
                LocalTime.of(0, 0), ZoneOffset.UTC)));
        assertEquals(-1.75, Epoch.J2010.daysUntil(ZonedDateTime.of(LocalDate.of(2009, Month.DECEMBER, 29),
                LocalTime.of(6, 0), ZoneOffset.UTC)));
    }

    @Test
    void daysUntilWorksOnZeroDifference() {
        // J2000
        assertEquals(0, Epoch.J2000.daysUntil(ZonedDateTime.of(LocalDate.of(2000, Month.JANUARY, 1),
                LocalTime.of(12, 0), ZoneOffset.UTC)));
        // J2010
        assertEquals(0, Epoch.J2010.daysUntil(ZonedDateTime.of(LocalDate.of(2010, Month.JANUARY, 1).minusDays(1),
                LocalTime.of(0, 0), ZoneOffset.UTC)));
    }

    @Test
    void julianCenturiesUntilWorks() {
        // J2000
        assertEquals(0.01, Epoch.J2000.julianCenturiesUntil(ZonedDateTime.of(LocalDate.of(2000, Month.JANUARY, 1),
                LocalTime.of(12, 0), ZoneOffset.UTC).plusDays(365).plusHours(6)));
        assertEquals(0.001, Epoch.J2000.julianCenturiesUntil(ZonedDateTime.of(LocalDate.of(2000, Month.JANUARY, 1),
                LocalTime.of(12, 0), ZoneOffset.UTC).plusDays(36).plusHours(12).plusMinutes(36)));
        // J2010
        assertEquals(0.01, Epoch.J2010.julianCenturiesUntil(ZonedDateTime.of(LocalDate.of(2010, Month.JANUARY, 1).minusDays(1),
                LocalTime.of(0, 0), ZoneOffset.UTC).plusDays(365).plusHours(6)));
        assertEquals(0.001, Epoch.J2010.julianCenturiesUntil(ZonedDateTime.of(LocalDate.of(2010, Month.JANUARY, 1).minusDays(1),
                LocalTime.of(0, 0), ZoneOffset.UTC).plusDays(36).plusHours(12).plusMinutes(36)));
    }

    @Test
    void julianCenturiesUntilWorksOnNegativeDifference() {
        // J2000
        assertEquals(-1, Epoch.J2000.julianCenturiesUntil(ZonedDateTime.of(LocalDate.of(2000, Month.JANUARY, 1),
                LocalTime.of(12, 0), ZoneOffset.UTC).minusDays(36525)));
        assertEquals(-0.001, Epoch.J2000.julianCenturiesUntil(ZonedDateTime.of(LocalDate.of(2000, Month.JANUARY, 1),
                LocalTime.of(12, 0), ZoneOffset.UTC).minusDays(36).minusHours(12).minusMinutes(36)));
        // J2010
        assertEquals(-1, Epoch.J2010.julianCenturiesUntil(ZonedDateTime.of(LocalDate.of(2010, Month.JANUARY, 1).minusDays(1),
                LocalTime.of(0, 0), ZoneOffset.UTC).minusDays(36525)));
        assertEquals(-0.001, Epoch.J2010.julianCenturiesUntil(ZonedDateTime.of(LocalDate.of(2010, Month.JANUARY, 1).minusDays(1),
                LocalTime.of(0, 0), ZoneOffset.UTC).minusDays(36).minusHours(12).minusMinutes(36)));
    }

    @Test
    void julianCenturiesUntilWorksOnZeroDifference() {
        // J2000
        assertEquals(0, Epoch.J2000.julianCenturiesUntil(ZonedDateTime.of(LocalDate.of(2000, Month.JANUARY, 1),
                LocalTime.of(12, 0), ZoneOffset.UTC)));
        // J2010
        assertEquals(0, Epoch.J2010.julianCenturiesUntil(ZonedDateTime.of(LocalDate.of(2010, Month.JANUARY, 1).minusDays(1),
                LocalTime.of(0, 0), ZoneOffset.UTC)));
    }
}

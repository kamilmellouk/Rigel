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
        assertEquals(2.25, Epoch.J2000.daysUntil(ZonedDateTime.of(LocalDate.of(2000, Month.JANUARY, 3),
                LocalTime.of(18, 0), ZoneOffset.UTC)));
    }

    @Test
    void daysUntilWorksOnNegativeDifference() {
        assertEquals(-365, Epoch.J2000.daysUntil(ZonedDateTime.of(LocalDate.of(1999, Month.JANUARY, 1),
                LocalTime.of(12, 0), ZoneOffset.UTC)));
    }

    @Test
    void daysUntilWorksOnZeroDifference() {
        assertEquals(0, Epoch.J2000.daysUntil(ZonedDateTime.of(LocalDate.of(2000, Month.JANUARY, 1),
                LocalTime.of(12, 0), ZoneOffset.UTC)));
    }

    @Test
    void julianCenturiesUntilWorks() {
        assertEquals(0.01, Epoch.J2000.julianCenturiesUntil(ZonedDateTime.of(LocalDate.of(2000, Month.JANUARY, 1),
                LocalTime.of(12, 0), ZoneOffset.UTC).plusDays(365).plusHours(6)));
    }

    @Test
    void julianCenturiesUntilWorksOnNegativeDifference() {
        assertEquals(-1, Epoch.J2000.julianCenturiesUntil(ZonedDateTime.of(LocalDate.of(2000, Month.JANUARY, 1),
                LocalTime.of(12, 0), ZoneOffset.UTC).minusDays(36525)));
    }

    @Test
    void julianCenturiesUntilWorksOnZeroDifference() {
        assertEquals(0, Epoch.J2000.julianCenturiesUntil(ZonedDateTime.of(LocalDate.of(2000, Month.JANUARY, 1),
                LocalTime.of(12, 0), ZoneOffset.UTC)));
    }
}

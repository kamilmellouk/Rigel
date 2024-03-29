package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.GeographicCoordinates;
import ch.epfl.rigel.math.Angle;
import org.junit.jupiter.api.Test;

import java.time.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Bastien Faivre (310929)
 * @author Kamil Mellouk (312327)
 */
public class MySiderealTimeTest {

    @Test
    void greenwichWorks() {
        assertEquals(Angle.ofHr(4.668119327), SiderealTime.greenwich(ZonedDateTime.of(
                LocalDate.of(1980, Month.APRIL, 22),
                LocalTime.of(14, 36, 51, (int) 6.7e8),
                ZoneOffset.UTC)),
                1e-10);
    }

    @Test
    void greenwichWorksOnTrivialCase() {
        assertEquals(4.894961211636988, SiderealTime.greenwich(ZonedDateTime.of(LocalDate.of(2000, Month.JANUARY, 1),
                LocalTime.of(12, 0),
                ZoneOffset.UTC)));
    }

    @Test
    void localWorks() {
        assertEquals(Angle.ofHr(4.668119327)+Math.PI/4, SiderealTime.local(ZonedDateTime.of(
                LocalDate.of(1980, Month.APRIL, 22),
                LocalTime.of(14, 36, 51, (int) 6.7e8),
                ZoneOffset.UTC),
                GeographicCoordinates.ofDeg(45,0)),
                1e-10);
    }

    @Test
    void localWorksOnTrivialCase() {
        assertEquals(Angle.ofHr(4.668119327), SiderealTime.local(ZonedDateTime.of(
                LocalDate.of(1980, Month.APRIL, 22),
                LocalTime.of(14, 36, 51, (int) 6.7e8),
                ZoneOffset.UTC),
                GeographicCoordinates.ofDeg(0,0)),
                1e-10);
    }

}

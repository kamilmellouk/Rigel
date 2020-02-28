package ch.epfl.rigel.astronomy;

import org.junit.jupiter.api.Test;

import java.time.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Bastien Faivre (310929)
 * @author Kamil Mellouk (312327)
 */
public class SiderealTimeTest {

    @Test
    void greenwichWorks() {
        assertEquals(1.21762685685550, SiderealTime.greenwich(ZonedDateTime.of(LocalDate.of(1980, Month.APRIL, 22), LocalTime.of(14, 36, 51), ZoneOffset.UTC)));
        assertEquals(20.516202146767, SiderealTime.greenwich(ZonedDateTime.of(LocalDate.of(2001, Month.JANUARY, 27), LocalTime.of(12, 0, 0), ZoneOffset.UTC)));
        assertEquals(0.07288478193602, SiderealTime.greenwich(ZonedDateTime.of(LocalDate.of(2001, Month.SEPTEMBER, 11), LocalTime.of(8, 14, 0), ZoneOffset.UTC)));
        assertEquals(2.47403226856777, SiderealTime.greenwich(ZonedDateTime.of(LocalDate.of(2004, Month.SEPTEMBER, 23), LocalTime.of(11, 0, 0), ZoneOffset.UTC)));
    }
}

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

    }

    @Test
    void greenwichWorksOnTrivialCase() {
        assertEquals(4.894961211636988, SiderealTime.greenwich(ZonedDateTime.of(LocalDate.of(2000, Month.JANUARY, 1),
                LocalTime.of(12, 0),
                ZoneOffset.UTC)));
    }

}

package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.math.Angle;
import org.junit.jupiter.api.Test;

import java.time.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Bastien Faivre
 * 02/03/2020
 */
public class MyEquatorialToHorizontalConversionTest {

    @Test
    void conversionWorksOnTrivialCases() {
        EquatorialCoordinates coordinates1 = EquatorialCoordinates.of(0,0);
        EquatorialToHorizontalConversion system = new EquatorialToHorizontalConversion(ZonedDateTime.of(LocalDate.of(2000, Month.JANUARY, 1),
                LocalTime.of(12, 0),
                ZoneOffset.UTC),
                GeographicCoordinates.ofDeg(0,0));
        HorizontalCoordinates coordinates2 = system.apply(coordinates1);
        assertEquals(90, coordinates2.azDeg());
        assertEquals(10.46,coordinates2.altDeg(), 10e-3);
    }

    @Test
    void conversionWorks() {
        EquatorialCoordinates coordinates1 = EquatorialCoordinates.of(1,1);
        EquatorialToHorizontalConversion system = new EquatorialToHorizontalConversion(ZonedDateTime.of(LocalDate.of(2007, Month.MARCH, 13),
                LocalTime.of(5, 9,50),
                ZoneOffset.UTC),
                GeographicCoordinates.ofDeg(2,1));
        HorizontalCoordinates coordinates2 = system.apply(coordinates1);
        //assertEquals(0, coordinates2.lon());
        //assertEquals(0, coordinates2.lat());
    }

    @Test
    void conversionWorksOnBook() {
        EquatorialCoordinates coordinates1 = EquatorialCoordinates.of(0,Angle.ofDMS(23,13,10));
        EquatorialToHorizontalConversion system = new EquatorialToHorizontalConversion(ZonedDateTime.of(LocalDate.of(2000, Month.JANUARY, 1),
                LocalTime.of(12, 0),
                ZoneOffset.UTC),
                GeographicCoordinates.ofDeg(0,52));
        HorizontalCoordinates coordinates2 = system.apply(coordinates1);
        //assertEquals(0, coordinates2.azDeg());
    }

}

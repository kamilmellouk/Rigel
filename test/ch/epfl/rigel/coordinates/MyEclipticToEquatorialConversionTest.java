package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.astronomy.Epoch;
import ch.epfl.rigel.math.Angle;
import org.junit.jupiter.api.Test;

import java.time.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Bastien Faivre
 * 02/03/2020
 */
public class MyEclipticToEquatorialConversionTest {

    @Test
    void conversionWorksOnTrivialCases() {
        EclipticCoordinates coordinates1 = EclipticCoordinates.of(0,0);
        EclipticToEquatorialConversion system = new EclipticToEquatorialConversion(ZonedDateTime.now());
        EquatorialCoordinates coordinates2 = system.apply(coordinates1);
        assertEquals(0, coordinates2.ra());
        assertEquals(0, coordinates2.dec());
    }

    @Test
    void conversionWorksOnBookExample() {
        EclipticCoordinates coordinates1 = EclipticCoordinates.of(Angle.ofDMS(139,41,10),Angle.ofDMS(4,52,31));
        EclipticToEquatorialConversion system = new EclipticToEquatorialConversion(ZonedDateTime.of(LocalDate.of(2009, Month.JULY,6), LocalTime.of(0,0), ZoneOffset.UTC));
        EquatorialCoordinates coordinates2 = system.apply(coordinates1);
        assertEquals(Angle.ofHr(9.581478), coordinates2.ra(), 10e-8);
        assertEquals(Angle.ofDMS(19,32,6.01), coordinates2.dec(), 10e-8);
    }

    @Test
    void conversionWorks() {
        EclipticCoordinates coordinates1 = EclipticCoordinates.of(Angle.ofHr(1),0);
        EclipticToEquatorialConversion system = new EclipticToEquatorialConversion(ZonedDateTime.now());
        EquatorialCoordinates coordinates2 = system.apply(coordinates1);
        assertEquals(Angle.ofDMS(13,48,42), coordinates2.ra(), 10e-6);
        assertEquals(Angle.ofDMS(5,54,33), coordinates2.dec(), 10e-5);
    }

}

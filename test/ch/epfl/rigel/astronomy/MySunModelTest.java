package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EclipticToEquatorialConversion;
import ch.epfl.rigel.math.Angle;
import org.junit.jupiter.api.Test;

import java.time.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Bastien Faivre (310929)
 * @author Kamil Mellouk (312327)
 */

class MySunModelTest {

    @Test
    void atWorks() {
        EclipticToEquatorialConversion system = new EclipticToEquatorialConversion(ZonedDateTime.of(LocalDate.of(2003, Month.JULY, 27),
                LocalTime.of(0, 0),
                ZoneOffset.UTC));
        Sun sun = SunModel.SUN.at(Epoch.J2010.daysUntil(ZonedDateTime.of(LocalDate.of(2003, Month.JULY, 27),
                LocalTime.of(0, 0),
                ZoneOffset.UTC)),
                system);
        assertEquals(123.58060053153336, sun.eclipticPos().lonDeg());
        assertEquals(0, sun.eclipticPos().latDeg());
        assertEquals(system.apply(sun.eclipticPos()).raDeg(), sun.equatorialPos().raDeg());
        assertEquals(system.apply(sun.eclipticPos()).decDeg(), sun.equatorialPos().decDeg());
    }

}
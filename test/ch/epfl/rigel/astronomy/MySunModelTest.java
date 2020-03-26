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
        assertEquals(123.58060053153356, sun.eclipticPos().lonDeg());
        assertEquals(0, sun.eclipticPos().latDeg());
        assertEquals(system.apply(sun.eclipticPos()).raDeg(), sun.equatorialPos().raDeg());
        assertEquals(system.apply(sun.eclipticPos()).decDeg(), sun.equatorialPos().decDeg());
    }

    ZonedDateTime zdt = ZonedDateTime.of(LocalDateTime.of(LocalDate.of(2003, 7, 27), LocalTime.MIDNIGHT), ZoneOffset.UTC);
    EclipticToEquatorialConversion eclToEqu = new EclipticToEquatorialConversion(zdt);
    ZonedDateTime zdt2 = ZonedDateTime.of(LocalDate.of(2010,  Month.FEBRUARY, 27),LocalTime.of(0,0), ZoneOffset.UTC);
    EclipticToEquatorialConversion eclToEqu2 = new EclipticToEquatorialConversion(zdt2);

    @Test
    void testFramePad() {
        assertEquals(5.9325494700300885, SunModel.SUN.at(27 + 31, new EclipticToEquatorialConversion(ZonedDateTime.of(LocalDate.of(2010,  Month.FEBRUARY, 27),LocalTime.of(0,0), ZoneOffset.UTC))).equatorialPos().ra());
        assertEquals(8.3926828082978, SunModel.SUN.at(-2349, new EclipticToEquatorialConversion(ZonedDateTime.of(LocalDate.of(2003, Month.JULY, 27), LocalTime.of(0, 0, 0, 0), ZoneOffset.UTC))).equatorialPos().raHr(), 1e-14);
        assertEquals(19.35288373097352, SunModel.SUN.at(-2349, new EclipticToEquatorialConversion(ZonedDateTime.of(LocalDate.of(2003, Month.JULY, 27), LocalTime.of(0, 0, 0, 0), ZoneOffset.UTC))).equatorialPos().decDeg());

    }

}
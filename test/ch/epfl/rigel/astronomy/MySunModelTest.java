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

    ZonedDateTime zdt = ZonedDateTime.of(LocalDateTime.of(LocalDate.of(2003, 7, 27), LocalTime.MIDNIGHT), ZoneOffset.UTC);
    EclipticToEquatorialConversion eclToEqu = new EclipticToEquatorialConversion(zdt);
    ZonedDateTime zdt2 = ZonedDateTime.of(LocalDate.of(2010,  Month.FEBRUARY, 27),LocalTime.of(0,0), ZoneOffset.UTC);
    EclipticToEquatorialConversion eclToEqu2 = new EclipticToEquatorialConversion(zdt2);

    @Test
    void test() {
        assertEquals(5.9325494700300885, SunModel.SUN.at(Epoch.J2010.daysUntil(zdt2), eclToEqu2).equatorialPos().ra(),1e-11);
    }

    @Test
    void test2() {
        assertEquals(8.392682808297806, SunModel.SUN.at(Epoch.J2010.daysUntil(zdt), eclToEqu).equatorialPos().raHr(), 1e-11);
    }

    @Test
    void test3() {
        assertEquals(19.35288373097352, SunModel.SUN.at(Epoch.J2010.daysUntil(zdt), eclToEqu).equatorialPos().decDeg(), 1e-11);
    }

    @Test
    void test4() {
        assertEquals(Angle.ofHr(8) + Angle.ofDMS(0, 23, 34), SunModel.SUN.at(Epoch.J2010.daysUntil(zdt), eclToEqu).equatorialPos().ra(), 1e-11);
    }

    @Test
    void test5() {
        assertEquals(Angle.ofDMS(19, 21, 10), SunModel.SUN.at(Epoch.J2010.daysUntil(zdt), eclToEqu).equatorialPos().dec(), 1e-11);
    }

}
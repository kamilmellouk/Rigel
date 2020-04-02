package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EclipticCoordinates;
import ch.epfl.rigel.coordinates.EclipticToEquatorialConversion;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import ch.epfl.rigel.math.Angle;
import org.junit.jupiter.api.Test;

import java.time.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Bastien Faivre
 * 28/03/2020
 */
class MyMoonModelTest {

    @Test
    void atWorks() {
        ZonedDateTime date = ZonedDateTime.of(LocalDateTime.of(LocalDate.of(2003, Month.SEPTEMBER, 1), LocalTime.of(0, 0)), ZoneOffset.UTC);
        double D = Epoch.J2010.daysUntil(date);
        Moon moon = MoonModel.MOON.at(D, new EclipticToEquatorialConversion(date));
    }

    @Test
    void framepadTestAngularSize() {
        assertEquals(0.009225908666849136, MoonModel.MOON.at(Epoch.J2010.daysUntil(ZonedDateTime.of(LocalDate.of(1979, 9, 1),LocalTime.of(0, 0),
                ZoneOffset.UTC)), new EclipticToEquatorialConversion(ZonedDateTime.of(
                LocalDate.of(1979, 9, 1),LocalTime.of(0, 0),ZoneOffset.UTC))).
                angularSize());
    }

    @Test
    void framepadTestInfo() {
        assertEquals("Lune (22.5%)", MoonModel.MOON.at(Epoch.J2010.daysUntil(ZonedDateTime.of(LocalDate.of(2003, 9, 1),LocalTime.of(0, 0),
                ZoneOffset.UTC)), new EclipticToEquatorialConversion(ZonedDateTime.of( LocalDate.of(2003, 9, 1),
                LocalTime.of(0, 0),ZoneOffset.UTC))).
                info() );
    }

    @Test
    void framepadTestDec() {
        assertEquals(-0.20114171346019355, MoonModel.MOON.at(-2313,
                new EclipticToEquatorialConversion(ZonedDateTime.of(LocalDate.of(2003,  Month.SEPTEMBER, 1),LocalTime.of(0,0), ZoneOffset.UTC))).equatorialPos().dec(),
                1e-13);
    }

    @Test
    void framepadTestRaHr() {
        assertEquals(14.211456457836, MoonModel.MOON.at(-2313,
                      new EclipticToEquatorialConversion(ZonedDateTime.of(LocalDate.of(2003,  Month.SEPTEMBER, 1),LocalTime.of(0,0), ZoneOffset.UTC))).equatorialPos().raHr(),
                1e-12);
    }

    @Test
    void atTestRa1() {
        double raHr = MoonModel.MOON.at(-2313, new

                EclipticToEquatorialConversion(ZonedDateTime.of(LocalDate.of(2003, Month.SEPTEMBER, 1), LocalTime.

                of(0, 0), ZoneOffset.UTC))).

                equatorialPos().

                raHr();
        assertEquals(14.211456457836277, raHr, 1e-12);
    }

    @Test
    void atTestDec1() {
        double dec = MoonModel.MOON.at(-2313, new
                EclipticToEquatorialConversion(ZonedDateTime.of(LocalDate.of(2003, Month.SEPTEMBER, 1), LocalTime.

                of(0, 0), ZoneOffset.UTC))).

                equatorialPos().

                dec();
        assertEquals(-0.20114171346019355, dec, 1e-12);
    }

    @Test
    void atTestDec2() {
        double dec = MoonModel.MOON.at(-2313, new
                EclipticToEquatorialConversion(ZonedDateTime.of(LocalDate.of(2003, Month.SEPTEMBER, 1), LocalTime.

                of(0, 0), ZoneOffset.UTC))).

                equatorialPos().

                dec();
        assertEquals(-0.20114171346019355, dec, 1e-13);
    }

    @Test
    void atTestAngularSize() {

        double angularsize = MoonModel.MOON.at(Epoch.J2010.daysUntil(ZonedDateTime.of(LocalDate.of(1979, 9, 1), LocalTime.of(0, 0),
                ZoneOffset.UTC)),
                new EclipticToEquatorialConversion(ZonedDateTime.of(
                        LocalDate.of(1979, 9, 1), LocalTime.

                                of(0, 0), ZoneOffset.UTC))).

                angularSize();

        assertEquals(0.009225908666849136, angularsize);
    }

    @Test
    void infoTest() {
        String info = MoonModel.MOON.at(Epoch.J2010.daysUntil(ZonedDateTime.of(LocalDate.of(2003, 9, 1), LocalTime.of(0, 0),
                ZoneOffset.UTC)),
                new EclipticToEquatorialConversion(ZonedDateTime.of(LocalDate.of(2003, 9, 1),
                        LocalTime.of(0, 0), ZoneOffset.UTC))).info();
        assertEquals("Lune (22.5%)", info);
        System.out.println(Angle.toHr(Angle.ofDeg(214.862515)));
        System.out.println(Angle.ofDeg(1.716257));
    }

    @Test
    void BookatTestRa1() {
        double raHr = MoonModel.MOON.at(-2313, new

                EclipticToEquatorialConversion(ZonedDateTime.of(LocalDate.of(2003, Month.SEPTEMBER, 1), LocalTime.

                of(0, 0), ZoneOffset.UTC))).

                equatorialPos().

                raHr();

        double ra = Angle.ofDeg(214.862515);
        double dec = Angle.ofDeg(1.716257);
        EclipticCoordinates ec = EclipticCoordinates.of(ra, dec);
        EquatorialCoordinates eq = new EclipticToEquatorialConversion(ZonedDateTime.of(LocalDate.of(2003, Month.SEPTEMBER, 1), LocalTime.

                of(0, 0), ZoneOffset.UTC)).apply(ec);

        assertEquals(eq.raHr(), raHr, 1e-6);
    }

    @Test
    void BookatTestDec1() {
        double dec = MoonModel.MOON.at(-2313, new
                EclipticToEquatorialConversion(ZonedDateTime.of(LocalDate.of(2003, Month.SEPTEMBER, 1), LocalTime.

                of(0, 0), ZoneOffset.UTC))).

                equatorialPos().

                dec();

        double ra = Angle.ofDeg(214.862515);
        double dec2 = Angle.ofDeg(1.716257);
        EclipticCoordinates ec = EclipticCoordinates.of(ra, dec2);
        EquatorialCoordinates eq = new EclipticToEquatorialConversion(ZonedDateTime.of(LocalDate.of(2003, Month.SEPTEMBER, 1), LocalTime.

                of(0, 0), ZoneOffset.UTC)).apply(ec);
        assertEquals(eq.dec(), dec, 1e-6);
    }

}
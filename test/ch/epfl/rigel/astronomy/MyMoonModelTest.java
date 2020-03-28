package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EclipticToEquatorialConversion;
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

    /*@Test
    void framepadTestAngularSize() {
        assertEquals(0.009225908666849136, MoonModel.MOON.at(Epoch.J2010.daysUntil(ZonedDateTime.of(LocalDate.of(1979, 9, 1),LocalTime.of(0, 0),
                ZoneOffset.UTC)), new EclipticToEquatorialConversion(ZonedDateTime.of(
                LocalDate.of(1979, 9, 1),LocalTime.of(0, 0),ZoneOffset.UTC))).
                angularSize());
    }*/

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
        assertEquals(14.211456457836277, MoonModel.MOON.at(-2313,
                      new EclipticToEquatorialConversion(ZonedDateTime.of(LocalDate.of(2003,  Month.SEPTEMBER, 1),LocalTime.of(0,0), ZoneOffset.UTC))).equatorialPos().raHr(),
                      1e-11);
    }
}
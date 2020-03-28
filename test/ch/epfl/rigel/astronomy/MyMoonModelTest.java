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

}
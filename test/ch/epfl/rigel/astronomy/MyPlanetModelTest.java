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

class MyPlanetModelTest {

    @Test
    void atTest() {
        Planet jupiter = PlanetModel.JUPITER.at(-2231.0,
                new EclipticToEquatorialConversion(ZonedDateTime.of(LocalDate.of(2003, Month.NOVEMBER, 22),
                        LocalTime.of(0, 0, 0, 0),
                        ZoneOffset.UTC)));
        //assertEquals(11.18715493470968, jupiter.equatorialPos().raHr(), 10e-15);

        //assertEquals(35.11141185362771, Angle.toDeg(jupiter.angularSize()) * 3600);

        //assertEquals(-1.9885659217834473, jupiter.magnitude());

        Planet mercury = PlanetModel.MERCURY.at(-2231.0,
                new EclipticToEquatorialConversion(ZonedDateTime.of(LocalDate.of(2003, Month.NOVEMBER, 22),
                        LocalTime.of(0, 0, 0, 0),
                        ZoneOffset.UTC)));

        //assertEquals(16.8200745658971, mercury.equatorialPos().raHr(), 10e-14);
        //assertEquals(-24.500872462861, mercury.equatorialPos().decDeg(), 10e-13);

    }
}
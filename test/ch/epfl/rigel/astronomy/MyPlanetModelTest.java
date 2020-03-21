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
    void atWorksForJupiter() {
        EclipticToEquatorialConversion system = new EclipticToEquatorialConversion(ZonedDateTime.of(LocalDate.of(2003, Month.NOVEMBER, 22),
                LocalTime.of(0, 0),
                ZoneOffset.UTC));

        Planet jupiter = PlanetModel.JUPITER.at(Epoch.J2010.daysUntil(ZonedDateTime.of(LocalDate.of(2003, Month.NOVEMBER, 22),
                LocalTime.of(0, 0),
                ZoneOffset.UTC)),
                system);
        System.out.println(jupiter.equatorialPos().ra());
        System.out.println(jupiter.equatorialPos().dec());
        assertEquals(2.928790313121228, jupiter.equatorialPos().ra(), 1e-15);
        assertEquals(0.1109442189408443, jupiter.equatorialPos().dec(), 1e-15);
        assertEquals(4.299418, jupiter.angularSize(), 1e-6);
        assertEquals(-2.0255983, jupiter.magnitude(), 1e-7);
    }

    @Test
    void atWorksForMercury() {
        EclipticToEquatorialConversion system = new EclipticToEquatorialConversion(ZonedDateTime.of(LocalDate.of(2003, Month.NOVEMBER, 22),
                LocalTime.of(0, 0),
                ZoneOffset.UTC));

        Planet mercury = PlanetModel.MERCURY.at(Epoch.J2010.daysUntil(ZonedDateTime.of(LocalDate.of(2003, Month.NOVEMBER, 22),
                LocalTime.of(0, 0),
                ZoneOffset.UTC)),
                system);
        System.out.println(Angle.toDeg(mercury.equatorialPos().ra()));
        System.out.println(Angle.toDeg(mercury.equatorialPos().dec()));
        assertEquals(4.4034852240879285, mercury.equatorialPos().ra(), 1e-15);
        assertEquals(-0.4276208940881414, mercury.equatorialPos().dec(), 1e-15);
        assertEquals(4.2844853, mercury.angularSize(), 1e-6);
        assertEquals(-1.0694658, mercury.magnitude(), 1e-7);
    }

}
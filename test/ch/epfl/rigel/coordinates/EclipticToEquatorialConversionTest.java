package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.astronomy.Epoch;
import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Bastien Faivre
 * 02/03/2020
 */
public class EclipticToEquatorialConversionTest {

    @Test
    void conversionWorksOnTrivialCases() {
        EclipticCoordinates coordinates1 = EclipticCoordinates.of(0,0);
        EclipticToEquatorialConversion system = new EclipticToEquatorialConversion(ZonedDateTime.now());
        EquatorialCoordinates coordinates2 = system.apply(coordinates1);
        assertEquals(0, coordinates2.ra());
        assertEquals(0, coordinates2.dec());
    }

}

package ch.epfl.rigel.coordinates;

import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Bastien Faivre
 * 02/03/2020
 */
public class MyEquatorialToHorizontalConversionTest {

    @Test
    void conversionWorksOnTrivialCases() {
        EquatorialToHorizontalConversion system = new EquatorialToHorizontalConversion(ZonedDateTime.now(), GeographicCoordinates.ofDeg(0, 0));
        HorizontalCoordinates coordinates2 = system.apply(EquatorialCoordinates.of(0,0));
        assertEquals(0, coordinates2.az());
        assertEquals(0, coordinates2.alt());
    }

}

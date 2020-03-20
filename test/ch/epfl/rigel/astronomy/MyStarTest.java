package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Bastien Faivre (310929)
 * @author Kamil Mellouk (312327)
 */

public class MyStarTest {

    @Test
    void constructorThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> new Star(-1,
                "name",
                EquatorialCoordinates.of(0,0),
                1,
                0));
        assertThrows(IllegalArgumentException.class, () -> new Star(1,
                "name",
                EquatorialCoordinates.of(0,0),
                1,
                -1));
        assertThrows(NullPointerException.class, () -> new Star(1,
                null,
                EquatorialCoordinates.of(0,0),
                1,
                0));
        assertThrows(NullPointerException.class, () -> new Star(1,
                "name",
                null,
                1,
                0));
    }

    @Test
    void hipparcosIdWorks() {
        assertEquals(1, new Star(1,
                "name",
                EquatorialCoordinates.of(0,0),
                1,
                0)
                .hipparcosId());
    }

    @Test
    void colorTemperatureWorks() {
        assertEquals(10125 ,new Star(1,
                "name",
                EquatorialCoordinates.of(0,0),
                1,
                0)
                .colorTemperature());
        assertEquals(3169 ,new Star(1,
                "name",
                EquatorialCoordinates.of(0,0),
                1,
                2)
                .colorTemperature());
    }

}

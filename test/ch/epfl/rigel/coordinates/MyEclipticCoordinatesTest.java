package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.math.Angle;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Bastien Faivre (310929)
 * @author Kamil Mellouk (312327)
 */

public class MyEclipticCoordinatesTest {

    @Test
    void ofTest() {
        assertThrows(IllegalArgumentException.class, () -> {
            EclipticCoordinates.of(-1, 0);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            EclipticCoordinates.of(0, Math.PI);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            EclipticCoordinates.of(Angle.TAU, Math.PI / 2);
        });
    }

    @Test
    void returnLonTest() {
        assertEquals(Math.PI / 4, EclipticCoordinates.of(Math.PI / 4, 0).lon());
        assertEquals(Math.PI / 2, EclipticCoordinates.of(Math.PI / 2, 0).lon());
    }

    @Test
    void returnLonDegTest() {
        assertEquals(0, EclipticCoordinates.of(0, 0).lonDeg());
        assertEquals(45, EclipticCoordinates.of(Math.PI / 4, 0).lonDeg());
        assertEquals(90, EclipticCoordinates.of(Math.PI / 2, 0).lonDeg());
    }

    @Test
    void returnLatTest() {
        assertEquals(Math.PI / 4, EclipticCoordinates.of(0, Math.PI / 4).lat());
        assertEquals(Math.PI / 2, EclipticCoordinates.of(0, Math.PI / 2).lat());
    }

    @Test
    void returnLatDegTest() {
        assertEquals(0, EclipticCoordinates.of(1, 0).latDeg());
        assertEquals(45, EclipticCoordinates.of(0, Math.PI / 4).latDeg());
        assertEquals(90, EclipticCoordinates.of(0, Math.PI / 2).latDeg());
    }

    @Test
    void toStringTest() {
        assertEquals("(λ=0.0000°, β=0.0000°)", EclipticCoordinates.of(0, 0).toString());
        assertEquals("(λ=90.0000°, β=22.5000°)", EclipticCoordinates.of(Math.PI / 2, Math.PI / 8).toString());
        assertEquals("(λ=60.0000°, β=36.0000°)", EclipticCoordinates.of(Math.PI / 3, Math.PI / 5).toString());
        assertEquals("(λ=45.0000°, β=24.7575°)", EclipticCoordinates.of(Math.PI / 4, 0.4321).toString());
    }

}

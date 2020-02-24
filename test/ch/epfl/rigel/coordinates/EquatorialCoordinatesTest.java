package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.math.Angle;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Bastien Faivre (310929)
 * @author Kamil Mellouk (312327)
 */

public class EquatorialCoordinatesTest {

    @Test
    void ofTest() {
        assertThrows(IllegalArgumentException.class, () -> {
            EquatorialCoordinates.of(-1,0);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            EquatorialCoordinates.of(0,Math.PI);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            EquatorialCoordinates.of(Angle.TAU,Math.PI/2);
        });
    }

    @Test
    void returnRaTest() {
        assertEquals(1, EquatorialCoordinates.of(1,0).ra());
        assertEquals(2, EquatorialCoordinates.of(2,0).ra());
    }

    @Test
    void returnRaDeg() {
        assertEquals(0, EquatorialCoordinates.of(0,0).raDeg());
        assertEquals(90, EquatorialCoordinates.of(Math.PI/2,0).raDeg());
    }

    @Test
    void returnRaHr() {
        assertEquals(1, EquatorialCoordinates.of(Angle.TAU/24,0).raHr(), 1e-4);
        assertEquals(2, EquatorialCoordinates.of(2*Angle.TAU/24,0).raHr(), 1e-4);
    }

    @Test
    void returnDec() {
        assertEquals(1, EquatorialCoordinates.of(0,1).dec());
        assertEquals(0.5, EquatorialCoordinates.of(0,0.5).dec());
    }

    @Test
    void returnDecDeg() {
        assertEquals(0, EquatorialCoordinates.of(0,0).decDeg());
        assertEquals(90, EquatorialCoordinates.of(0,Math.PI/2).decDeg());
    }

    @Test
    void toStringTest() {
        assertEquals("(ra=0.0000h, dec=0.0000°)", EquatorialCoordinates.of(0,0).toString());
        assertEquals("(ra=0.5000h, dec=22.5000°)", EquatorialCoordinates.of(Math.PI/24,Math.PI/8).toString());
        assertEquals("(ra=1.0000h, dec=36.0000°)", EquatorialCoordinates.of(2*Math.PI/24,Math.PI/5).toString());
        assertEquals("(ra=6.0000h, dec=24.7575°)", EquatorialCoordinates.of(Math.PI/2,0.4321).toString());
    }

}

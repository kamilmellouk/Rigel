package ch.epfl.rigel.coordinates;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Bastien Faivre (310929)
 * @author Kamil Mellouk (312327)
 */

/**
 * make the constructor public in the class
 */

public class GeographicCoordinatesTest {

    @Test
    void returnLonTest() {
        assertEquals(Math.PI/4, GeographicCoordinates.ofDeg(45,0).lon());
        assertEquals(Math.PI/2, GeographicCoordinates.ofDeg(90,0).lon());
    }

    @Test
    void returnLonDegTest() {
        assertEquals(0, GeographicCoordinates.ofDeg(0,0).lonDeg());
        assertEquals(45, GeographicCoordinates.ofDeg(45,0).lonDeg());
        assertEquals(90, GeographicCoordinates.ofDeg(90,0).lonDeg());
    }

    @Test
    void returnLatTest() {
        assertEquals(Math.PI/4, GeographicCoordinates.ofDeg(0,45).lat());
        assertEquals(Math.PI/2, GeographicCoordinates.ofDeg(0,90).lat());
    }

    @Test
    void returnLatDegTest() {
        assertEquals(0, GeographicCoordinates.ofDeg(1,0).latDeg());
        assertEquals(45, GeographicCoordinates.ofDeg(0,45).latDeg());
        assertEquals(90, GeographicCoordinates.ofDeg(0,90).latDeg());
    }

    @Test
    void isValidLonDegTest() {
        // bounds
        assertTrue(GeographicCoordinates.isValidLonDeg(-180));
        assertFalse(GeographicCoordinates.isValidLonDeg(180));
        // normal cases
        assertTrue(GeographicCoordinates.isValidLonDeg(-90));
        assertTrue(GeographicCoordinates.isValidLonDeg(0));
        assertTrue(GeographicCoordinates.isValidLonDeg(90));
        assertFalse(GeographicCoordinates.isValidLonDeg(-181));
        assertFalse(GeographicCoordinates.isValidLonDeg(181));
    }

    @Test
    void isValidLatDegTest() {
        // bounds
        assertTrue(GeographicCoordinates.isValidLatDeg(-90));
        assertTrue(GeographicCoordinates.isValidLatDeg(90));
        // normal cases
        assertTrue(GeographicCoordinates.isValidLatDeg(-45));
        assertTrue(GeographicCoordinates.isValidLatDeg(0));
        assertTrue(GeographicCoordinates.isValidLatDeg(45));
        assertFalse(GeographicCoordinates.isValidLatDeg(-91));
        assertFalse(GeographicCoordinates.isValidLatDeg(91));
    }

    @Test
    void ofDegConstructorTest() {
        assertEquals(0, GeographicCoordinates.ofDeg(0,0).lon());
        assertEquals(0, GeographicCoordinates.ofDeg(0,0).lonDeg());
        assertEquals(0, GeographicCoordinates.ofDeg(0,0).lat());
        assertEquals(0, GeographicCoordinates.ofDeg(0,0).latDeg());
        assertEquals(Math.PI/2, GeographicCoordinates.ofDeg(90,0).lon());
        assertEquals(90, GeographicCoordinates.ofDeg(90,0).lonDeg());
        assertEquals(Math.PI/2, GeographicCoordinates.ofDeg(0,90).lat());
        assertEquals(90, GeographicCoordinates.ofDeg(0,90).latDeg());
        // exception test
        assertThrows(IllegalArgumentException.class, () -> {
            GeographicCoordinates.ofDeg(-181,0);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            GeographicCoordinates.ofDeg(180,0);
        });
        assertThrows(IllegalArgumentException.class, () ->{
            GeographicCoordinates.ofDeg(0,-91);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            GeographicCoordinates.ofDeg(0,91);
        });
    }

    @Test
    void toStringTest() {
        assertEquals("(lon=0.0000°, lat=0.0000°)", GeographicCoordinates.ofDeg(0,0).toString());
        assertEquals("(lon=90.0000°, lat=22.5000°)", GeographicCoordinates.ofDeg(90,22.5).toString());
        assertEquals("(lon=60.0000°, lat=36.0000°)", GeographicCoordinates.ofDeg(60,36).toString());
        assertEquals("(lon=70.7316°, lat=24.7575°)", GeographicCoordinates.ofDeg(70.7316,24.7575).toString());
    }

}

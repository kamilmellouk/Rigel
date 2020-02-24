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
        assertEquals(1, new GeographicCoordinates(1,1).lon());
        assertEquals(2, new GeographicCoordinates(2,2).lon());
    }

    @Test
    void returnLonDegTest() {
        assertEquals(0, new GeographicCoordinates(0,0).lonDeg());
        assertEquals(90, new GeographicCoordinates(Math.PI/2,0).lonDeg());
    }

    @Test
    void returnLatTest() {
        assertEquals(1, new GeographicCoordinates(1,1).lat());
        assertEquals(2, new GeographicCoordinates(2,2).lat());
    }

    @Test
    void returnLatDegTest() {
        assertEquals(0, new GeographicCoordinates(0,0).latDeg());
        assertEquals(90, new GeographicCoordinates(0,Math.PI/2).latDeg());
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
    void returnLonTest2() {
        assertEquals(1, new GeographicCoordinates(1,0).lon());
        assertEquals(2, new GeographicCoordinates(2,0).lon());
    }

    @Test
    void returnLonDegTest2() {
        assertEquals(0, new GeographicCoordinates(0,1).lonDeg());
        assertEquals(45, new GeographicCoordinates(Math.PI/4,0).lonDeg());
        assertEquals(90, new GeographicCoordinates(Math.PI/2,0).lonDeg());
    }

    @Test
    void returnLatTest2() {
        assertEquals(1, new GeographicCoordinates(0,1).lat());
        assertEquals(2, new GeographicCoordinates(0,2).lat());
    }

    @Test
    void returnLatDegTest2() {
        assertEquals(0, new GeographicCoordinates(1,0).latDeg());
        assertEquals(45, new GeographicCoordinates(0,Math.PI/4).latDeg());
        assertEquals(90, new GeographicCoordinates(0,Math.PI/2).latDeg());
    }

    @Test
    void toStringTest() {
        assertEquals("(lon=0.0000°, lat=0.0000°)", new GeographicCoordinates(0,0).toString());
        assertEquals("(lon=90.0000°, lat=22.5000°)", new GeographicCoordinates(Math.PI/2,Math.PI/8).toString());
        assertEquals("(lon=60.0000°, lat=36.0000°)", new GeographicCoordinates(Math.PI/3,Math.PI/5).toString());
        assertEquals("(lon=70.7316°, lat=24.7575°)", new GeographicCoordinates(1.2345,0.4321).toString());
    }

}

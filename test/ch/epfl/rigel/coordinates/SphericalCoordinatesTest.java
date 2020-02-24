package ch.epfl.rigel.coordinates;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author Bastien Faivre
 * 24/02/2020
 */
public class SphericalCoordinatesTest {

    @Test
    void returnLonTest() {
        assertEquals(1, new SphericalCoordinates(1,1).lon());
        assertEquals(2, new SphericalCoordinates(2,2).lon());
    }

    @Test
    void returnLonDegTest() {
        assertEquals(0, new SphericalCoordinates(0,0).lonDeg());
        assertEquals(90, new SphericalCoordinates(Math.PI/2,0).lonDeg());
    }

    @Test
    void returnLatTest() {
        assertEquals(1, new SphericalCoordinates(1,1).lat());
        assertEquals(2, new SphericalCoordinates(2,2).lat());
    }

    @Test
    void returnLatDegTest() {
        assertEquals(0, new SphericalCoordinates(0,0).latDeg());
        assertEquals(90, new SphericalCoordinates(0,Math.PI/2).latDeg());
    }

    @Test
    void throwExceptionWithEquals() {
        assertThrows(UnsupportedOperationException.class, () -> {
            new SphericalCoordinates(0,0).equals(new Object());
        });
    }

    @Test
    void throwExceptionWithHashCode() {
        assertThrows(UnsupportedOperationException.class, () -> {
            new SphericalCoordinates(0,0).hashCode();
        });
    }

}

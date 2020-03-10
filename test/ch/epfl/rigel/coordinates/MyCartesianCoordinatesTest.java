package ch.epfl.rigel.coordinates;

import ch.epfl.test.TestRandomizer;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.PI;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MyCartesianCoordinatesTest {
    @Test
    void xWorks() {
        assertEquals(3, CartesianCoordinates.of(3, 5).x());
        assertEquals(-4.66, CartesianCoordinates.of(-4.66, 5).x());
        assertEquals(1.2345678, CartesianCoordinates.of(1.2345678, 0).x());
    }

    @Test
    void yWorks() {
        assertEquals(0, CartesianCoordinates.of(3, 0).y());
        assertEquals(2.3, CartesianCoordinates.of(-4.66, 2.3).y());
        assertEquals(9.87654321, CartesianCoordinates.of(0, 9.87654321).y());

    }

    @Test
    void toStringWorks() {
        assertEquals("(x=1.0000, y=-1.0000)", CartesianCoordinates.of(1, -1).toString());
        assertEquals("(x=1.0001, y=2.0002)", CartesianCoordinates.of(1.00009, 2.00019).toString());
    }

    @Test
    void equalsThrowsUOE() {
        assertThrows(UnsupportedOperationException.class, () -> CartesianCoordinates.of(0, 0).equals(CartesianCoordinates.of(0, 0)));
    }

    @Test
    void hashCodeThrowsUOE() {
        assertThrows(UnsupportedOperationException.class, () -> CartesianCoordinates.of(0, 0).hashCode());
    }
}

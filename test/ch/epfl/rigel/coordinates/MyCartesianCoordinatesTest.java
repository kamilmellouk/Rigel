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
        assertEquals(3, CartesianCoordinates.of(3,5).x());
        assertEquals(-4.66, CartesianCoordinates.of(-4.66,5).x());
    }

    @Test
    void yWorks() {
        assertEquals(0, CartesianCoordinates.of(3,0).y());
        assertEquals(2.3, CartesianCoordinates.of(-4.66,2.3).y());

    }

    @Test
    void toStringWorks() {
        assertEquals("(x, y) = (1.0, -1.0)", CartesianCoordinates.of(1, -1).toString());
    }
}

package ch.epfl.rigel.coordinates;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Mohamed Kamil MELLOUK
 * 05.03.20
 */
public class MyStereographicProjectionTest {

    @Test
    void circleCenterForParallelReturnsInfinity() {
        assertEquals("(x=0.0, y=Infinity)",
                (new StereographicProjection(HorizontalCoordinates.of(0, 0)).circleCenterForParallel(HorizontalCoordinates.of(0, 0)).toString()));
    }

    @Test
    void circleCenterForParallelWorks() {
        // TODO implement test
        assertEquals(
                CartesianCoordinates.of(0, 1.4835636007422273).y(),
                (new StereographicProjection((HorizontalCoordinates.of(1, 1))).circleCenterForParallel(HorizontalCoordinates.of(1, 1)).y()));
    }

    @Test
    void circleRadiusForParallelReturnsInfinity() {
        assertEquals(1.0/0.0,
                (new StereographicProjection(HorizontalCoordinates.of(0,0)).circleRadiusForParallel(HorizontalCoordinates.of(0, 0))));
    }

    @Test
    void circleRadiusForParallelWorks() {
        // TODO implement test
    }

}

package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Bastien Faivre
 * 20/03/2020
 */
class MyAsterismTest {

    @Test
    void asterismThrowsException() {
        List<Star> stars = new ArrayList();
        assertThrows(IllegalArgumentException.class, () -> new Asterism(stars));
    }

    @Test
    void starsWorks() {
        List<Star> stars = new ArrayList();
        stars.add(new Star(1, "star", EquatorialCoordinates.of(0, 0), 1, 0));
        assertEquals(1, new Asterism(stars).stars().get(0).hipparcosId());
        assertEquals(0, new Asterism(stars).stars().get(0).angularSize());
        assertEquals(1, new Asterism(stars).stars().get(0).magnitude());
        assertEquals("star", new Asterism(stars).stars().get(0).name());
    }

}
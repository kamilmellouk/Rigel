package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Bastien Faivre (310929)
 * @author Kamil Mellouk (312327)
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

    @Test
    void starsIsUnmodifiable() {
        List<Star> stars = new ArrayList();
        stars.add(new Star(1, "star", EquatorialCoordinates.of(0, 0), 1, 0));
        Asterism asterism = new Asterism(stars);
        assertThrows(UnsupportedOperationException.class, () -> {asterism.stars().add(null);});
        assertThrows(UnsupportedOperationException.class, () -> {asterism.stars().add(0, null);});
    }

}
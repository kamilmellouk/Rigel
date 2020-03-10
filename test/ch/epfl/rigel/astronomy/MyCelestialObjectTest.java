package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.astronomy.CelestialObject;
import ch.epfl.rigel.astronomy.Planet;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Bastien Faivre (310929)
 * @author Kamil Mellouk (312327)
 */
public class MyCelestialObjectTest {

    @Test
    void celestialObjectThrowsIAE() {
       assertThrows(IllegalArgumentException.class,() -> new Planet("Objet Celeste", null, -20, 20));
       assertThrows(IllegalArgumentException.class,() -> new Planet("Objet Celeste", EquatorialCoordinates.of(0, 0), -3, 20));
    }

    @Test
    void celestialObjectThrowsNPE() {
        assertThrows(NullPointerException.class, () -> new Planet("test", null, 1,1));
        assertThrows(NullPointerException.class, () -> new Planet(null, EquatorialCoordinates.of(0,0), 1,1));
    }

    @Test
    void getterWork() {
        assertEquals("test", new Planet("test", EquatorialCoordinates.of(0, 0), 3, 20).name());
        assertEquals(0, new Planet("test", EquatorialCoordinates.of(0, 0), 3, 20).equatorialPos().ra());
        assertEquals(0, new Planet("test", EquatorialCoordinates.of(0, 0), 3, 20).equatorialPos().dec());
        assertEquals(3, new Planet("test", EquatorialCoordinates.of(0, 0), 3, 20).angularSize());
        assertEquals(20, new Planet("test", EquatorialCoordinates.of(0, 0), 3, 20).magnitude());
    }

    @Test
    void infoWorks() {
        assertEquals("test", new Planet("test", EquatorialCoordinates.of(0, 0), 3, 20).info());
    }

    @Test
    void toStringWorks() {
        assertEquals("test", new Planet("test", EquatorialCoordinates.of(0, 0), 3, 20).toString());

    }

}

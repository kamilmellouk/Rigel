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
public class MyPlanetTest {
    @Test
    void planetThrowsIAE() {
        assertThrows(IllegalArgumentException.class,() -> new Planet("Objet Celeste", null, 20, 20));
        assertThrows(IllegalArgumentException.class,() -> new Planet("Objet Celeste", EquatorialCoordinates.of(0, 0), -3, 20));
    }
}

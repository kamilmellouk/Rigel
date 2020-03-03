package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.astronomy.CelestialObject;
import ch.epfl.rigel.astronomy.Planet;
import ch.epfl.rigel.coordinates.EclipticCoordinates;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Bastien Faivre (310929)
 * @author Kamil Mellouk (312327)
 */
public class MySunTest {
    @Test
    void sunThrowsIAE() {
        assertThrows(IllegalArgumentException.class,() -> new Sun(null, null, 20, 20));
        assertThrows(IllegalArgumentException.class,() -> new Sun(EclipticCoordinates.of(0,0), null, 20, 20));
        assertThrows(IllegalArgumentException.class,() -> new Sun(null, EquatorialCoordinates.of(0,0), 20, 20));
    }
}

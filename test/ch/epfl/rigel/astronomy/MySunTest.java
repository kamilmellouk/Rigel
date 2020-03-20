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
    void moonThrowsIAE() {
        assertThrows(IllegalArgumentException.class, () -> new Sun(EclipticCoordinates.of(0, 0), EquatorialCoordinates.of(0, 0), -1, 1.5f));
        assertThrows(IllegalArgumentException.class, () -> new Sun(EclipticCoordinates.of(0, 0), EquatorialCoordinates.of(0, 0), -2, 2f));
    }

    @Test
    void moonThrowsNPE() {
        assertThrows(NullPointerException.class, () -> new Sun(EclipticCoordinates.of(0,0),null, 1, 0.5f));
        assertThrows(NullPointerException.class, () -> new Sun(null, EquatorialCoordinates.of(0, 0), 1, -4f));
        assertThrows(NullPointerException.class, () -> new Sun(null, EquatorialCoordinates.of(0, 0), 1, 0.5f));
    }

    @Test
    void meanAnomalyWorks() {
        assertEquals(2f, new Sun(EclipticCoordinates.of(0, 0), EquatorialCoordinates.of(0, 0), 2, 2f).meanAnomaly());
    }

    @Test
    void eclipticPosWorks() {
        assertEquals(1, new Sun(EclipticCoordinates.of(1, 1), EquatorialCoordinates.of(1, 1), 2, 2f).eclipticPos().lon());
        assertEquals(1, new Sun(EclipticCoordinates.of(1, 1), EquatorialCoordinates.of(1, 1), 2, 2f).eclipticPos().lat());
    }

    @Test
    void magnitudeWorks() {
        assertEquals(-26.7f, new Sun(EclipticCoordinates.of(0, 0), EquatorialCoordinates.of(0, 0), 2, 2f).magnitude());
    }

}

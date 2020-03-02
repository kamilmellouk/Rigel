package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Bastien Faivre (310929)
 * @author Kamil Mellouk (312327)
 */
public class MyMoonTest {

    @Test
    void infoWorksOnTrivialCases() {
        assertEquals("Lune (50.0%)", new Moon(EquatorialCoordinates.of(0, 0), 2, 1, 0.5f).info());
        assertEquals("Lune (75.0%)", new Moon(EquatorialCoordinates.of(0, 0), 2, 1, 0.75f).info());
    }
    @Test
    void infoWorks() {
        assertEquals("Lune (37.5%)", new Moon(EquatorialCoordinates.of(0, 0), 2, 1, 0.3752f).info());
        assertEquals("Lune (45.7%)", new Moon(EquatorialCoordinates.of(0, 0), 2, 1, 0.4567f).info());
    }

    @Test
    void infoWorksOnEdgeCases() {
        assertEquals("Lune (0.0%)", new Moon(EquatorialCoordinates.of(0, 0), 2, 1, 0f).info());
        assertEquals("Lune (100.0%)", new Moon(EquatorialCoordinates.of(0, 0), 2, 1, 1f).info());
    }

    @Test
    void moonThrowsRightException() {
       assertThrows(IllegalArgumentException.class,() -> new Moon(null, 2, 1, 0.5f));
       assertThrows(IllegalArgumentException.class,() -> new Moon(EquatorialCoordinates.of(0, 0), 2, 1, 2f));
       assertThrows(IllegalArgumentException.class,() -> new Moon(EquatorialCoordinates.of(0, 0), 2, 1, -4f));
       assertThrows(IllegalArgumentException.class,() -> new Moon(EquatorialCoordinates.of(0, 0), -3, 1, 0.5f));
    }
}

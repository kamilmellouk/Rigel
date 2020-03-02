package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Mohamed Kamil MELLOUK
 * 02.03.20
 */
public class MoonTest {
    @Test
    void infoWorks() {
        //assertEquals("Lune (37.5%)", new Moon(EquatorialCoordinates.of(0, 0), 2, 1, 0.3752f).info());
    }

    @Test
    void moonThrows() {
       // assertThrows(IllegalArgumentException, new Moon(EquatorialCoordinates.of(0, 0), 2, 1, 2f));
    }
}

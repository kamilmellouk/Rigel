package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import ch.epfl.test.TestRandomizer;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Bastien Faivre (310929)
 * @author Kamil Mellouk (312327)
 */

public class MyStarTest {

    @Test
    void constructorThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> new Star(-1,
                "name",
                EquatorialCoordinates.of(0,0),
                1,
                0));
        assertThrows(IllegalArgumentException.class, () -> new Star(1,
                "name",
                EquatorialCoordinates.of(0,0),
                1,
                -1));
        assertThrows(NullPointerException.class, () -> new Star(1,
                null,
                EquatorialCoordinates.of(0,0),
                1,
                0));
        assertThrows(NullPointerException.class, () -> new Star(1,
                "name",
                null,
                1,
                0));
    }

    @Test
    void hipparcosIdWorks() {
        assertEquals(1, new Star(1,
                "name",
                EquatorialCoordinates.of(0,0),
                1,
                0)
                .hipparcosId());
    }

    @Test
    void colorTemperatureWorks() {
        assertEquals(10125 ,new Star(1,
                "name",
                EquatorialCoordinates.of(0,0),
                1,
                0)
                .colorTemperature());
        assertEquals(3169 ,new Star(1,
                "name",
                EquatorialCoordinates.of(0,0),
                1,
                2)
                .colorTemperature());
    }

    @Test
    void starWorksOnKnowValues() {
        var rng = TestRandomizer.newRandom();
        for (int i = 0; i < TestRandomizer.RANDOM_ITERATIONS; i++) {
            int hipparcosId = rng.nextInt(0, Integer.MAX_VALUE);
            byte[] array = new byte[16];
            rng.nextBytes(array);
            String name = new String(array, StandardCharsets.UTF_8);
            EquatorialCoordinates pos = EquatorialCoordinates
                    .of(1.1123, -0.0982);
            float magnitude = (float) rng
                    .nextDouble(Float.MIN_EXPONENT,
                            Float.MAX_VALUE);
            float colorIndex = (float) rng
                    .nextDouble(-0.5, 5.5 + Double.MIN_VALUE);
            Star s = new Star(hipparcosId, name, pos, magnitude,
                    colorIndex);
            assertEquals(name, s.name());
            assertEquals(pos.ra(), s.equatorialPos().ra());
            assertEquals(pos.dec(), s.equatorialPos().dec());
            assertEquals(magnitude, s.magnitude());
            assertEquals(name, s.info());
            assertEquals(hipparcosId, s.hipparcosId());
        }
    }

    @Test
    void starFails() {
        var rng = TestRandomizer.newRandom();
        for (int i = 0; i < TestRandomizer.RANDOM_ITERATIONS; i++) {

            String name = "test";
            int hipparcosId = rng.nextInt(-Integer.MAX_VALUE, 0);
            float magnitude = 1;
            float colorIndex = (float) rng
                    .nextDouble(-Float.MAX_VALUE,
                            0.5 - Float.MIN_VALUE);
            if (rng.nextInt(0, 2) == 0) {
                colorIndex = (float) rng
                        .nextDouble(0.5 + Float.MIN_VALUE,
                                Float.MAX_VALUE);
            }
            float finalColorIndex = colorIndex;
            assertThrows(IllegalArgumentException.class,
                    () -> new Star(hipparcosId, name,
                            EquatorialCoordinates.of(0, 0),
                            magnitude, 1));
            assertThrows(IllegalArgumentException.class,
                    () -> new Star(hipparcosId, name,
                            EquatorialCoordinates.of(0, 0),
                            magnitude, finalColorIndex));
        }
    }


    @Test
    void colorTemperatureWorksOnKnownValues() {
        Star rigel = new Star(0, "Rigel",
                EquatorialCoordinates.of(0, 0), 1, -0.03f);
        assertEquals(10500, rigel.colorTemperature(), 15);
        Star betelgeuse = new Star(0, "Rigel",
                EquatorialCoordinates.of(0, 0), 1, 1.5f);
        assertEquals(3800, betelgeuse.colorTemperature(), 10);
    }

}

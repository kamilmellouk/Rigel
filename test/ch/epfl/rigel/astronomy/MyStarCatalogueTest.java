package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import ch.epfl.rigel.math.Angle;
import ch.epfl.test.TestRandomizer;
import org.junit.jupiter.api.Test;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Bastien Faivre (310929)
 * @author Kamil Mellouk (312327)
 */
public class MyStarCatalogueTest {

    @Test
    void AllMethodsWorksWithValidValues(){
        var rng = TestRandomizer.newRandom();
        for (int j = 0; j< TestRandomizer.RANDOM_ITERATIONS; j++) {
            List<Star> stars = new ArrayList<>();
            List<Star> stars1 = new ArrayList<>();
            List<Star> stars2 = new ArrayList<>();
            List<Integer> indices1 = new ArrayList<>();
            List<Integer> indices2 = new ArrayList<>();
            for (int i = 0; i < TestRandomizer.RANDOM_ITERATIONS; i++) {
                var hipparcosId = rng.nextInt(0, Integer.MAX_VALUE);
                var colorIndex = rng.nextDouble(-0.5, 5.5 + Double.MIN_VALUE);
                var ra = rng.nextDouble(0, Angle.TAU);
                var dec = rng.nextDouble(-Math.PI/2, Math.PI/2);
                var magnitude = rng.nextDouble();
                Star s = new Star(hipparcosId, "EZ", EquatorialCoordinates.of(ra, dec), (float)magnitude, (float)colorIndex);
                stars.add(s);
                if (i % 2 == 0) {
                    stars2.add(s);
                    indices2.add(i);
                }
                if (i % 3 == 0) {
                    stars1.add(s);
                    indices1.add(i);
                }
            }
            List<Asterism> asterisms = new ArrayList<>();
            Asterism a1 = new Asterism(stars1);
            asterisms.add(a1);
            Asterism a2 = new Asterism(stars2);
            asterisms.add(a2);
            StarCatalogue sc = new StarCatalogue(stars, asterisms);
            assertEquals(stars, sc.stars());
            assertEquals(Set.copyOf(asterisms), sc.asterisms());
            assertEquals(indices1, sc.asterismIndices(a1));
            assertEquals(indices2, sc.asterismIndices(a2));
        }
    }

    @Test
    void AllMethodsFailsWithInvalidStarsInAsterism(){
        var rng = TestRandomizer.newRandom();
        for (int j = 0; j< TestRandomizer.RANDOM_ITERATIONS; j++) {
            List<Star> stars = new ArrayList<>();
            List<Star> stars1 = new ArrayList<>();
            List<Star> stars2 = new ArrayList<>();
            List<Integer> indices1 = new ArrayList<>();
            List<Integer> indices2 = new ArrayList<>();
            for (int i = 0; i < TestRandomizer.RANDOM_ITERATIONS; i++) {
                var hipparcosId = rng.nextInt(0, Integer.MAX_VALUE);
                var colorIndex = rng.nextDouble(-0.5, 5.5 + Double.MIN_VALUE);
                var ra = rng.nextDouble(0, Angle.TAU);
                var dec = rng.nextDouble(-Math.PI/2, Math.PI/2);
                var magnitude = rng.nextDouble();
                Star s = new Star(hipparcosId, "EZ", EquatorialCoordinates.of(ra, dec), (float)magnitude, (float)colorIndex);
                stars1.add(s);
                indices1.add(i);
                if (i != 0) {
                    stars.add(s);
                }
                if (i % 4 == 0) {
                    stars2.add(s);
                    indices2.add(i);
                }
            }
            List<Asterism> asterisms = new ArrayList<>();
            Asterism a1 = new Asterism(stars1);
            asterisms.add(a1);
            Asterism a2 = new Asterism(stars2);
            asterisms.add(a2);
            assertThrows(IllegalArgumentException.class, () -> {
                new StarCatalogue(stars, asterisms);
            });
        }
    }


    @Test
    void AllMethodsFailsWithEmptyStars(){
        var rng = TestRandomizer.newRandom();
        for (int j = 0; j < TestRandomizer.RANDOM_ITERATIONS; j++) {
            List<Star> stars = Collections.emptyList();
            List<Star> stars1 = new ArrayList<>();
            List<Star> stars2 = new ArrayList<>();
            List<Integer> indices1 = new ArrayList<>();
            List<Integer> indices2 = new ArrayList<>();
            for (int i = 0; i < TestRandomizer.RANDOM_ITERATIONS; i++) {
                var hipparcosId = rng.nextInt(0, Integer.MAX_VALUE);
                var colorIndex = rng.nextDouble(-0.5, 5.5 + Double.MIN_VALUE);
                var ra = rng.nextDouble(0, Angle.TAU);
                var dec = rng.nextDouble(-Math.PI/2, Math.PI/2);
                var magnitude = rng.nextDouble();
                Star s = new Star(hipparcosId, "EZ", EquatorialCoordinates.of(ra, dec), (float)magnitude, (float)colorIndex);
                stars1.add(s);
                indices1.add(i);
                if (i % 4 == 0) {
                    stars2.add(s);
                    indices2.add(i);
                }
            }
            List<Asterism> asterisms = new ArrayList<>();
            Asterism a1 = new Asterism(stars1);
            asterisms.add(a1);
            Asterism a2 = new Asterism(stars2);
            asterisms.add(a2);
            assertThrows(IllegalArgumentException.class, () -> {
                new StarCatalogue(stars, asterisms);
            });
        }
    }

    @Test
    void asterismIndicesFailsOnNonValidAsterism(){
        var rng = TestRandomizer.newRandom();
        for (int j = 0; j< TestRandomizer.RANDOM_ITERATIONS; j++) {
            List<Star> stars = new ArrayList<>();
            List<Star> stars1 = new ArrayList<>();
            List<Star> stars2 = new ArrayList<>();
            List<Integer> indices1 = new ArrayList<>();
            List<Integer> indices2 = new ArrayList<>();
            for (int i = 0; i < TestRandomizer.RANDOM_ITERATIONS; i++) {
                var hipparcosId = rng.nextInt(0, Integer.MAX_VALUE);
                var colorIndex = rng.nextDouble(-0.5, 5.5 + Double.MIN_VALUE);
                var ra = rng.nextDouble(0, Angle.TAU);
                var dec = rng.nextDouble(-Math.PI/2, Math.PI/2);
                var magnitude = rng.nextDouble();
                Star s = new Star(hipparcosId, "EZ", EquatorialCoordinates.of(ra, dec), (float)magnitude, (float)colorIndex);
                stars.add(s);
                if (i % 2 == 0) {
                    stars2.add(s);
                    indices2.add(i);
                }
                if (i % 3 == 0) {
                    stars1.add(s);
                    indices1.add(i);
                }
            }
            List<Asterism> asterisms = new ArrayList<>();
            Asterism a1 = new Asterism(stars1);
            asterisms.add(a1);
            Asterism a2 = new Asterism(stars2);
            StarCatalogue sc = new StarCatalogue(stars, asterisms);
            assertEquals(stars, sc.stars());
            assertEquals(Set.copyOf(asterisms), sc.asterisms());
            assertEquals(indices1, sc.asterismIndices(a1));
            assertThrows(IllegalArgumentException.class, () -> {
                sc.asterismIndices(a2);
            });
        }
    }

}

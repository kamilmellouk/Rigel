package ch.epfl.rigel.astronomy;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Bastien Faivre (310929)
 * @author Kamil Mellouk (312327)
 */
public class MyHygDatabaseLoaderTest {
    private static final String HYG_CATALOGUE_NAME =
            "/hygdata_v3.csv";

    @Test
    void hygDatabaseIsCorrectlyInstalled() throws IOException {
        try (InputStream hygStream = getClass().getResourceAsStream(HYG_CATALOGUE_NAME)) {
            assertNotNull(hygStream);
        }
    }

    @Test
    void hygDatabaseContainsRigel() throws IOException {
        try (InputStream hygStream = getClass().getResourceAsStream(HYG_CATALOGUE_NAME)) {
            StarCatalogue catalogue = new StarCatalogue.Builder()
                    .loadFrom(hygStream, HygDatabaseLoader.INSTANCE)
                    .build();
            Star rigel = null;
            for (Star s : catalogue.stars()) {
                if (s.name().equalsIgnoreCase("rigel"))
                    rigel = s;
            }
            assertNotNull(rigel);
            assertTrue(rigel.name().equalsIgnoreCase("rigel"));
            assertEquals(24436, rigel.hipparcosId());
            assertEquals((int) Math.floor(4600 * (1 / (0.92 * -0.030 + 1.7) + 1 / (0.92 * -0.030 + 0.62))), rigel.colorTemperature());
        }
    }

    @Test
    void frameTest1() throws IOException {
        try (InputStream hygStream = getClass().getResourceAsStream(HYG_CATALOGUE_NAME)) {
            StarCatalogue catalogue = new StarCatalogue.Builder()
                    .loadFrom(hygStream, HygDatabaseLoader.INSTANCE)
                    .build();
            int i = 0;
            for(Star star : catalogue.stars()) {
                if (star.name().charAt(0) == '?') {
                    i = 1;
                    assertEquals(' ', star.name().charAt(1));
                }
            }
                    assertEquals(1,i);
        }
    }

    @Test
    void hygPutsCorrectDefaultProper() throws IOException {
        StarCatalogue.Builder cb;
        try (InputStream hygStream = getClass().getResourceAsStream(HYG_CATALOGUE_NAME)) {
            cb = new StarCatalogue.Builder().loadFrom(hygStream, HygDatabaseLoader.INSTANCE);
        }
        int i = 0;
        int count = 0;
        for (Star star : cb.stars()) {
            if (star.name().charAt(0) == '?') {
                i = 1;
                assertEquals(' ', star.name().charAt(1));
                ++count;
            }
        }
        assertEquals(1, i);
        assertEquals(3601, count);
    }

    @Test
    void hygPutsCorrectDefaultMag() throws IOException {
        StarCatalogue.Builder cb;
        try (InputStream hygStream = getClass().getResourceAsStream(HYG_CATALOGUE_NAME)) {
            cb = new StarCatalogue.Builder().loadFrom(hygStream, HygDatabaseLoader.INSTANCE);
        }
        int i = 0;
        int count = 0;
        for (Star star : cb.stars()) {
            if (star.magnitude() == 0) {
                i = 1;
                ++count;
            }
        }
        assertEquals(0, i);
        assertEquals(0, count);
    }

    @Test
    void hygPutsCorrectDefaultHipID() throws IOException {
        StarCatalogue.Builder cb;
        try (InputStream hygStream = getClass().getResourceAsStream(HYG_CATALOGUE_NAME)) {
            cb = new StarCatalogue.Builder().loadFrom(hygStream, HygDatabaseLoader.INSTANCE);
        }
        int i = 0;
        int count = 0;
        for (Star star : cb.stars()) {
            if (star.hipparcosId() == 0) {
                i = 1;
                ++count;
            }
        }
        assertEquals(1, i);
        assertEquals(25, count);
    }

    @Test
    void hygPutsCorrectDefaultBV() throws IOException {
        StarCatalogue.Builder cb;
        try (InputStream hygStream = getClass().getResourceAsStream(HYG_CATALOGUE_NAME)) {
            cb = new StarCatalogue.Builder().loadFrom(hygStream, HygDatabaseLoader.INSTANCE);
        }
        int i = 0;
        int count = 0;
        int val = (int) Math.floor(4600 * (1 / 1.7 + 1 / 0.62));
        for (Star star : cb.stars()) {
            if (star.colorTemperature() == val) {
                System.out.println(star.info());
                i = 1;
                ++count;
            }
        }
        assertEquals(1, i);
        assertEquals(28, count);
    }


}

package ch.epfl.rigel.astronomy;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Bastien Faivre (310929)
 * @author Kamil Mellouk (312327)
 */
public class MyAsterismLoaderTest {
    private static final String HYG_CATALOGUE_NAME =
            "/hygdata_v3.csv";
    private static final String AST_CATALOGUE_NAME =
            "/asterisms.txt";

    @Test
    void astDatabaseIsCorrectlyInstalled() throws IOException {
        try (InputStream astStream = getClass()
                .getResourceAsStream(AST_CATALOGUE_NAME)) {
            assertNotNull(astStream);
        }
    }

    @Test
    void astDatabaseContainsRigel() throws IOException {
        InputStream hygStream = getClass().getResourceAsStream(HYG_CATALOGUE_NAME);
        try (InputStream astStream = getClass().getResourceAsStream(AST_CATALOGUE_NAME)) {
            StarCatalogue catalogue = new StarCatalogue.Builder()
                    .loadFrom(hygStream, HygDatabaseLoader.INSTANCE)
                    .loadFrom(astStream, AsterismLoader.INSTANCE)
                    .build();
            Star rigel = null;
            for (Star s : catalogue.stars()) {
                if (s.name().equalsIgnoreCase("rigel"))
                    rigel = s;
            }
            assertNotNull(rigel);
        }
    }


}

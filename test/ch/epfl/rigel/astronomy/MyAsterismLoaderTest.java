package ch.epfl.rigel.astronomy;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

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
    void astDatabaseContainsRigelAsterisms() throws IOException {
        InputStream hygStream = getClass().getResourceAsStream(HYG_CATALOGUE_NAME);
        try (InputStream astStream = getClass().getResourceAsStream(AST_CATALOGUE_NAME)) {
            StarCatalogue catalogue = new StarCatalogue.Builder()
                    .loadFrom(hygStream, HygDatabaseLoader.INSTANCE)
                    .loadFrom(astStream, AsterismLoader.INSTANCE)
                    .build();

            for(Asterism ast : catalogue.asterisms()) {
                if(ast.stars().get(0).hipparcosId() == 24436) {
                    assertTrue(ast.stars().get(1).hipparcosId() == 27366);
                    assertTrue(ast.stars().get(2).hipparcosId() == 26727);
                    assertTrue(ast.stars().get(3).hipparcosId() == 27989);
                    assertTrue(ast.stars().get(4).hipparcosId() == 28614);
                    assertTrue(ast.stars().get(5).hipparcosId() == 29426);
                    assertTrue(ast.stars().get(6).hipparcosId() == 28716);
                }
                if(ast.stars().get(0).hipparcosId() == 25336) {
                    assertTrue(ast.stars().get(1).hipparcosId() == 25930);
                    assertTrue(ast.stars().get(2).hipparcosId() == 24674);
                    assertTrue(ast.stars().get(3).hipparcosId() == 24436);
                }
            }
        }
    }


}

package ch.epfl.rigel.astronomy;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

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
        try (InputStream astStream = getClass().getResourceAsStream(AST_CATALOGUE_NAME)) {
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

    @Test
    void frameTest1() throws IOException{
        InputStream hygStream = getClass().getResourceAsStream(HYG_CATALOGUE_NAME);
        try (InputStream astStream = getClass().getResourceAsStream(AST_CATALOGUE_NAME)) {
            StarCatalogue catalogue = new StarCatalogue.Builder()
                    .loadFrom(hygStream, HygDatabaseLoader.INSTANCE)
                    .loadFrom(astStream, AsterismLoader.INSTANCE)
                    .build();

            Queue<Asterism> a = new ArrayDeque<>();
            Star beltegeuse = null;
            for (Asterism ast : catalogue.asterisms()) {
                for (Star s : ast.stars()) {if (s.name().equalsIgnoreCase("Rigel")) { a.add(ast);}}}
            int astCount = 0;
            for (Asterism ast : a) {
                ++astCount;
                for (Star s : ast.stars()) {if (s.name().equalsIgnoreCase("Betelgeuse")) { beltegeuse = s; }}}
            assertNotNull(beltegeuse);
            assertEquals(2,astCount);
        }
    }

    @Test
    void variousTestsAndReadablePrintfOnCompletelyFinishedStarCatalogue() throws IOException {
        try (InputStream hygStream = getClass().getResourceAsStream(HYG_CATALOGUE_NAME)) {
            InputStream asterismStream = getClass()
                    .getResourceAsStream(AST_CATALOGUE_NAME);
            StarCatalogue catalogue = new StarCatalogue.Builder()
                    .loadFrom(hygStream, HygDatabaseLoader.INSTANCE).loadFrom(asterismStream, AsterismLoader.INSTANCE)
                    .build();
            Star rigel = null;
            for (Star s : catalogue.stars()) {
                if (s.name().equalsIgnoreCase("rigel"))
                    rigel = s;
            }
            assertNotNull(rigel);

            List<Star> allStar = new ArrayList<Star>();
            allStar.addAll(catalogue.stars());

            System.out.println("LIST OF STARS :");
            for(Star s : allStar){
                System.out.print(s.hipparcosId() + " ");
            } //should print out the same star IDS as in the fichier (check visually)
            System.out.println();
            System.out.println();

            System.out.println("ASTERISMS : ");
            int i;

            //vérifier visuellement en utilisant CTRL-F que les astérismes contenu dans ASTERISMS sont bien les memes
            //flemme de coder une méthode qui vérifie automatiquement
            for(Asterism asterism : catalogue.asterisms()){
                List<Integer> cAstInd = catalogue.asterismIndices(asterism);
                i = 0;
                for(Star star : asterism.stars()){
                    System.out.print("Hip : ");
                    System.out.print(star.hipparcosId());
                    System.out.print("  foundHipparcos : ");
                    System.out.print(allStar.get(cAstInd.get(i)).hipparcosId());

                /*TEST : l'index stoqué dans asterismIndices renvoie le meme hipparcosId que
                l'index stoqué dans l'astérisme voulu : */
                    assertEquals(allStar.get(cAstInd.get(i)).hipparcosId(), star.hipparcosId());
                    System.out.print(" ||| ");
                    i++;
                }
                System.out.println();
            }
        }
    }


}

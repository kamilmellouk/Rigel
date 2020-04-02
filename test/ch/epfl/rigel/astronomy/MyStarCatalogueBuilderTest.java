package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import org.junit.jupiter.api.Test;


import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Bastien Faivre (310929)
 * @author Kamil Mellouk (312327)
 */
public class MyStarCatalogueBuilderTest {

    StarCatalogue.Builder cb = new StarCatalogue.Builder();
    Star s0 = new Star(0, "s0",
            EquatorialCoordinates.of(0, 0), 0f, 0f);
    Star s1 = new Star(0, "s1",
            EquatorialCoordinates.of(0, 0), 0f, 0f);
    Star s2 = new Star(0, "s2",
            EquatorialCoordinates.of(0, 0), 0f, 0f);
    Star s3 = new Star(0, "s3",
            EquatorialCoordinates.of(0, 0), 0f, 0f);
    List<Star> sList = new ArrayList<>();

    @Test
    void createsEmptyStarCatalogue() {
        StarCatalogue catalogue = cb.build();
        assertTrue(catalogue.stars().isEmpty());
        assertTrue(catalogue.asterisms().isEmpty());
    }

    @Test
    void addStarWorks() {
        sList.add(s0);
        sList.add(s1);
        sList.add(s2);
        sList.add(s3);

        cb.addStar(s0).addStar(s1).addStar(s2).addStar(s3);
        StarCatalogue catalogue = cb.build();

        assertEquals(sList, catalogue.stars());
    }

    @Test
    void addAsterismWorks() {
        sList.add(s0);
        sList.add(s1);
        sList.add(s2);
        sList.add(s3);

        cb.addStar(s0).addStar(s1).addStar(s2).addStar(s3);

        Asterism ass = new Asterism(sList);
        cb.addAsterism(ass);

        List<Asterism> assList = new ArrayList<>();
        assList.add(ass);

        StarCatalogue catalogue = cb.build();

        assertTrue(catalogue.asterisms().contains(ass));
    }

    @Test
    void modifyingStarAndAsterismThrowsIO() {
        sList.add(s0);
        sList.add(s1);
        sList.add(s2);
        sList.add(s3);
        Asterism ass = new Asterism(sList);

        StarCatalogue catalogue = cb.build();
        List<Star> unmodStar = catalogue.stars();
        Set<Asterism> unmodAst = catalogue.asterisms();

        assertThrows(UnsupportedOperationException.class, () -> {
            unmodStar.add(s0);
        });
        assertThrows(UnsupportedOperationException.class, () -> {
            unmodAst.add(ass);
        });
    }

}

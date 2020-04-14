package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.*;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.time.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Bastien Faivre (310929)
 * @author Kamil Mellouk (312327)
 */

class MyObservedSkyTest {

    private static final ZonedDateTime when = ZonedDateTime.of(
            LocalDate.of(2003, Month.SEPTEMBER, 1),
            LocalTime.MIDNIGHT,
            ZoneOffset.UTC);
    private static final GeographicCoordinates where = GeographicCoordinates.ofDeg(0, 0);
    private static final StereographicProjection proj = new StereographicProjection(HorizontalCoordinates.ofDeg(0, 0));
    private static final List<Star> listStar = new ArrayList<>();
    private static final List<Asterism> listAsterism = new ArrayList<>();
    private static final StarCatalogue starCatalogue = new StarCatalogue(listStar, listAsterism);
    private static final ObservedSky observedSky = new ObservedSky(when, where, proj, starCatalogue);

    @Test
    void listPlanetsWorks() {
        assertEquals(7, observedSky.planets().size());
    }

    @Test
    void earthIsSkipped() {
        boolean earthIsThere = false;
        for (Planet planet : observedSky.planets()) {
            if (planet.name().equals("Terre")) {
                earthIsThere = true;
                break;
            }
        }
        assertFalse(earthIsThere);
    }

    @Test
    void asterismsWorks() {
        assertTrue(observedSky.asterisms().isEmpty());
    }

    @Test
    void asterismIndicesThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> observedSky.asterismIndices(new Asterism(listStar)));
    }

    @Test
    void distanceBetweenWorks() {
        // to test, make the method public
        /*assertEquals(2, observedSky.distanceBetween(CartesianCoordinates.of(0, 0), CartesianCoordinates.of(0, 2)));
        assertEquals(5, observedSky.distanceBetween(CartesianCoordinates.of(1, 3), CartesianCoordinates.of(6, 3)));
        assertEquals(Math.sqrt(2), observedSky.distanceBetween(CartesianCoordinates.of(0, 0), CartesianCoordinates.of(1, 1)));*/
    }

    @Test
    void objectClosestToWorksOnEdgeCases() {
        assertEquals(Optional.empty(), observedSky.objectClosestTo(CartesianCoordinates.of(0, 0), 0));
    }

    @Test
    void objectClosestToWorksFrama() throws IOException {
        String HYG_CATALOGUE_NAME = "/hygdata_v3.csv";
        String AST_CATALOGUE_NAME = "/asterisms.txt";
        StarCatalogue catalogue;
        ObservedSky sky;
        StereographicProjection stereo;
        GeographicCoordinates geoCoords;
        ZonedDateTime time;
        EquatorialToHorizontalConversion convEquToHor;
        EclipticToEquatorialConversion convEcltoEqu;
        StarCatalogue.Builder builder;

        try (InputStream hygStream = getClass().getResourceAsStream(HYG_CATALOGUE_NAME)) {
            builder = new StarCatalogue.Builder().loadFrom(hygStream, HygDatabaseLoader.INSTANCE);
        }
        try (InputStream astStream = getClass().getResourceAsStream(AST_CATALOGUE_NAME)) {
            catalogue = builder.loadFrom(astStream, AsterismLoader.INSTANCE).build();
        }
        time = ZonedDateTime.of(LocalDate.of(2020, Month.APRIL, 4),LocalTime.of(0, 0), ZoneOffset.UTC);
        geoCoords = GeographicCoordinates.ofDeg(30, 45);
        stereo = new StereographicProjection(HorizontalCoordinates.ofDeg(20, 22));
        convEquToHor = new EquatorialToHorizontalConversion(time, geoCoords);
        convEcltoEqu = new EclipticToEquatorialConversion(time);
        sky = new ObservedSky(time, geoCoords, stereo, catalogue);


        assertEquals("Tau Phe",
                sky.objectClosestTo(stereo.apply(new EquatorialToHorizontalConversion(time,geoCoords)
                        .apply(EquatorialCoordinates.of(0.004696959812148989,-0.861893035343076))),0.1).get().name());

        assertEquals(Optional.empty(),
                sky.objectClosestTo(stereo.apply(new EquatorialToHorizontalConversion(time,geoCoords)
                        .apply(EquatorialCoordinates.of(0.004696959812148989,-0.8618930353430763))),0.001));

    }
}
package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.*;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Bastien Faivre (310929)
 * @author Kamil Mellouk (312327)
 */
// TODO: 07/04/2020 final class?
public final class ObservedSky {

    private final Sun sun;
    private final Moon moon;
    private final List<Planet> planets;

    private final StereographicProjection stereoProj;

    private final Map<CelestialObject, CartesianCoordinates> objectPosMap = new HashMap<>();

    /**
     * Constructor of the observed sky
     *
     * @param when          the observation zoned date time
     * @param where         the observation position
     * @param stereoProj    the stereographic projection
     * @param starCatalogue the catalogue of stars
     */
    public ObservedSky(ZonedDateTime when, GeographicCoordinates where, StereographicProjection stereoProj, StarCatalogue starCatalogue) {

        sun = SunModel.SUN.at(Epoch.J2010.daysUntil(when), new EclipticToEquatorialConversion(when));
        moon = MoonModel.MOON.at(Epoch.J2010.daysUntil(when), new EclipticToEquatorialConversion(when));
        List<Planet> planets = new ArrayList<>();
        for (PlanetModel planet : PlanetModel.values()) {
            if (!planet.equals(PlanetModel.EARTH))
                planets.add(planet.at(Epoch.J2010.daysUntil(when), new EclipticToEquatorialConversion(when)));
        }
        this.planets = List.copyOf(planets);

        this.stereoProj = stereoProj;

        objectPosMap.put(moon, stereoProj.apply(new EquatorialToHorizontalConversion(when, where).apply(moon.equatorialPos())));

    }

    /**
     * Getter for the sun
     *
     * @return the sun
     */
    public Sun sun() {
        return sun;
    }

    public CartesianCoordinates sunPosition() {
        return stereoProj.apply()
    }


    /**
     * Getter for the moon
     *
     * @return the moon
     */
    public Moon moon() {
        return moon;
    }

    /*private final StereographicProjection stereographicProjection;

    private final Sun sun;

    private final Map<CartesianCoordinates, CelestialObject> posObjectMap = new HashMap<>();

    public ObservedSky(ZonedDateTime obsTime, GeographicCoordinates obsPos, StereographicProjection stereographicProjection, StarCatalogue starCatalogue) {

        this.stereographicProjection = stereographicProjection;

        sun = SunModel.SUN.at(Epoch.J2010.daysUntil(obsTime), new EclipticToEquatorialConversion(obsTime));
        posObjectMap.put(
                stereographicProjection.apply((new EquatorialToHorizontalConversion(obsTime, obsPos)).apply(sun.equatorialPos())),
                sun
        );


    }*/
}

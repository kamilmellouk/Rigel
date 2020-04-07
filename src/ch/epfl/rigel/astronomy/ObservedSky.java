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

    /**
     * Constructor of the observed sky
     *
     * @param observationTime         the observation zoned date time
     * @param observationPosition     the observation position
     * @param stereographicProjection the stereographic projection
     * @param starCatalogue           the catalogue of stars
     */
    public ObservedSky(ZonedDateTime observationTime, GeographicCoordinates observationPosition, StereographicProjection stereographicProjection, StarCatalogue starCatalogue) {
        sun = SunModel.SUN.at(Epoch.J2010.daysUntil(observationTime), new EclipticToEquatorialConversion(observationTime));
        moon = MoonModel.MOON.at(Epoch.J2010.daysUntil(observationTime), new EclipticToEquatorialConversion(observationTime));
        List<Planet> mutablePlanetsList = new ArrayList<>();
        for (PlanetModel planet : PlanetModel.values()) {
            mutablePlanetsList.add(planet.at(Epoch.J2010.daysUntil(observationTime), new EclipticToEquatorialConversion(observationTime)));
        }
        planets = List.copyOf(mutablePlanetsList);
    }

    /**
     * Getter for the sun
     *
     * @return the sun
     */
    public Sun sun() {
        return sun;
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

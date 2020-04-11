package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.*;

import java.time.ZonedDateTime;
import java.util.*;

/**
 * Representation of the sky at a given time and place
 *
 * @author Bastien Faivre (310929)
 * @author Kamil Mellouk (312327)
 */
public final class ObservedSky {

    private final Map<CelestialObject, CartesianCoordinates> objectsWithCoordinates = new HashMap<>();

    private final Sun sun;
    private final Moon moon;

    private final List<Planet> planets;
    private final double[] planetPositions = new double[14];

    private final StarCatalogue starCatalogue;

    /**
     * Constructor of the observed sky
     *
     * @param when                    the observation zoned date time
     * @param where                   the observation position
     * @param stereographicProjection the stereographic projection
     * @param starCatalogue           the catalogue of stars
     */
    public ObservedSky(ZonedDateTime when, GeographicCoordinates where, StereographicProjection stereographicProjection, StarCatalogue starCatalogue) {
        EquatorialToHorizontalConversion conversionSystem = new EquatorialToHorizontalConversion(when, where);

        sun = SunModel.SUN.at(Epoch.J2010.daysUntil(when), new EclipticToEquatorialConversion(when));
        CartesianCoordinates sunPosition = stereographicProjection.apply(conversionSystem.apply(sun.equatorialPos()));
        objectsWithCoordinates.put(sun, sunPosition);

        moon = MoonModel.MOON.at(Epoch.J2010.daysUntil(when), new EclipticToEquatorialConversion(when));
        CartesianCoordinates moonPosition = stereographicProjection.apply(conversionSystem.apply(moon.equatorialPos()));
        objectsWithCoordinates.put(moon, moonPosition);

        List<Planet> mutablePlanetsList = new ArrayList<>();
        int index = 0;
        for (PlanetModel planet : PlanetModel.values()) {
            // the earth is skipped
            if (!planet.equals(PlanetModel.EARTH)) {
                Planet planetModel = planet.at(Epoch.J2010.daysUntil(when), new EclipticToEquatorialConversion(when));
                mutablePlanetsList.add(planetModel);

                CartesianCoordinates position = stereographicProjection.apply(conversionSystem.apply(planetModel.equatorialPos()));
                planetPositions[index] = position.x();
                planetPositions[index + 1] = position.y();
                index += 2;

                objectsWithCoordinates.put(planetModel, position);
            }
        }
        planets = List.copyOf(mutablePlanetsList);

        this.starCatalogue = starCatalogue;

        for (Star star : starCatalogue.stars()) {
            CartesianCoordinates starPosition = stereographicProjection.apply(conversionSystem.apply(star.equatorialPos()));
            objectsWithCoordinates.put(star, starPosition);
        }

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
     * Getter for the position of the sun
     *
     * @return the position of the sun
     */
    public CartesianCoordinates sunPosition() {
        return objectsWithCoordinates.get(sun);
    }

    /**
     * Getter for the moon
     *
     * @return the moon
     */
    public Moon moon() {
        return moon;
    }

    /**
     * Getter for the position of the moon
     *
     * @return the position of the moon
     */
    public CartesianCoordinates moonPosition() {
        return objectsWithCoordinates.get(moon);
    }

    /**
     * Getter for the list containing the 7 extraterrestrial planets
     *
     * @return the list containing the 7 extraterrestrial planets
     */
    public List<Planet> planets() {
        return planets;
    }

    /**
     * Getter for the coordinates of the 7 extraterrestrial planets
     *
     * @return the array containing the coordinates of the 7 extraterrestrial planets
     */
    public double[] planetPositions() {
        return planetPositions;
    }

    /**
     * Getter for the list of stars of the star catalogue used
     *
     * @return the list of stars of the star catalogue used
     */
    public List<Star> stars() {
        return starCatalogue.stars();
    }

    /**
     * Getter for the set of asterism of the star catalogue used
     *
     * @return the set of asterism of the star catalogue used
     */
    public Set<Asterism> asterisms() {
        return starCatalogue.asterisms();
    }

    /**
     * the list of the stars indices containing in the given asterism
     *
     * @param asterism the asterism of we want to get its stars indices
     * @return the the list of the stars indices containing in the given asterism
     */
    public List<Integer> asterismIndices(Asterism asterism) {
        return starCatalogue.asterismIndices(asterism);
    }

    /**
     * Return the closest celestial object of the given coordinates but in the range of the given max distance
     *
     * @param coordinates the given point
     * @param maxDistance the limit range
     * @return the closest celestial object
     * or Optional.empty() if there isn't any celestial object in the specific range
     */
    public Optional<CelestialObject> objectClosestTo(CartesianCoordinates coordinates, double maxDistance) {
        double minDistance = maxDistance;
        CelestialObject closestObject = null;
        // find the closest celestial object
        for (CelestialObject celestialObject : objectsWithCoordinates.keySet()) {
            double distance = distanceBetween(objectsWithCoordinates.get(celestialObject), coordinates);
            if (distance < maxDistance && distance < minDistance) {
                minDistance = distance;
                closestObject = celestialObject;
            }
        }
        return Optional.ofNullable(closestObject);
    }

    /**
     * Compute the distance between the two given points
     *
     * @param p1 the first point
     * @param p2 the second point
     * @return the distance between the two given points
     */
    private double distanceBetween(CartesianCoordinates p1, CartesianCoordinates p2) {
        return Math.sqrt((p1.x() - p2.x()) * (p1.x() - p2.x()) +
                         (p1.y() - p2.y()) * (p1.y() - p2.y()));
    }

}

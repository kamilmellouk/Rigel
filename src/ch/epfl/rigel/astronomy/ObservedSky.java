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

    private final Map<ObservedCelestialObjects, double[]> objectPosMap = new HashMap<>();

    private final Sun sun;
    private final Moon moon;
    private final List<Planet> planets;

    private final StarCatalogue catalogue;


    /**
     * Constructor of the observed sky
     *
     * @param when                    the observation zoned date time
     * @param where                   the observation position
     * @param stereographicProjection the stereographic projection
     * @param catalogue               the catalogue of stars
     */
    public ObservedSky(ZonedDateTime when, GeographicCoordinates where, StereographicProjection stereographicProjection, StarCatalogue catalogue) {
        EquatorialToHorizontalConversion conversionSystem = new EquatorialToHorizontalConversion(when, where);

        // add sun
        sun = SunModel.SUN.at(Epoch.J2010.daysUntil(when), new EclipticToEquatorialConversion(when));
        CartesianCoordinates sunPosition = stereographicProjection.apply(conversionSystem.apply(sun.equatorialPos()));
        objectPosMap.put(ObservedCelestialObjects.SUN, new double[]{sunPosition.x(), sunPosition.y()});

        // add moon
        moon = MoonModel.MOON.at(Epoch.J2010.daysUntil(when), new EclipticToEquatorialConversion(when));
        CartesianCoordinates moonPosition = stereographicProjection.apply(conversionSystem.apply(moon.equatorialPos()));
        objectPosMap.put(ObservedCelestialObjects.MOON, new double[]{moonPosition.x(), moonPosition.y()});

        // add planets
        List<Planet> tempPlanetList = new ArrayList<>();
        double[] planetPositions = new double[14];
        int planetIndex = 0;
        for (PlanetModel planetModel : PlanetModel.values()) {
            // the earth is skipped
            if (!planetModel.equals(PlanetModel.EARTH)) {
                Planet planet = planetModel.at(Epoch.J2010.daysUntil(when), new EclipticToEquatorialConversion(when));
                tempPlanetList.add(planet);

                CartesianCoordinates position = stereographicProjection.apply(conversionSystem.apply(planet.equatorialPos()));
                planetPositions[planetIndex] = position.x();
                planetPositions[planetIndex + 1] = position.y();
                planetIndex += 2;
            }
        }
        objectPosMap.put(ObservedCelestialObjects.PLANETS, planetPositions);
        planets = List.copyOf(tempPlanetList);

        // add stars
        this.catalogue = catalogue;
        double[] starPositions = new double[catalogue.stars().size() * 2];
        int starIndex = 0;
        for (Star star : catalogue.stars()) {
            CartesianCoordinates position = stereographicProjection.apply(conversionSystem.apply(star.equatorialPos()));
            starPositions[starIndex] = position.x();
            starPositions[starIndex + 1] = position.y();
            starIndex += 2;
        }
        objectPosMap.put(ObservedCelestialObjects.STARS, starPositions);

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
        return CartesianCoordinates.of(
                objectPosMap.get(ObservedCelestialObjects.SUN)[0],
                objectPosMap.get(ObservedCelestialObjects.SUN)[1]
        );
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
        return CartesianCoordinates.of(
                objectPosMap.get(ObservedCelestialObjects.MOON)[0],
                objectPosMap.get(ObservedCelestialObjects.MOON)[1]
        );
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
        return objectPosMap.get(ObservedCelestialObjects.PLANETS);
    }

    /**
     * Getter for the list of stars of the star catalogue used
     *
     * @return the list of stars of the star catalogue used
     */
    public List<Star> stars() {
        return catalogue.stars();
    }

    /**
     * Getter for the coordinates of the stars
     *
     * @return the array containing the coordinates of the stars
     */
    public double[] starPositions() {
        return objectPosMap.get(ObservedCelestialObjects.STARS);
    }


    /**
     * Getter for the set of asterism of the star catalogue used
     *
     * @return the set of asterism of the star catalogue used
     */
    public Set<Asterism> asterisms() {
        return catalogue.asterisms();
    }

    /**
     * the list of the stars indices containing in the given asterism
     *
     * @param asterism the asterism of we want to get its stars indices
     * @return the the list of the stars indices containing in the given asterism
     */
    public List<Integer> asterismIndices(Asterism asterism) {
        return catalogue.asterismIndices(asterism);
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
        // we work with the square of distances for performances
        double minDistanceSquared = maxDistance * maxDistance;
        CelestialObject closestObject = null;

        double x = coordinates.x();
        double y = coordinates.y();

        System.out.println(sunPosition());
        System.out.println(x + " " + y);

        if (Math.abs(x - sunPosition().x()) < maxDistance && Math.abs(y - sunPosition().y()) < maxDistance) {
            double sunDistanceSquared = distanceBetweenSquared(coordinates, sunPosition());
            if (sunDistanceSquared < minDistanceSquared) {
                closestObject = sun;
            }
        }
        /*
        if (Math.abs(x - moonPosition().x()) < maxDistance && Math.abs(y - moonPosition().y()) < maxDistance) {
            double moonDistanceSquared = distanceBetweenSquared(coordinates, moonPosition());
            if (moonDistanceSquared < minDistanceSquared) {
                closestObject = moon;
            }
        }

        for (int i = 0; i < planetPositions().length; i += 2) {
            CartesianCoordinates planetPos = CartesianCoordinates.of(planetPositions()[i], planetPositions()[i + 1]);
            if (Math.abs(x - planetPos.x()) < maxDistance && Math.abs(y - planetPos.y()) < maxDistance) {
                double distanceSquared = distanceBetweenSquared(planetPos, coordinates);
                if (distanceSquared < minDistanceSquared) {
                    minDistanceSquared = distanceSquared;
                    closestObject = planets().get(i / 2);
                }
            }
        }

        for (int i = 0; i < starPositions().length; i += 2) {
            CartesianCoordinates starPos = CartesianCoordinates.of(starPositions()[i], starPositions()[i + 1]);
            if (Math.abs(x - starPos.x()) < maxDistance && Math.abs(y - starPos.y()) < maxDistance) {
                double distanceSquared = distanceBetweenSquared(starPos, coordinates);
                if (distanceSquared < minDistanceSquared) {
                    minDistanceSquared = distanceSquared;
                    closestObject = stars().get(i / 2);
                }
            }
        }*/

        return Optional.ofNullable(closestObject);
    }

    /**
     * Compute the distance between the two given points
     *
     * @param p1 the first point
     * @param p2 the second point
     * @return the distance between the two given points
     */
    private double distanceBetweenSquared(CartesianCoordinates p1, CartesianCoordinates p2) {
        return (p1.x() - p2.x()) * (p1.x() - p2.x()) +
                (p1.y() - p2.y()) * (p1.y() - p2.y());
    }

    private enum ObservedCelestialObjects {
        SUN, MOON, PLANETS, STARS
    }

}

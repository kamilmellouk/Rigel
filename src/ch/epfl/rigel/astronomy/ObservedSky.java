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

    // Associating each celestial object(s) with its position(s)
    private final Map<ObservedCelestialObjects, double[]> objectPosMap = new HashMap<>();

    private final Sun sun;
    private final Moon moon;
    private final List<Planet> planets;

    private final StarCatalogue catalogue;
    private final List<Star> stars;
    private final Set<Asterism> asterisms;

    /**
     * Constructor of the observed sky
     *
     * @param when                    the observation zoned date time
     * @param where                   the observation position
     * @param stereographicProjection the stereographic projection
     * @param catalogue               the catalogue of stars
     */
    public ObservedSky(ZonedDateTime when, GeographicCoordinates where, StereographicProjection stereographicProjection, StarCatalogue catalogue) {
        EquatorialToHorizontalConversion equToHorConversion = new EquatorialToHorizontalConversion(when, where);
        EclipticToEquatorialConversion eclToEquConversion = new EclipticToEquatorialConversion(when);
        double daysFromJ2010UntilWhen = Epoch.J2010.daysUntil(when);

        // add sun
        this.sun = SunModel.SUN.at(daysFromJ2010UntilWhen, eclToEquConversion);
        CartesianCoordinates sunPosition = stereographicProjection.apply(equToHorConversion.apply(this.sun.equatorialPos()));
        this.objectPosMap.put(ObservedCelestialObjects.SUN, new double[]{sunPosition.x(), sunPosition.y()});

        // add moon
        this.moon = MoonModel.MOON.at(daysFromJ2010UntilWhen, eclToEquConversion);
        CartesianCoordinates moonPosition = stereographicProjection.apply(equToHorConversion.apply(this.moon.equatorialPos()));
        this.objectPosMap.put(ObservedCelestialObjects.MOON, new double[]{moonPosition.x(), moonPosition.y()});

        // add planets
        List<Planet> tempPlanetList = new ArrayList<>();
        double[] planetPositions = new double[14];
        int planetIndex = 0;
        for (PlanetModel planetModel : PlanetModel.values()) {
            // the earth is skipped
            if (!planetModel.equals(PlanetModel.EARTH)) {
                Planet planet = planetModel.at(daysFromJ2010UntilWhen, eclToEquConversion);
                tempPlanetList.add(planet);

                CartesianCoordinates position = stereographicProjection.apply(equToHorConversion.apply(planet.equatorialPos()));
                planetPositions[planetIndex] = position.x();
                planetPositions[planetIndex + 1] = position.y();
                planetIndex += 2;
            }
        }
        this.objectPosMap.put(ObservedCelestialObjects.PLANETS, planetPositions);
        this.planets = List.copyOf(tempPlanetList);

        this.catalogue = catalogue;
        this.stars = catalogue.stars();
        this.asterisms = catalogue.asterisms();
        // add stars
        double[] starPositions = new double[this.stars.size() * 2];
        int starIndex = 0;
        for (Star star : catalogue.stars()) {
            CartesianCoordinates position = stereographicProjection.apply(equToHorConversion.apply(star.equatorialPos()));
            starPositions[starIndex] = position.x();
            starPositions[starIndex + 1] = position.y();
            starIndex += 2;
        }
        this.objectPosMap.put(ObservedCelestialObjects.STARS, starPositions);
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
        return stars;
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
        return asterisms;
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

        ObservedCelestialObjects typeOfclosestObject = null;
        int index = 0;

        for (ObservedCelestialObjects type : objectPosMap.keySet()) {
            double[] positions = objectPosMap.get(type);
            for (int i = 0; i < positions.length; i += 2) {
                CartesianCoordinates pos = CartesianCoordinates.of(positions[i], positions[i + 1]);
                if (Math.abs(x - pos.x()) < maxDistance && Math.abs(y - pos.y()) < maxDistance) {
                    double distanceSquared = distanceBetweenSquared(pos, coordinates);
                    if (distanceSquared < minDistanceSquared) {
                        minDistanceSquared = distanceSquared;
                        typeOfclosestObject = type;
                        index = i;
                    }
                }
            }
        }

        if (typeOfclosestObject != null) {
            if (typeOfclosestObject == ObservedCelestialObjects.SUN) {
                closestObject = sun;
            } else if (typeOfclosestObject == ObservedCelestialObjects.MOON) {
                closestObject = moon;
            } else if (typeOfclosestObject == ObservedCelestialObjects.PLANETS) {
                closestObject = planets().get(index / 2);
            } else {
                closestObject = stars().get(index / 2);
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
    private double distanceBetweenSquared(CartesianCoordinates p1, CartesianCoordinates p2) {
        return (p1.x() - p2.x()) * (p1.x() - p2.x()) +
                (p1.y() - p2.y()) * (p1.y() - p2.y());
    }

    /**
     * Enumeration of the CelestialObjects to observe
     */
    private enum ObservedCelestialObjects {
        SUN, MOON, PLANETS, STARS
    }

}

package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EquatorialCoordinates;

/**
 * @author Bastien Faivre (310929)
 * @author Kamil Mellouk (312327)
 */

public final class Planet extends CelestialObject {

    /**
     * Constructor of a Planet
     *
     * @param name          the name of the planet
     * @param equatorialPos (not null) the equatorial coordinates of the planet
     * @param angularSize   (non negative) the angular size of the planet
     * @param magnitude     the magnitude of the planet
     */
    public Planet(String name, EquatorialCoordinates equatorialPos, float angularSize, float magnitude) {
        super(name, equatorialPos, angularSize, magnitude);
    }

}

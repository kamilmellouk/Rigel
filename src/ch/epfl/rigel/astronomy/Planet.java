package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EquatorialCoordinates;

/**
 * @author Bastien Faivre (310929)
 * @author Kamil Mellouk (312327)
 */
public final class Planet extends CelestialObject {
    /**
     * Constructing a Planet
     * @param name of the Planet
     * @param equatorialPos (not null) equatorial coordinates of the Planet
     * @param angularSize (non negative) angular size of the Planet
     * @param magnitude magnitude of the Planet
     */
    Planet(String name, EquatorialCoordinates equatorialPos, float angularSize, float magnitude) {
        super(name, equatorialPos, angularSize, magnitude);
    }
}

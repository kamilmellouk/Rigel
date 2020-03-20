package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.Preconditions;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;

import java.util.Objects;

/**
 * @author Bastien Faivre (310929)
 * @author Kamil Mellouk (312327)
 */

public abstract class CelestialObject {

    // Attributes used to describe every celestial object
    private final String name;
    private final EquatorialCoordinates equatorialPos;
    private final float angularSize;
    private final float magnitude;

    /**
     * Constructor of a new Celestial Object (in subclasses)
     *
     * @param name          the name of the celestial object
     * @param equatorialPos (not null) the equatorial coordinates of the celestial object
     * @param angularSize   (non negative) the angular size of the celestial object
     * @param magnitude     the magnitude of the celestial object
     */
    CelestialObject(String name, EquatorialCoordinates equatorialPos, float angularSize, float magnitude) {
        // check exception
        Preconditions.checkArgument(angularSize >= 0);

        this.name = Objects.requireNonNull(name);
        this.equatorialPos = Objects.requireNonNull(equatorialPos);
        this.angularSize = angularSize;
        this.magnitude = magnitude;
    }

    /**
     * @return the name of the celestial object
     */
    public String name() {
        return name;
    }

    /**
     * @return the angular size of the celestial object
     */
    public double angularSize() {
        return angularSize;
    }

    /**
     * @return the magnitude of the celestial object
     */
    public double magnitude() {
        return magnitude;
    }

    /**
     * @return the equatorial coordinates of the celestial object
     */
    public EquatorialCoordinates equatorialPos() {
        return equatorialPos;
    }

    /**
     * Displaying information on the Celestial object (redefined in subclasses)
     *
     * @return String displaying the wanted information
     */
    public String info() {
        return name();
    }

    @Override
    public String toString() {
        return info();
    }
}

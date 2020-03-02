package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EquatorialCoordinates;

/**
 * @author Bastien Faivre (310929)
 * @author Kamil Mellouk (312327)
 */
public abstract class CelestialObject {

    // Attributes used to describe every celestial object
    private String name;
    private EquatorialCoordinates equatorialPos;
    private float angularSize;
    private float magnitude;

    /**
     * Constructing a new Celestial Object (in subclasses)
     * @param name of the celestial object
     * @param equatorialPos (not null) equatorial coordinates of the celestial object
     * @param angularSize (non negative) angular size of the celestial object
     * @param magnitude magnitude of the celestial object
     */
    CelestialObject(String name, EquatorialCoordinates equatorialPos, float angularSize, float magnitude) {
        if(angularSize < 0 || equatorialPos == null) throw new IllegalArgumentException();

        this.name = name;
        this.equatorialPos = equatorialPos;
        this.angularSize = angularSize;
        this.magnitude = magnitude;
    }

    public String name() {
        return name;
    }

    public EquatorialCoordinates equatorialPos() {
        return equatorialPos;
    }

    public double angularSize() {
        return angularSize;
    }

    public double magnitude() {
        return magnitude;
    }

    /**
     * Displaying information on the Celestial object (redefined in subclasses)
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

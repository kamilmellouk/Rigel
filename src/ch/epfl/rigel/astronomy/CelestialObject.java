package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EquatorialCoordinates;

/**
 * @author Bastien Faivre (310929)
 * @author Kamil Mellouk (312327)
 */
public abstract class CelestialObject {

    /// Attributes used to describe every celestial object
    private String name;
    private EquatorialCoordinates equatorialPos;
    private float angularSize;
    private float magnitude;

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

    public String info() {
        return name();
    }

    @Override
    public String toString() {
        return info();
    }
}

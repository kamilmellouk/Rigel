package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import ch.epfl.rigel.math.ClosedInterval;

/**
 * @author Bastien Faivre (310929)
 * @author Kamil Mellouk (312327)
 */
public final class Moon extends CelestialObject {

    /// Attribute specific to the Moon
    private float phase;

    Moon(EquatorialCoordinates equatorialPos, float angularSize, float magnitude, float phase) {
        super("Lune", equatorialPos, angularSize, magnitude);
        if(!ClosedInterval.of(0,1).contains(phase)) throw new IllegalArgumentException();

        this.phase = phase;
    }

    @Override
    public String info() {
        return String.format(super.name() + " (%s%)", phase*100);
    }
}

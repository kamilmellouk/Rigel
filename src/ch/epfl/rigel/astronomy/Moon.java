package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import ch.epfl.rigel.math.ClosedInterval;

import java.util.Locale;

/**
 * @author Bastien Faivre (310929)
 * @author Kamil Mellouk (312327)
 */
public final class Moon extends CelestialObject {

    // Attribute specific to the Moon
    private float phase;

    /**
     * Constructing a Moon
     * @param equatorialPos (not null) equatorial coordinates of the Moon
     * @param angularSize (non negative) angular size of the Moon
     * @param magnitude magnitude of the Moon
     * @param phase (contained in [0, 1]) MoonPhase
     */
    Moon(EquatorialCoordinates equatorialPos, float angularSize, float magnitude, float phase) {
        super("Lune", equatorialPos, angularSize, magnitude);
        if(!ClosedInterval.of(0,1).contains(phase)) throw new IllegalArgumentException();

        this.phase = phase;
    }

    @Override
    public String info() {
        return String.format(Locale.ROOT,super.name() + " (%.1f%%)", phase*100);
    }
}

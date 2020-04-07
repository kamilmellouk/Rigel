package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.Preconditions;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import ch.epfl.rigel.math.ClosedInterval;

import java.util.Locale;

/**
 * Representation of a moon
 *
 * @author Bastien Faivre (310929)
 * @author Kamil Mellouk (312327)
 */

public final class Moon extends CelestialObject {

    // Attribute specific to the Moon
    private final float phase;

    /**
     * Constructor of a Moon
     *
     * @param equatorialPos (not null) the equatorial coordinates of the Moon
     * @param angularSize   (non negative) the angular size of the Moon
     * @param magnitude     the magnitude of the Moon
     * @param phase         (contained in [0, 1]) the MoonPhase
     * @throws IllegalArgumentException if phase isn't contained in [0, 1]
     */
    public Moon(EquatorialCoordinates equatorialPos, float angularSize, float magnitude, float phase) {
        super("Lune", equatorialPos, angularSize, magnitude);
        this.phase = (float) Preconditions.checkInInterval(ClosedInterval.of(0, 1), phase);
    }

    @Override
    public String info() {
        return String.format(Locale.ROOT, "Lune (%.1f%%)", phase * 100);
    }
}

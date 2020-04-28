package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.Preconditions;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import ch.epfl.rigel.math.ClosedInterval;

/**
 * Representation of a star
 *
 * @author Bastien Faivre (310929)
 * @author Kamil Mellouk (312327)
 */
public final class Star extends CelestialObject {

    private static final ClosedInterval COLOR_INDEX_INTERVAL = ClosedInterval.of(-0.5, 5.5);

    // Attributes used to identify a Star, in additions to the ones used for a CelestialObject
    private final int hipparcosId;
    private final int colorTemperature;

    /**
     * Constructor of a star
     *
     * @param hipparcosId   HIP identification number for the star
     * @param name          the name of the star
     * @param equatorialPos the position of the star (in equatorial coordinates, relative to the earth)
     * @param magnitude     the magnitude of the star
     * @param colorIndex    the color index of the star
     * @throws IllegalArgumentException if hipparcorId < 0 or colorIndex isn't in [-0.5, 5.5]
     */
    public Star(int hipparcosId, String name, EquatorialCoordinates equatorialPos, float magnitude, float colorIndex) {
        super(name, equatorialPos, 0, magnitude);

        // check if the arguments are valid too
        Preconditions.checkArgument(hipparcosId >= 0);
        this.hipparcosId = hipparcosId;
        Preconditions.checkInInterval(COLOR_INDEX_INTERVAL, colorIndex);
        double interValue = 0.92 * colorIndex;
        colorTemperature = (int) (4600 * (1 / (interValue + 1.7) + 1 / (interValue + 0.62)));
    }

    /**
     * Getter for the hipparcosId
     *
     * @return the hipparcosId of the star
     */
    public int hipparcosId() {
        return hipparcosId;
    }

    /**
     * Compute the color temperature of the star, given its color index
     *
     * @return the floor of the temperature, in Kelvins
     */
    public int colorTemperature() {
        return colorTemperature;
    }

}

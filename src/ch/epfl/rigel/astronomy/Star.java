package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import ch.epfl.rigel.math.ClosedInterval;

/**
 * @author Bastien Faivre (310929)
 * @author Kamil Mellouk (312327)
 */
public final class Star extends CelestialObject {

    // Attributes used to identify a Star, in additions to the ones used for a CelestialObject
    private final int hipparcosId;
    private float colorIndex;

    /**
     * Constructing a new Star
     * @param hipparcosId HIP identification number for the star
     * @param name of the star
     * @param equatorialPos position of the star (in equatorial coordinates, relative to the earth)
     * @param magnitude of the star
     * @param colorIndex of the star
     */
    public Star(int hipparcosId, String name, EquatorialCoordinates equatorialPos, float magnitude, float colorIndex) {
        super(name, equatorialPos, 0, magnitude);
        if(hipparcosId < 0 || !ClosedInterval.of(-0.5, 0.5).contains(colorIndex))
            throw new IllegalArgumentException();

        this.hipparcosId = hipparcosId;
        this.colorIndex = colorIndex;
    }

    /**
     * Getter for the HipparcosId
     * @return hipparcosId
     */
    public int hipparcorId() {
        return hipparcosId;
    }

    /**
     * Computing the color temperature of the star, given its color index
     * @return floor of the temperature, in Kelvins
     */
    public int colorTemperature() {
        return (int) Math.floor(4600*(1/(0.92*colorIndex + 1.7) + 1/(0.92*colorIndex + 0.62)));
    }

}

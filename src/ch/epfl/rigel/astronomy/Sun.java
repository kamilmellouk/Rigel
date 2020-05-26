package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EclipticCoordinates;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;

import java.util.Objects;

/**
 * Representation of the sun
 *
 * @author Bastien Faivre (310929)
 * @author Kamil Mellouk (312327)
 */
public final class Sun extends CelestialObject {

    // Attributes specific to the Sun
    private final EclipticCoordinates eclipticPos;
    private final float meanAnomaly;

    /**
     * Constructor of a Sun
     *
     * @param eclipticPos   (not null) ecliptic coordinates of the Sun
     * @param equatorialPos (not null) equatorial coordinates of the Sun
     * @param angularSize   (non negative) angular size of the sun
     * @param meanAnomaly   mean anomaly
     */
    public Sun(EclipticCoordinates eclipticPos, EquatorialCoordinates equatorialPos, float angularSize, float meanAnomaly) {
        super("Soleil", equatorialPos, angularSize, -26.7f);

        this.eclipticPos = Objects.requireNonNull(eclipticPos);
        this.meanAnomaly = meanAnomaly;
    }

    /**
     * Getter for the ecliptic coordinates
     *
     * @return the ecliptic coordinates of the sun
     */
    public EclipticCoordinates eclipticPos() {
        return eclipticPos;
    }

    /**
     * Getter for the mean anomaly
     *
     * @return the mean anomaly of the sun
     */
    public float meanAnomaly() {
        return meanAnomaly;
    }
}

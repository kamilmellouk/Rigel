package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EclipticCoordinates;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;

/**
 * @author Bastien Faivre (310929)
 * @author Kamil Mellouk (312327)
 */
public final class Sun extends CelestialObject {

    // Attributes specific to the Sun
    private EclipticCoordinates eclipticPos;
    private float meanAnomaly;

    /**
     * Constructing a Sun
     * @param eclipticPos (not null) ecliptic coordinates of the Sun
     * @param equatorialPos (not null) equatorial coordinates of the Sun
     * @param angularSize (non negative) angular size of the sun
     * @param meanAnomaly mean anomaly
     */
    Sun(EclipticCoordinates eclipticPos, EquatorialCoordinates equatorialPos, float angularSize, float meanAnomaly) {
        super("Soleil", equatorialPos, angularSize, -26.7f);
        if(eclipticPos == null) throw new IllegalArgumentException();
        this.eclipticPos = eclipticPos;
        this.meanAnomaly = meanAnomaly;
    }
}

package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EclipticToEquatorialConversion;

/**
 * Mathematical model for the position of a CelestialObject
 *
 * @author Bastien Faivre (310929)
 * @author Kamil Mellouk (312327)
 * @see SunModel
 * @see MoonModel
 * @see PlanetModel
 */
public interface CelestialObjectModel<O> {

    /**
     * Compute the model of the CelestialObject O
     *
     * @param daysSinceJ2010                 days since Epoch.J2010 (positive or negative)
     * @param eclipticToEquatorialConversion conversion of the object's coordinates
     * @return modeled object O
     */
    O at(double daysSinceJ2010, EclipticToEquatorialConversion eclipticToEquatorialConversion);

}

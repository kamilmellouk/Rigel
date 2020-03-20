package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EclipticToEquatorialConversion;

/**
 * @author Bastien Faivre (310929)
 * @author Kamil Mellouk (312327)
 */

public interface CelestialObjectModel<O> {

    /**
     * Computing the model of the CelestialObject O
     *
     * @param daysSinceJ2010                 days since Epoch.J2010 (positive or negative)
     * @param eclipticToEquatorialConversion conversion of the object's coordinates
     * @return modeled object O
     */
    O at(double daysSinceJ2010, EclipticToEquatorialConversion eclipticToEquatorialConversion);

}

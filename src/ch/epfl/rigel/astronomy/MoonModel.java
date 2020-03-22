package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EclipticToEquatorialConversion;
import ch.epfl.rigel.math.Angle;

/**
 * @author Bastien Faivre (310929)
 * @author Kamil Mellouk (312327)
 */
public enum MoonModel implements CelestialObjectModel<Moon> {
    MOON();

    // Lunar constants for the Epoch J2010
    private final double meanLon = Angle.ofDeg(91.929336);
    private final double perigMeanLon = Angle.ofDeg(130.143076);
    private final double ascKnotLon = Angle.ofDeg(291.682547);
    private final double orbitalIncl = Angle.ofDeg(5.145396);
    private final double orbitalExc = 0.0549;


    @Override
    public Moon at(double daysSinceJ2010, EclipticToEquatorialConversion eclipticToEquatorialConversion) {
        // TODO finish implementing
        Sun currentSun = SunModel.SUN.at(daysSinceJ2010, eclipticToEquatorialConversion);

        double orbitalLon = Angle.ofDeg(13.1763966) * daysSinceJ2010 + meanLon;

        double meanMoonAnomaly = orbitalLon - Angle.ofDeg(0.1114041) * daysSinceJ2010 - perigMeanLon;

        // Correction terms for the Sun's influence on the Moon's trajectory
        ///double evection = Angle.ofDeg(1.2739) * Math.sin(2*(orbitalLon - SunModel.));

       /* return new Moon(
                ,
                ,
                0,

        );*/
       return null;
    }
}

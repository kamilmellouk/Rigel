package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EclipticCoordinates;
import ch.epfl.rigel.coordinates.EclipticToEquatorialConversion;
import ch.epfl.rigel.math.Angle;

/**
 * Mathematical model for the position of the Moon
 *
 * @author Bastien Faivre (310929)
 * @author Kamil Mellouk (312327)
 */
public enum MoonModel implements CelestialObjectModel<Moon> {
    MOON();

    // Lunar constants relative to the Epoch J2010 (could be declared public, but only used locally so left private)
    private static final double MEAN_LON = Angle.ofDeg(91.929336);
    private static final double MEAN_LON_AT_PERIGEE = Angle.ofDeg(130.143076);
    private static final double ASCENDING_NODE_LON = Angle.ofDeg(291.682547);
    private static final double ORBIT_INCLINATION = Angle.ofDeg(5.145396);
    private static final double ORBIT_ECCENTRICITY = 0.0549;

    /**
     * Compute the model of the moon
     *
     * @param daysSinceJ2010                 days since Epoch.J2010 (positive or negative)
     * @param eclipticToEquatorialConversion conversion of the object's coordinates
     * @return the moon model
     */
    @Override
    public Moon at(double daysSinceJ2010, EclipticToEquatorialConversion eclipticToEquatorialConversion) {
        Sun currentSun = SunModel.SUN.at(daysSinceJ2010, eclipticToEquatorialConversion);
        double sinOfSunMeanAnomaly = Math.sin(currentSun.meanAnomaly());
        double sunGeoEclLon = currentSun.eclipticPos().lon();

        double meanOrbitalLon = Angle.ofDeg(13.1763966) * daysSinceJ2010 + MEAN_LON;

        double meanAnomaly = meanOrbitalLon - Angle.ofDeg(0.1114041) * daysSinceJ2010 - MEAN_LON_AT_PERIGEE;

        double evection = Angle.ofDeg(1.2739) * Math.sin(2 * (meanOrbitalLon - sunGeoEclLon) - meanAnomaly);
        double annualEquationCorrection = Angle.ofDeg(0.1858) * sinOfSunMeanAnomaly;
        double correction3 = Angle.ofDeg(0.37) * sinOfSunMeanAnomaly;

        double correctedAnomaly = meanAnomaly + evection - annualEquationCorrection - correction3;

        double centralEquationCorrection = Angle.ofDeg(6.2886) * Math.sin(correctedAnomaly);
        double correction4 = Angle.ofDeg(0.214) * Math.sin(2 * correctedAnomaly);

        double correctedOrbitalLon = meanOrbitalLon + evection + centralEquationCorrection - annualEquationCorrection + correction4;

        double variation = Angle.ofDeg(0.6583) * Math.sin(2 * (correctedOrbitalLon - sunGeoEclLon));

        double realOrbitalLon = correctedOrbitalLon + variation;

        double ascendingNodeMeanLon = ASCENDING_NODE_LON - Angle.ofDeg(0.0529539) * daysSinceJ2010;
        double ascendingNodeCorrectedLon = ascendingNodeMeanLon - Angle.ofDeg(0.16) * sinOfSunMeanAnomaly;

        // compute an intermediate value for performances
        double intermediateValue = Math.sin(realOrbitalLon - ascendingNodeCorrectedLon);

        double eclipticLon = Angle.normalizePositive(Math.atan2(intermediateValue * Math.cos(ORBIT_INCLINATION), Math.cos(realOrbitalLon - ascendingNodeCorrectedLon)) + ascendingNodeCorrectedLon);
        double eclipticLat = Math.asin(intermediateValue * Math.sin(ORBIT_INCLINATION));
        EclipticCoordinates eclipticCoordinates = EclipticCoordinates.of(eclipticLon, eclipticLat);

        double phase = (1 - Math.cos(realOrbitalLon - sunGeoEclLon)) / 2;

        double distanceEarthMoon = (1 - ORBIT_ECCENTRICITY * ORBIT_ECCENTRICITY) / (1 + ORBIT_ECCENTRICITY * Math.cos(correctedAnomaly + centralEquationCorrection));
        double angularSize = Angle.ofDeg(0.5181) / distanceEarthMoon;

        return new Moon(
                eclipticToEquatorialConversion.apply(eclipticCoordinates),
                (float) angularSize,
                0,
                (float) phase
        );

    }
}

package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EclipticCoordinates;
import ch.epfl.rigel.coordinates.EclipticToEquatorialConversion;
import ch.epfl.rigel.math.Angle;

/**
 * @author Bastien Faivre (310929)
 * @author Kamil Mellouk (312327)
 */

public enum MoonModel implements CelestialObjectModel<Moon> {
    MOON();

    // Lunar constants for the Epoch J2010
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
        // get the mean anomaly (especially its sinus) and the geocentric ecliptic longitude of the current sun
        Sun currentSun = SunModel.SUN.at(daysSinceJ2010, eclipticToEquatorialConversion);
        double sinOfSunMeanAnomaly = Math.sin(currentSun.meanAnomaly());
        double sunGeoEclLon = currentSun.eclipticPos().lon();

        // compute the mean orbital longitude
        double meanOrbitalLon = Angle.ofDeg(13.1763966) * daysSinceJ2010 + MEAN_LON;

        // compute the mean anomaly
        double meanAnomaly = meanOrbitalLon - Angle.ofDeg(0.1114041) * daysSinceJ2010 - MEAN_LON_AT_PERIGEE;

        // compute the correction terms
        double evection = Angle.ofDeg(1.2739) * Math.sin(2 * (meanOrbitalLon - sunGeoEclLon) - meanAnomaly);
        double annualEquationCorrection = Angle.ofDeg(0.1858) * sinOfSunMeanAnomaly;
        double correction3 = Angle.ofDeg(0.37) * sinOfSunMeanAnomaly;

        // compute the corrected anomaly
        double correctedAnomaly = meanAnomaly + evection - annualEquationCorrection - correction3;

        // compute 2 another correction terms
        double centralEquationCorrection = Angle.ofDeg(6.2886) * Math.sin(correctedAnomaly);
        double correction4 = Angle.ofDeg(0.214) * Math.sin(2 * correctedAnomaly);

        // compute the corrected orbital longitude
        double correctedOrbitalLon = meanOrbitalLon + evection + centralEquationCorrection - annualEquationCorrection + correction4;

        // compute the variation
        double variation = Angle.ofDeg(0.6583) * Math.sin(2 * (correctedOrbitalLon - sunGeoEclLon));

        // compute the real orbital longitude
        double realOrbitalLon = correctedOrbitalLon + variation;

        // compute the mean longitude and the corrected longitude of the ascending node
        double ascendingNodeMeanLon = ASCENDING_NODE_LON - Angle.ofDeg(0.0529539) * daysSinceJ2010;
        double ascendingNodeCorrectedLon = ascendingNodeMeanLon - Angle.ofDeg(0.16) * sinOfSunMeanAnomaly;

        // compute an intermediate value for performances
        double intermediateValue = Math.sin(realOrbitalLon - ascendingNodeCorrectedLon);

        // compute the ecliptic longitude and latitude of the moon
        double eclipticLon = Angle.normalizePositive(Math.atan2(intermediateValue * Math.cos(ORBIT_INCLINATION), Math.cos(realOrbitalLon - ascendingNodeCorrectedLon)) + ascendingNodeCorrectedLon);
        double eclipticLat = Math.asin(intermediateValue * Math.sin(ORBIT_INCLINATION));
        // compute the ecliptic coordinates of the moon
        EclipticCoordinates eclipticCoordinates = EclipticCoordinates.of(eclipticLon, eclipticLat);

        // compute the phase of the moon
        double phase = (1 - Math.cos(realOrbitalLon - sunGeoEclLon)) / 2;

        // compute the distance earth-moon to compute the angular size of the moon
        double distanceEarthMoon = (1 - ORBIT_ECCENTRICITY * ORBIT_ECCENTRICITY) / (1 + ORBIT_ECCENTRICITY * Math.cos(correctedAnomaly + centralEquationCorrection));
        double angularSize = Angle.ofDeg(0.5181) / distanceEarthMoon;

        // return the computed moon model
        return new Moon(
                eclipticToEquatorialConversion.apply(eclipticCoordinates),
                (float) angularSize,
                0,
                (float) phase
        );

    }
}

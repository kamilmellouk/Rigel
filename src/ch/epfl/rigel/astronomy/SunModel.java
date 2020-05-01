package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EclipticCoordinates;
import ch.epfl.rigel.coordinates.EclipticToEquatorialConversion;
import ch.epfl.rigel.math.Angle;

/**
 * Mathematical model for the position of the sun
 *
 * @author Bastien Faivre (310929)
 * @author Kamil Mellouk (312327)
 */
public enum SunModel implements CelestialObjectModel<Sun> {
    SUN();

    // The longitude of the sun at J2010 (could be declared public, but only used locally so left private)
    private static final double SUN_LON_AT_J2010 = Angle.ofDeg(279.557208);
    // The longitude of the sun at perigee (could be declared public, but only used locally so left private)
    private static final double SUN_LON_AT_PERIGEE = Angle.ofDeg(283.112438);
    // The eccentricity of the orbit sun-earth (could be declared public, but only used locally so left private)
    private static final double SUN_EARTH_ECCENTRICITY = 0.016705;
    private static final double SUN_EARTH_ECCENTRICITY_SQUARED = SUN_EARTH_ECCENTRICITY * SUN_EARTH_ECCENTRICITY;
    // The theta0 angle (could be declared public, but only used locally so left private)
    private static final double THETA_0 = Angle.ofDeg(0.533128);
    // The mean earth rotating speed around the sun (could be declared public, but only used locally so left private)
    private static final double EARTH_ROTATION_SPEED = Angle.TAU / 365.242191;

    /**
     * Compute the model of the sun
     *
     * @param daysSinceJ2010                 days since Epoch.J2010 (positive or negative)
     * @param eclipticToEquatorialConversion conversion of the object's coordinates
     * @return the sun model
     */
    @Override
    public Sun at(double daysSinceJ2010, EclipticToEquatorialConversion eclipticToEquatorialConversion) {
        double sunMeanAnomaly = EARTH_ROTATION_SPEED * daysSinceJ2010 + SUN_LON_AT_J2010 - SUN_LON_AT_PERIGEE;
        double sunRealAnomaly = sunMeanAnomaly + 2 * SUN_EARTH_ECCENTRICITY * Math.sin(sunMeanAnomaly);
        double sunGeocentricEclipticLon = Angle.normalizePositive(sunRealAnomaly + SUN_LON_AT_PERIGEE);
        EclipticCoordinates sunEclipticCoordinates = EclipticCoordinates.of(sunGeocentricEclipticLon, 0);

        System.out.println("pos recomputed");
        return new Sun(
                sunEclipticCoordinates,
                eclipticToEquatorialConversion.apply(sunEclipticCoordinates),
                (float) (THETA_0 * ((1 + SUN_EARTH_ECCENTRICITY * Math.cos(sunRealAnomaly)) / (1 - SUN_EARTH_ECCENTRICITY_SQUARED))),
                (float) sunMeanAnomaly
        );
    }

}

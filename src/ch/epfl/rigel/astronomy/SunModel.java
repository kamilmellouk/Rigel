package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EclipticCoordinates;
import ch.epfl.rigel.coordinates.EclipticToEquatorialConversion;
import ch.epfl.rigel.math.Angle;

/**
 * @author Bastien Faivre (310929)
 * @author Kamil Mellouk (312327)
 */

public enum SunModel implements CelestialObjectModel<Sun> {

    SUN();

    // the longitude of the sun at J2010
    private static final double SUN_LON_AT_J2010 = Angle.ofDeg(279.557208);
    // the longitude of the sun at perigee
    private static final double SUN_LON_AT_PERIGEE = Angle.ofDeg(283.112438);
    // the eccentricity of the orbit sun-earth
    private static final double SUN_EARTH_ECCENTRICITY = 0.016705;
    // the theta0 angle
    private static final double THETA_0 = Angle.ofDeg(0.533128);

    /**
     * Compute the model of the sun
     *
     * @param daysSinceJ2010                 days since Epoch.J2010 (positive or negative)
     * @param eclipticToEquatorialConversion conversion of the object's coordinates
     * @return the sun model
     */
    @Override
    public Sun at(double daysSinceJ2010, EclipticToEquatorialConversion eclipticToEquatorialConversion) {
        // the mean anomaly of the sun
        double sunMeanAnomaly = Angle.normalizePositive(Angle.TAU / 365.242191) * daysSinceJ2010 + SUN_LON_AT_J2010 - SUN_LON_AT_PERIGEE;
        // the real anomaly of the sun
        double sunRealAnomaly = sunMeanAnomaly + 2 * SUN_EARTH_ECCENTRICITY * Math.sin(sunMeanAnomaly);
        // the geocentric ecliptic longitude of the sun
        double sunGeoEclLon = sunRealAnomaly + SUN_LON_AT_PERIGEE;
        // the geocentric ecliptic latitude of the sun
        double sunGeoEclLat = 0;

        return new Sun(
                EclipticCoordinates.of(sunGeoEclLon, sunGeoEclLat),
                eclipticToEquatorialConversion.apply(EclipticCoordinates.of(sunGeoEclLon, sunGeoEclLat)),
                (float) (THETA_0 * ((1 + SUN_EARTH_ECCENTRICITY * Math.cos(sunRealAnomaly)) / (1 - SUN_EARTH_ECCENTRICITY * SUN_EARTH_ECCENTRICITY))),
                (float) sunMeanAnomaly
        );
    }
}

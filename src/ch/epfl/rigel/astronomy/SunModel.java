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

    private final double sunLonAtJ2010 = Angle.ofDeg(279.557208);
    private final double sunLonAtPerig = Angle.ofDeg(283.112438);
    private final double sunEarthExcentricity = 0.016705;

    private double daysSinceJ2010;
    private double sunMeanAnomaly;
    private double sunRealAnomaly;
    private double sunGeoEclLon;
    private double sunGeoEclLat;

    private final double theta0 = Angle.ofDeg(0.0533128);

    @Override
    public Sun at(double daysSinceJ2010, EclipticToEquatorialConversion eclipticToEquatorialConversion) {
        this.daysSinceJ2010 = daysSinceJ2010;
        sunMeanAnomaly = Angle.normalizePositive(Angle.TAU/365.242191)*this.daysSinceJ2010 + sunLonAtJ2010 - sunLonAtPerig;
        sunRealAnomaly = sunMeanAnomaly + 2*sunEarthExcentricity*Math.sin(sunMeanAnomaly);
        sunGeoEclLon = sunRealAnomaly + sunLonAtPerig;
        sunGeoEclLat = 0;

        return new Sun(
                EclipticCoordinates.of(sunGeoEclLon, sunGeoEclLat),
                eclipticToEquatorialConversion.apply(EclipticCoordinates.of(sunGeoEclLon, sunGeoEclLat)),
                (float) (theta0*((1+sunEarthExcentricity*Math.cos(sunRealAnomaly))/(1-sunEarthExcentricity*sunEarthExcentricity))),
                (float) sunMeanAnomaly
                );
    }
}

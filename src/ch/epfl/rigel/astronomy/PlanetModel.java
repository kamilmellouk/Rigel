package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EclipticCoordinates;
import ch.epfl.rigel.coordinates.EclipticToEquatorialConversion;
import ch.epfl.rigel.math.Angle;

import java.util.List;

/**
 * @author Bastien Faivre
 * 20/03/2020
 */
public enum PlanetModel implements CelestialObjectModel<Planet> {

    MERCURY("Mercure", 0.24085, 75.5671, 77.612, 0.205627,
            0.387098, 7.0051, 48.449, 6.74, -0.42),
    VENUS("Vénus", 0.615207, 272.30044, 131.54, 0.006812,
            0.723329, 3.3947, 76.769, 16.92, -4.40),
    EARTH("Terre", 0.999996, 99.556772, 103.2055, 0.016671,
            0.999985, 0, 0, 0, 0),
    MARS("Mars", 1.880765, 109.09646, 336.217, 0.093348,
            1.523689, 1.8497, 49.632, 9.36, -1.52),
    JUPITER("Jupiter", 11.857911, 337.917132, 14.6633, 0.048907,
            5.20278, 1.3035, 100.595, 196.74, -9.40),
    SATURN("Saturne", 29.310579, 172.398316, 89.567, 0.053853,
            9.51134, 2.4873, 113.752, 165.60, -8.88),
    URANUS("Uranus", 84.039492, 271.063148, 172.884833, 0.046321,
            19.21814, 0.773059, 73.926961, 65.80, -7.19),
    NEPTUNE("Neptune", 165.84539, 326.895127, 23.07, 0.010483,
            30.1985, 1.7673, 131.879, 62.20, -6.87);

    public static List<PlanetModel> ALL = List.of(PlanetModel.values());

    private final String name;
    private final double revolutionPeriod;
    private final double lonAtJ2010;
    private final double lonAtPerigee;
    private final double orbitEccentricity;
    private final double halfAxisOrbit;
    private final double orbitInclinationAtEcliptic;
    private final double ascendingNodeLon;
    private final float angularSize;
    private final float magnitude;

    PlanetModel(String name, double revolutionPeriod, double lonAtJ2010, double lonAtPerigee, double orbitEccentricity, double halfAxisOrbit, double orbitInclinationAtEcliptic, double ascendingNodeLon, double angularSize, double magnitude) {
        this.name = name;
        this.revolutionPeriod = revolutionPeriod;
        this.lonAtJ2010 = Angle.ofDeg(lonAtJ2010);
        this.lonAtPerigee = Angle.ofDeg(lonAtPerigee);
        this.orbitEccentricity = orbitEccentricity;
        this.halfAxisOrbit = halfAxisOrbit;
        this.orbitInclinationAtEcliptic = Angle.ofDeg(orbitInclinationAtEcliptic);
        this.ascendingNodeLon = Angle.ofDeg(ascendingNodeLon);
        this.angularSize = (float) angularSize;
        this.magnitude = (float) magnitude;
    }

    @Override
    public Planet at(double daysSinceJ2010, EclipticToEquatorialConversion eclipticToEquatorialConversion) {
        // compute the mean anomaly of the planet
        double planetMeanAnomaly = (Angle.TAU / 365.242191) * (daysSinceJ2010 / revolutionPeriod) + lonAtJ2010 - lonAtPerigee;
        // compute the real anomaly of the planet
        double planetRealAnomaly = planetMeanAnomaly + 2 * orbitEccentricity * Math.sin(planetMeanAnomaly);
        // compute the radius of the planet on the orbit plan
        double radius = (halfAxisOrbit * (1 - orbitEccentricity * orbitEccentricity)) / (1 + orbitEccentricity * Math.cos(planetRealAnomaly));
        // compute the longitude of the planet on the orbit plan
        double longitude = planetRealAnomaly + lonAtPerigee;

        // compute an intermediate value for the performances
        double intermediateValue = Math.sin(longitude - ascendingNodeLon);

        // compute the heliocentric ecliptic latitude of the planet
        double heliocentricEclipticLat = Math.asin(intermediateValue * Math.sin(orbitInclinationAtEcliptic));
        // project the radius on the ecliptic plan
        double projectedRadius = radius * Math.cos(heliocentricEclipticLat);
        // project the longitude on the ecliptic plan
        double projectedLongitude = Math.atan2(intermediateValue * Math.cos(orbitInclinationAtEcliptic), Math.cos(longitude - ascendingNodeLon)) + ascendingNodeLon;

        // compute an intermediate value for the performances
        double intermediateValue2 = radius * Math.sin(projectedLongitude - longitude);

        // TODO: 20/03/2020 L and R are for the earth 
        /*
        // compute the geocentric ecliptic longitude of the planet depending on its type
        double geoEclLon;
        if (name.equals("Mercure") || name.equals("Vénus")) {
            geoEclLon = Math.PI + longitude + Math.atan2(projectedRadius * Math.sin(longitude - projectedLongitude), radius - projectedRadius * Math.cos(longitude - projectedLongitude));
        } else {
            geoEclLon = projectedLongitude + Math.atan2(intermediateValue2, projectedRadius - radius * Math.cos(projectedLongitude - longitude));
        }

        // compute the geocentric ecliptic latitude of the planet
        double geoEclLat = Math.atan2(projectedRadius * Math.tan(heliocentricEclipticLat) * Math.sin(geoEclLon - projectedLongitude), intermediateValue2);

        double distancePlanetEarth = Math.sqrt(radius * radius);

        float planetAngularSize = (float) (angularSize / distancePlanetEarth);

         */
        EclipticCoordinates geoEclCoordinates = EclipticCoordinates.of(0, 0);

        return new Planet(
                name,
                eclipticToEquatorialConversion.apply(geoEclCoordinates),
                planetAngularSize,
                magnitude);
    }
}

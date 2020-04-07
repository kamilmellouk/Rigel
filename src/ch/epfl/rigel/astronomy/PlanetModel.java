package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EclipticCoordinates;
import ch.epfl.rigel.coordinates.EclipticToEquatorialConversion;
import ch.epfl.rigel.math.Angle;

import java.util.List;

/**
 * @author Bastien Faivre (310929)
 * @author Kamil Mellouk (312327)
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

    // List containing the model of all planets
    public static List<PlanetModel> ALL = List.copyOf(List.of(PlanetModel.values()));

    // Attributes of a planet
    private final String name;
    private final double revolutionPeriod;
    private final double lonAtJ2010;
    private final double lonAtPerigee;
    private final double orbitEccentricity;
    private final double halfAxisOrbit;
    private final double orbitInclinationAtEcliptic;
    private final double ascendingNodeLon;
    private final double angularSizeAtOneAU;
    private final double magnitudeAtOneAU;

    /**
     * Constructor of a planet model
     *
     * @param name                       the name of the planet
     * @param revolutionPeriod           the revolution period
     * @param lonAtJ2010                 the longitude at J2010
     * @param lonAtPerigee               the longitude at perigee
     * @param orbitEccentricity          the orbit eccentricity
     * @param halfAxisOrbit              the half big axis of orbit
     * @param orbitInclinationAtEcliptic the orbit inclination at ecliptic
     * @param ascendingNodeLon           the longitude of the ascending node
     * @param angularSizeAtOneAU         the angular size
     * @param magnitudeAtOneAU           the magnitude
     */
    PlanetModel(String name, double revolutionPeriod, double lonAtJ2010, double lonAtPerigee, double orbitEccentricity, double halfAxisOrbit, double orbitInclinationAtEcliptic, double ascendingNodeLon, double angularSizeAtOneAU, double magnitudeAtOneAU) {
        this.name = name;
        this.revolutionPeriod = revolutionPeriod;
        this.lonAtJ2010 = Angle.ofDeg(lonAtJ2010);
        this.lonAtPerigee = Angle.ofDeg(lonAtPerigee);
        this.orbitEccentricity = orbitEccentricity;
        this.halfAxisOrbit = halfAxisOrbit;
        this.orbitInclinationAtEcliptic = Angle.ofDeg(orbitInclinationAtEcliptic);
        this.ascendingNodeLon = Angle.ofDeg(ascendingNodeLon);
        this.angularSizeAtOneAU = Angle.ofArcsec(angularSizeAtOneAU);
        this.magnitudeAtOneAU = magnitudeAtOneAU;
    }

    /**
     * Compute the model of the planet
     *
     * @param daysSinceJ2010                 days since Epoch.J2010 (positive or negative)
     * @param eclipticToEquatorialConversion conversion of the object's coordinates
     * @return the planet model
     */
    @Override
    public Planet at(double daysSinceJ2010, EclipticToEquatorialConversion eclipticToEquatorialConversion) {
        /*
        compute :
            mean anomaly
            real anomaly
            distance to sun
            heliocentric longitude
         of the planet
         */
        double meanAnomaly = Angle.TAU / 365.242191 * (daysSinceJ2010 / revolutionPeriod) + lonAtJ2010 - lonAtPerigee;
        double realAnomaly = meanAnomaly + 2 * orbitEccentricity * Math.sin(meanAnomaly);
        double distanceToSun = (halfAxisOrbit * (1 - orbitEccentricity * orbitEccentricity)) / (1 + orbitEccentricity * Math.cos(realAnomaly));
        double heliocentricLon = realAnomaly + lonAtPerigee;

        // compute an intermediate value for the performances
        double firstIntermediateValue = Math.sin(heliocentricLon - ascendingNodeLon);

        // compute the heliocentric ecliptic latitude
        double heliocentricEclipticLat = Math.asin(firstIntermediateValue * Math.sin(orbitInclinationAtEcliptic));
        // compute an intermediate value for performances
        double cosHelEclLat = Math.cos(heliocentricEclipticLat);

        // project the radius and the longitude on the ecliptic plan
        double projectedRadiusOnEcliptic = distanceToSun * cosHelEclLat;
        double heliocentricEclipticLon = Math.atan2(firstIntermediateValue * Math.cos(orbitInclinationAtEcliptic), Math.cos(heliocentricLon - ascendingNodeLon)) + ascendingNodeLon;

        /*
        compute :
            mean anomaly
            real anomaly
            distance to sun
            heliocentric longitude
         of the earth
         */
        double earthMeanAnomaly = (Angle.TAU / 365.242191) * (daysSinceJ2010 / PlanetModel.EARTH.revolutionPeriod) + PlanetModel.EARTH.lonAtJ2010 - PlanetModel.EARTH.lonAtPerigee;
        double earthRealAnomaly = earthMeanAnomaly + 2 * PlanetModel.EARTH.orbitEccentricity * Math.sin(earthMeanAnomaly);
        double distanceEarthSun = (PlanetModel.EARTH.halfAxisOrbit * (1 - PlanetModel.EARTH.orbitEccentricity * PlanetModel.EARTH.orbitEccentricity)) / (1 + PlanetModel.EARTH.orbitEccentricity * Math.cos(earthRealAnomaly));
        double earthHeliocentricLon = earthRealAnomaly + PlanetModel.EARTH.lonAtPerigee;

        // compute an intermediate value for performances
        double secondIntermediateValue = distanceEarthSun * Math.sin(heliocentricEclipticLon - earthHeliocentricLon);

        // compute the geocentric ecliptic longitude of the planet depending on its type
        double geocentricEclipticLon;
        if (this.ordinal() <= 1) {
            geocentricEclipticLon = Math.PI + earthHeliocentricLon + Math.atan2(Math.sin(earthHeliocentricLon - heliocentricEclipticLon) * projectedRadiusOnEcliptic, distanceEarthSun - projectedRadiusOnEcliptic * Math.cos(earthHeliocentricLon - heliocentricEclipticLon));
        } else {
            geocentricEclipticLon = heliocentricEclipticLon + Math.atan2(secondIntermediateValue, projectedRadiusOnEcliptic - distanceEarthSun * Math.cos(heliocentricEclipticLon - earthHeliocentricLon));
        }
        // compute the geocentric ecliptic latitude of the planet
        double geocentricEclipticLat = Math.atan((projectedRadiusOnEcliptic * Math.tan(heliocentricEclipticLat) * Math.sin(geocentricEclipticLon - heliocentricEclipticLon)) / secondIntermediateValue);
        // compute the geocentric ecliptic coordinates of the planet
        EclipticCoordinates geocentricEclipticCoordinates = EclipticCoordinates.of(Angle.normalizePositive(geocentricEclipticLon), geocentricEclipticLat);

        // compute the distance between the planet and the earth
        double distanceEarthPlanet = Math.sqrt(distanceEarthSun * distanceEarthSun + distanceToSun * distanceToSun - 2 * distanceToSun * distanceEarthSun * Math.cos(heliocentricLon - earthHeliocentricLon) * cosHelEclLat);
        // compute the angular size of the planet
        double angularSize = angularSizeAtOneAU / distanceEarthPlanet;

        // compute the phase of the planet
        double phase = (1 + Math.cos(geocentricEclipticLon - heliocentricLon)) / 2;
        // compute the magnitude of the planet
        double magnitude = magnitudeAtOneAU + 5 * Math.log10(distanceToSun * distanceEarthPlanet / Math.sqrt(phase));

        // return the computed planet model
        return new Planet(
                name,
                eclipticToEquatorialConversion.apply(geocentricEclipticCoordinates),
                (float) angularSize,
                (float) magnitude);
    }

}

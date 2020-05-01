package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EclipticCoordinates;
import ch.epfl.rigel.coordinates.EclipticToEquatorialConversion;
import ch.epfl.rigel.math.Angle;

import java.util.List;

/**
 * Mathematical model for the position of a planet
 *
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
    URANUS("Uranus", 84.039492, 356.135400, 172.884833, 0.046321,
            19.21814, 0.773059, 73.926961, 65.80, -7.19),
    NEPTUNE("Neptune", 165.84539, 326.895127, 23.07, 0.010483,
            30.1985, 1.7673, 131.879, 62.20, -6.87);

    // List containing the model of all planets
    public static final List<PlanetModel> ALL = List.of(PlanetModel.values());

    private static final double TAU_OVER_DAYS_PER_YEAR = Angle.TAU / 365.242191;

    // Attributes of a planet (could be declared public, but only used locally so left private)
    private final String name;
    private final double revolutionPeriod;
    private final double lonAtJ2010;
    private final double lonAtPerigee;
    private final double orbitEccentricity;
    private final double halfAxisOrbit;
    private final double orbitInclinationAtEcliptic;
    //private final double sinOrbitInclinationAtEcliptic;
    //private final double cosOrbitInclinationAtEcliptic;
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
        //this.sinOrbitInclinationAtEcliptic = Math.sin(orbitInclinationAtEcliptic);
        //this.cosOrbitInclinationAtEcliptic = Math.cos(orbitInclinationAtEcliptic);
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
        double realAnomaly = realAnomaly(this, daysSinceJ2010);
        double distanceToSun = (halfAxisOrbit * (1 - orbitEccentricity * orbitEccentricity)) / (1 + orbitEccentricity * Math.cos(realAnomaly));
        double heliocentricLon = realAnomaly + lonAtPerigee;

        // compute an intermediate value for the performances
        double firstIntermediateValue = Math.sin(heliocentricLon - ascendingNodeLon);

        double heliocentricEclipticLat = Math.asin(firstIntermediateValue * Math.sin(orbitInclinationAtEcliptic));
        // compute an intermediate value for performances
        double cosHelEclLat = Math.cos(heliocentricEclipticLat);

        double projectedRadiusOnEcliptic = distanceToSun * cosHelEclLat;
        double heliocentricEclipticLon = Math.atan2(firstIntermediateValue * Math.cos(orbitInclinationAtEcliptic), Math.cos(heliocentricLon - ascendingNodeLon)) + ascendingNodeLon;

        double earthRealAnomaly = realAnomaly(PlanetModel.EARTH, daysSinceJ2010);
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

        double geocentricEclipticLat = Math.atan((projectedRadiusOnEcliptic * Math.tan(heliocentricEclipticLat) * Math.sin(geocentricEclipticLon - heliocentricEclipticLon)) / secondIntermediateValue);
        EclipticCoordinates geocentricEclipticCoordinates = EclipticCoordinates.of(Angle.normalizePositive(geocentricEclipticLon), geocentricEclipticLat);

        double distanceEarthPlanet = Math.sqrt(distanceEarthSun * distanceEarthSun + distanceToSun * distanceToSun - 2 * distanceToSun * distanceEarthSun * Math.cos(heliocentricLon - earthHeliocentricLon) * cosHelEclLat);
        double angularSize = angularSizeAtOneAU / distanceEarthPlanet;
        double phase = (1 + Math.cos(geocentricEclipticLon - heliocentricLon)) / 2;
        double magnitude = magnitudeAtOneAU + 5 * Math.log10(distanceToSun * distanceEarthPlanet / Math.sqrt(phase));

        return new Planet(
                name,
                eclipticToEquatorialConversion.apply(geocentricEclipticCoordinates),
                (float) angularSize,
                (float) magnitude);
    }

    /**
     * Compute the real anomaly of the given planet
     *
     * @param planetModel    the given planet
     * @param daysSinceJ2010 the number of days since J2010
     * @return the real anomaly of the given planet
     */
    private double realAnomaly(PlanetModel planetModel, double daysSinceJ2010) {
        double meanAnomaly = TAU_OVER_DAYS_PER_YEAR * (daysSinceJ2010 / planetModel.revolutionPeriod) + planetModel.lonAtJ2010 - planetModel.lonAtPerigee;
        return meanAnomaly + 2 * planetModel.orbitEccentricity * Math.sin(meanAnomaly);
    }

}

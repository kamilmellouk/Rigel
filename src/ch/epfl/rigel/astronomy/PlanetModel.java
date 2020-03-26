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

    public static List<PlanetModel> ALL = List.copyOf(List.of(PlanetModel.values()));

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
        /*
        compute :
            mean anomaly M
            real anomaly v
            radius r
            longitude l
        of the planet
         */
        double M = Angle.normalizePositive((Angle.TAU / 365.242191) * (daysSinceJ2010 / revolutionPeriod) + lonAtJ2010 - lonAtPerigee);
        double v = Angle.normalizePositive(M + 2 * orbitEccentricity * Math.sin(M));
        double r = (halfAxisOrbit * (1 - orbitEccentricity * orbitEccentricity)) / (1 + orbitEccentricity * Math.cos(v));
        double l = Angle.normalizePositive(v + lonAtPerigee);

        System.out.println("D = " + daysSinceJ2010);
        System.out.println("M = " + Angle.toDeg(M));
        System.out.println("v = " + Angle.toDeg(v));
        System.out.println("r = " + r);
        System.out.println("l = " + Angle.toDeg(l));

        // compute an intermediate value for the performances
        double sin_l_minus_omega = Math.sin(l - ascendingNodeLon);

        // compute the heliocentric ecliptic latitude
        double phi = Angle.normalizePositive(Math.asin(sin_l_minus_omega * Math.sin(orbitInclinationAtEcliptic)));
        // compute an intermediate value for performances
        double cosPhi = Math.cos(phi);

        System.out.println("phi = " + Angle.toDeg(phi));

        // project the radius and the longitude on the ecliptic plan
        double rPrime = r * cosPhi;
        double lPrime = Angle.normalizePositive(Math.atan2(sin_l_minus_omega * Math.cos(orbitInclinationAtEcliptic), Math.cos(l - ascendingNodeLon)) + ascendingNodeLon);

        System.out.println("rPrime = " + rPrime);
        System.out.println("lPrime = " + Angle.toDeg(lPrime));

        /*
        compute :
            mean anomaly M
            real anomaly v
            radius r
            longitude l
        of the earth
         */
        double M_earth = Angle.normalizePositive((Angle.TAU / 365.242191) * (daysSinceJ2010 / PlanetModel.EARTH.revolutionPeriod) + PlanetModel.EARTH.lonAtJ2010 - PlanetModel.EARTH.lonAtPerigee);
        double v_earth = Angle.normalizePositive(M_earth + 2 * PlanetModel.EARTH.orbitEccentricity * Math.sin(M_earth));
        double R = (PlanetModel.EARTH.halfAxisOrbit * (1 - PlanetModel.EARTH.orbitEccentricity * PlanetModel.EARTH.orbitEccentricity)) / (1 + PlanetModel.EARTH.orbitEccentricity * Math.cos(v_earth));
        double L = Angle.normalizePositive(v_earth + PlanetModel.EARTH.lonAtPerigee);

        System.out.println("M_earth = " + Angle.toDeg(M_earth));
        System.out.println("v_earth = " + Angle.toDeg(v_earth));
        System.out.println("R = " + Angle.toDeg(R));
        System.out.println("L = " + L);

        // compute an intermediate value for performances
        double R_sin_lPrime_minus_L = R * Math.sin(lPrime - L);

        // compute the geocentric ecliptic longitude of the planet depending on its type
        double lambda;
        if (name.equals("Mercure") || name.equals("Vénus")) {
            lambda = Math.PI + L + Math.atan2(Math.sin(L - lPrime) * rPrime, R - rPrime * Math.cos(L - lPrime));
        } else {
            lambda = lPrime + Math.atan2(R_sin_lPrime_minus_L, rPrime - R * Math.cos(lPrime - L));
        }
        // compute the geocentric ecliptic latitude of the planet
        double beta = Math.atan((rPrime * Math.tan(phi) * Math.sin(lambda - lPrime)) / R_sin_lPrime_minus_L);
        // compute the geocentric ecliptic coordinates of the planet
        System.out.println("lambda = " + Angle.toDeg(lambda));
        System.out.println("beta no reduce = " + Angle.toDeg(Math.atan((rPrime * Math.tan(phi) * Math.sin(lambda - lPrime)) / R_sin_lPrime_minus_L)));
        System.out.println("beta = " + Angle.toDeg(beta));
        EclipticCoordinates geoEclCoordinates = EclipticCoordinates.of(Angle.normalizePositive(lambda), beta);


        // compute the between the planet and the earth
        double rho = Math.sqrt(R * R + r * r - 2 * R * Math.cos(l - L) * cosPhi);
        // compute the angular size of the planet
        float theta = (float) Angle.normalizePositive(angularSize / rho);

        System.out.println("rho = " + rho);
        System.out.println("theta = " + theta);

        // compute the phase of the planet
        double F = (1 + Math.cos(lambda - l)) / 2;
        // compute the magnitude of the planet
        float m = (float) (magnitude + 5 * Math.log10((r * rho) / Math.sqrt(F)));

        System.out.println("F = " + F);
        System.out.println("m = " + m);

        // return the computed planet model
        return new Planet(
                name,
                eclipticToEquatorialConversion.apply(geoEclCoordinates),
                theta,
                m);
    }

}

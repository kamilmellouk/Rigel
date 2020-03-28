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
    private static final double meanLon = Angle.ofDeg(91.929336);
    private static final double meanLonAtPerigee = Angle.ofDeg(130.143076);
    private static final double ascendingNodeLon = Angle.ofDeg(291.682547);
    private static final double orbitInclination = Angle.ofDeg(5.145396);
    private static final double orbitEccentricity = 0.0549;

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
        double sinOfMSun = Math.sin(Angle.ofDeg(238.533547));
        double lambdaSun = Angle.ofDeg(158.171829);

        // compute the mean orbital longitude
        double l = Angle.ofDeg(13.1763966) * daysSinceJ2010 + meanLon;

        // compute the mean anomaly
        double Mm = l - Angle.ofDeg(0.1114041) * daysSinceJ2010 - meanLonAtPerigee;

        // compute the correction terms
        double Ev = Angle.ofDeg(1.2739) * Math.sin(2 * (l - lambdaSun) - Mm);
        double Ae = Angle.ofDeg(0.1858) * sinOfMSun;
        double A3 = Angle.ofDeg(0.37) * sinOfMSun;

        // compute the corrected anomaly
        double MmPrime = Mm + Ev - Ae - A3;

        // compute 2 another correction terms
        double Ec = Angle.ofDeg(6.2886) * Math.sin(MmPrime);
        double A4 = Angle.ofDeg(0.214) * Math.sin(2 * MmPrime);

        // compute the corrected orbital longitude
        double lPrime = l + Ev + Ec - Ae + A4;

        // compute the variation
        double V = Angle.ofDeg(0.6583) * Math.sin(2 * (lPrime - lambdaSun));

        // compute the real orbital longitude
        double lSecond = lPrime + V;

        // compute the mean longitude and the corrected longitude of the ascending node
        double N = ascendingNodeLon - Angle.ofDeg(0.0529539) * daysSinceJ2010;
        double NPrime = N - Angle.ofDeg(0.16) * sinOfMSun;

        // compute an intermediate value for performances
        double sin_lSecond_minus_NPrime = Math.sin(lSecond - NPrime);

        // compute the ecliptic longitude and latitude of the moon
        double lambda = Angle.normalizePositive(Math.atan2(sin_lSecond_minus_NPrime * Math.cos(orbitInclination), Math.cos(lSecond - NPrime)) + NPrime);
        double beta = Math.asin(sin_lSecond_minus_NPrime * Math.sin(orbitInclination));
        // compute the ecliptic coordinates of the moon
        EclipticCoordinates eclipticCoordinates = EclipticCoordinates.of(lambda, beta);

        // compute the phase of the moon
        double F = (1 - Math.cos(lSecond - lambdaSun)) / 2;

        // compute the distance earth-moon to compute the angular size of the moon
        double rho = (1 - orbitEccentricity * orbitEccentricity) / (1 + orbitEccentricity * Math.cos(MmPrime + Ec));
        double theta = Angle.ofDeg(0.5181) / rho;

        // return the computed moon model
        return new Moon(
                eclipticToEquatorialConversion.apply(eclipticCoordinates),
                (float) theta,
                0,
                (float) F
        );

    }
}

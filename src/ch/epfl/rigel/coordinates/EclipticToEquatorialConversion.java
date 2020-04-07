package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.astronomy.Epoch;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.Polynomial;

import java.time.ZonedDateTime;
import java.util.function.Function;

/**
 * @author Bastien Faivre (310929)
 * @author Kamil Mellouk (312327)
 */

public final class EclipticToEquatorialConversion implements Function<EclipticCoordinates, EquatorialCoordinates> {

    // Polynomial used to compute the ecliptic obliqueness
    private static final Polynomial OBLIQUENESS_POLYNOMIAL = Polynomial.of(
            Angle.ofArcsec(0.00181),
            -Angle.ofArcsec(0.0006),
            -Angle.ofArcsec(46.815),
            Angle.ofDMS(23, 26, 21.45)
    );
    // The cosine od the ecliptic obliqueness
    private final double cosOfEclipticObliqueness;
    // The sinus of the ecliptic obliqueness
    private final double sinOfEclipticObliqueness;

    /**
     * Constructor of the coordinates system change
     *
     * @param when date-time couple with time zone
     */
    public EclipticToEquatorialConversion(ZonedDateTime when) {
        double eclipticObliqueness = OBLIQUENESS_POLYNOMIAL.at(Epoch.J2000.julianCenturiesUntil(when));
        cosOfEclipticObliqueness = Math.cos(eclipticObliqueness);
        sinOfEclipticObliqueness = Math.sin(eclipticObliqueness);
    }

    /**
     * @param ecl the given ecliptic coordinates
     * @return the equatorial coordinates corresponding to the given ecliptic coordinates
     */
    @Override
    public EquatorialCoordinates apply(EclipticCoordinates ecl) {
        return EquatorialCoordinates.of(
                Angle.normalizePositive(Math.atan2(Math.sin(ecl.lon()) * cosOfEclipticObliqueness - Math.tan(ecl.lat()) * sinOfEclipticObliqueness, Math.cos(ecl.lon()))),
                Math.asin(Math.sin(ecl.lat()) * cosOfEclipticObliqueness + Math.cos(ecl.lat()) * sinOfEclipticObliqueness * Math.sin(ecl.lon()))
        );
    }


    /**
     * @return nothing
     * @throws UnsupportedOperationException the exception to throw
     */
    @Override
    public final int hashCode() throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    /**
     * @param obj the object
     * @return nothing
     * @throws UnsupportedOperationException the exception to throw
     */
    @Override
    public final boolean equals(Object obj) throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }
}

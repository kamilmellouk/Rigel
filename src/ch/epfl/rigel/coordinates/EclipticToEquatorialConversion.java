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

    // TODO: 29/02/2020 check performances
    // the cosinus od the ecliptic obliquity
    private double cosOfEclipticObliquity;
    // the sinus of the ecliptic obliquity
    private double sinOfEclipticObliquity;

    /**
     * constructor of the coordinates system change
     *
     * @param when date-time couple with time zone
     */
    public EclipticToEquatorialConversion(ZonedDateTime when) {
        // compute the ecliptic obliquity
        double eclipticObliquity = Polynomial.of(
                Angle.ofArcsec(0.00181),
                -Angle.ofArcsec(0.0006),
                -Angle.ofArcsec(46.815),
                Angle.ofDMS(23, 26, 21.45))
                .at(Epoch.J2000.julianCenturiesUntil(when));
        cosOfEclipticObliquity = Math.cos(eclipticObliquity);
        sinOfEclipticObliquity = Math.sin(eclipticObliquity);
    }

    /**
     * @param ecl the given ecliptic coordinates
     * @return the equatorial coordinates corresponding to the given ecliptic coordinates
     */
    @Override
    public EquatorialCoordinates apply(EclipticCoordinates ecl) {
        return EquatorialCoordinates.of(
                Math.atan2(Math.sin(ecl.lon()) * cosOfEclipticObliquity - Math.tan(ecl.lat()) * sinOfEclipticObliquity, Math.cos(ecl.lon())),
                Math.asin(Math.sin(ecl.lat()) * cosOfEclipticObliquity + Math.cos(ecl.lat()) * sinOfEclipticObliquity * Math.sin(ecl.lon()))
        );
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

    /**
     * @return nothing
     * @throws UnsupportedOperationException the exception to throw
     */
    @Override
    public final int hashCode() throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }
}

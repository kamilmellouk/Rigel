package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.astronomy.Epoch;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.Polynomial;

import java.time.ZonedDateTime;
import java.util.function.Function;

/**
 * @author Mohamed Kamil MELLOUK
 * 28.02.20
 */
public final class EclipticToEquatorialConversion implements Function<EclipticCoordinates, EquatorialCoordinates> {

    private final Polynomial eclObliquityFormula = Polynomial.of(0.00181, -0.0006, -46.815, Angle.ofDeg(23.4392917));
    private double julCentSinceJ2000;
    private double eclObliquity;

    public EclipticToEquatorialConversion(ZonedDateTime when) {
        julCentSinceJ2000 = Epoch.J2000.julianCenturiesUntil(when);
        eclObliquity = eclObliquityFormula.at(julCentSinceJ2000);
    }


    @Override
    public EquatorialCoordinates apply(EclipticCoordinates ecl) {
       return EquatorialCoordinates.of(
                Math.atan2(Math.sin(ecl.lon())*Math.cos(eclObliquity) - Math.tan(ecl.lat())*Math.sin(eclObliquity),Math.cos(ecl.lon())),
                Math.asin(Math.sin(ecl.lat())*Math.cos(eclObliquity) + Math.cos(ecl.lat())*Math.sin(eclObliquity)*Math.sin(ecl.lon()))
        );
    }



    /**
     * @param obj the object
     * @return nothing
     * @throws UnsupportedOperationException to guarantee that no subclass redefines the method
     */
    @Override
    public final boolean equals(Object obj) throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    /**
     * @return nothing
     * @throws UnsupportedOperationException to guarantee that no subclass redefines the method
     */
    @Override
    public final int hashCode() throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }
}

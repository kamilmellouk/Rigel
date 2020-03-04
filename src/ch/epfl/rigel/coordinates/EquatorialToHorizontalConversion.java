package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.astronomy.SiderealTime;

import java.time.ZonedDateTime;
import java.util.function.Function;

/**
 * @author Bastien Faivre (310929)
 * @author Kamil Mellouk (312327)
 */

public final class EquatorialToHorizontalConversion implements Function<EquatorialCoordinates, HorizontalCoordinates> {

    // the sidereal time
    private final double siderealTime;
    // the observer
    private final double observerSinLat;
    private final double observerCosLat;

    /**
     * constructor of the coordinates system change
     *
     * @param when  date-time couple with time zone
     * @param where the geographic coordinates of the place
     */
    public EquatorialToHorizontalConversion(ZonedDateTime when, GeographicCoordinates where) {
        this.siderealTime = SiderealTime.local(when, where);
        observerSinLat = Math.sin(where.lat());
        observerCosLat = Math.cos(where.lat());
    }

    /**
     * @param equ the given equatorial coordinates
     * @return the horizontal coordinates corresponding to the given equatorial coordinates
     */
    @Override
    public HorizontalCoordinates apply(EquatorialCoordinates equ) {
        // compute the hour angle
        double hourAngle = siderealTime - equ.ra();
        // compute the altitude
        double h = Math.asin(Math.sin(equ.dec()) * observerSinLat + Math.cos(equ.dec()) * observerCosLat * Math.cos(hourAngle));
        return HorizontalCoordinates.of(
                Math.atan2(-Math.cos(equ.dec()) * observerCosLat * Math.sin(hourAngle), Math.sin(equ.dec()) - observerSinLat * Math.sin(h)),
                h
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

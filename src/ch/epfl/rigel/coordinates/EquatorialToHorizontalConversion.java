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
    private double siderealTime;

    /**
     * constructor of the coordinates system change
     *
     * @param when  date-time couple with time zone
     * @param where the geographic coordinates of the place
     */
    public EquatorialToHorizontalConversion(ZonedDateTime when, GeographicCoordinates where) {
        this.siderealTime = SiderealTime.local(when, where);
    }

    /**
     * @param equ the given equatorial coordinates
     * @return the horizontal coordinates corresponding to the given equatorial coordinates
     */
    @Override
    public HorizontalCoordinates apply(EquatorialCoordinates equ) {
        double hourAngle = siderealTime - equ.ra();
        // TODO: 29/02/2020 check h
        double h = Math.asin(Math.sin(equ.dec()) * Math.sin(equ.lat()) + Math.cos(equ.dec()) * Math.cos(equ.lat()) * Math.cos(hourAngle));
        return HorizontalCoordinates.of(
                Math.atan2(-Math.cos(equ.dec()) * Math.cos(equ.lat()) * Math.sin(hourAngle), Math.sin(equ.dec()) - Math.sin(equ.lat()) * Math.sin(h)),
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

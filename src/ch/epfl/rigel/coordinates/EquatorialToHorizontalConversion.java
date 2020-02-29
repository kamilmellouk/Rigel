package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.astronomy.SiderealTime;

import java.time.ZonedDateTime;
import java.util.function.Function;

/**
 * @author Bastien Faivre (310929)
 * @author Kamil Mellouk (312327)
 */

public final class EquatorialToHorizontalConversion implements Function<EquatorialCoordinates, HorizontalCoordinates> {

    private GeographicCoordinates where;
    private double sidTime;
    private double hourAngle;

    public EquatorialToHorizontalConversion(ZonedDateTime when, GeographicCoordinates where) {
        this.where = where;
        this.sidTime = SiderealTime.local(when, where);
    }

    @Override
    public HorizontalCoordinates apply(EquatorialCoordinates equat) {
        hourAngle = sidTime - equat.ra();
        double h = Math.asin(Math.sin(equat.dec()) * Math.sin(equat.lat()) + Math.cos(equat.dec()) * Math.cos(equat.lat()) * Math.cos(hourAngle));
        return HorizontalCoordinates.of(
                Math.atan2(-Math.cos(equat.dec()) * Math.cos(equat.lat()) * Math.sin(hourAngle), Math.sin(equat.dec()) - Math.sin(equat.lat()) * Math.sin(h)),
                h
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

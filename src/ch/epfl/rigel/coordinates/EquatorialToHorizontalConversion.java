package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.astronomy.SiderealTime;
import ch.epfl.rigel.math.Angle;

import java.time.ZonedDateTime;
import java.util.function.Function;

/**
 * Conversion from equatorial to horizontal coordinates
 *
 * @see EquatorialCoordinates
 * @see HorizontalCoordinates
 *
 * @author Bastien Faivre (310929)
 * @author Kamil Mellouk (312327)
 */
public final class EquatorialToHorizontalConversion implements Function<EquatorialCoordinates, HorizontalCoordinates> {

    // The sidereal time
    private final double siderealTime;
    // The observer
    private final double observerSinLat;
    private final double observerCosLat;

    /**
     * Constructor of the coordinates system change
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
     * Return the horizontal coordinates corresponding to the given equatorial coordinates
     *
     * @param equ the given equatorial coordinates
     * @return the horizontal coordinates corresponding to the given equatorial coordinates
     */
    @Override
    public HorizontalCoordinates apply(EquatorialCoordinates equ) {
        double hourAngle = siderealTime - equ.ra();

        double altitude = Math.asin(Math.sin(equ.dec()) * observerSinLat + Math.cos(equ.dec()) * observerCosLat * Math.cos(hourAngle));
        return HorizontalCoordinates.of(
                Angle.normalizePositive(Math.atan2(-Math.cos(equ.dec()) * observerCosLat * Math.sin(hourAngle), Math.sin(equ.dec()) - observerSinLat * Math.sin(altitude))),
                altitude
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

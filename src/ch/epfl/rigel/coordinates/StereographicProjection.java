package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.math.Angle;

import java.util.Locale;
import java.util.function.Function;

/**
 * @author Bastien Faivre (310929)
 * @author Kamil Mellouk (312327)
 */

public final class StereographicProjection implements Function<HorizontalCoordinates, CartesianCoordinates> {

    // HorizontalCoordinates of the center point of the projection
    private final HorizontalCoordinates center;
    // Store values exclusive to the projection center to compute the projection
    private final double centerAz;
    private final double cosCenterAlt;
    private final double sinCenterAlt;

    /**
     * Constructor of the stereographic projection
     *
     * @param center the center point of the projection
     */
    public StereographicProjection(HorizontalCoordinates center) {
        this.center = center;
        this.centerAz = center.az();
        this.cosCenterAlt = Math.cos(center.alt());
        this.sinCenterAlt = Math.sin(center.alt());
    }

    /**
     * Compute the coordinates for the circle center of the StereographicProjection of a parallel
     *
     * @param hor HorizontalCoordinates of the parallel
     * @return xy-coordinates for the circle center of the projection of the parallel
     */
    public CartesianCoordinates circleCenterForParallel(HorizontalCoordinates hor) {
        return CartesianCoordinates.of(0, cosCenterAlt / (Math.sin(hor.alt()) + sinCenterAlt));
    }

    /**
     * Compute the circle radius for the StereographicProjection of a parallel
     *
     * @param parallel to project
     * @return circle radius of the StereographicProjection of parallel
     */
    public double circleRadiusForParallel(HorizontalCoordinates parallel) {
        return Math.cos(parallel.alt()) / (Math.sin(parallel.alt()) + sinCenterAlt);
    }

    /**
     * Compute the projected diameter of an object of given angular size
     *
     * @param rad the angular size of the object
     * @return the projected diameter of the object of given angular size
     */
    public double applyToAngle(double rad) {
        return 2 * Math.tan(rad / 4d);
    }

    /**
     * Compute the cartesian coordinates of the projection of the given point
     *
     * @param azAlt the given horizontal coordinates
     * @return the cartesian coordinates of the projection of the given point
     */
    @Override
    public CartesianCoordinates apply(HorizontalCoordinates azAlt) {
        // compute the sinus and cosine of the altitude of the given point
        double azAltSin = Math.sin(azAlt.alt());
        double azAltCos = Math.cos(azAlt.alt());

        // compute the delta azimuth value, its cosine and the d value
        double deltaAz = azAlt.az() - centerAz;
        double cosDeltaAz = Math.cos(deltaAz);
        double d = 1d / (1 + azAltSin * sinCenterAlt + azAltCos * cosCenterAlt * cosDeltaAz);

        return CartesianCoordinates.of(
                d * azAltCos * Math.sin(deltaAz),
                d * (azAltSin * cosCenterAlt - azAltCos * sinCenterAlt * cosDeltaAz)
        );
    }

    /**
     * Compute the inverse StereographicProjection of given cartesian coordinates
     *
     * @param xy CartesianCoordinates to inverse project
     * @return HorizontalCoordinates resulting from the inverse projection
     */
    public HorizontalCoordinates inverseApply(CartesianCoordinates xy) {
        // get the value of the given cartesian coordinates
        double x = xy.x();
        double y = xy.y();

        // compute the rho value, the sinus and cosine of the implicit angle c
        double p = Math.sqrt(x * x + y * y);
        double sinC = (2 * p) / (p * p + 1);
        double cosC = (1 - p * p) / (p * p + 1);

        return HorizontalCoordinates.of(
                Angle.normalizePositive(Math.atan2(x * sinC, p * cosCenterAlt * cosC - y * sinCenterAlt * sinC) + centerAz),
                Math.asin(cosC * sinCenterAlt + (y * sinC * cosCenterAlt) / p)
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

    @Override
    public String toString() {
        return String.format(Locale.ROOT, "StereographicProjection of center : x=%.4f, y=%.4f", centerAz, center.alt());
    }
}

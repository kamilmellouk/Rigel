package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.math.Angle;

import java.util.Locale;
import java.util.function.Function;

/**
 * Projecting a point on a sphere (in horizontal coordinates) on a plane (using cartesian coordinates)
 *
 * @author Bastien Faivre (310929)
 * @author Kamil Mellouk (312327)
 * @see HorizontalCoordinates
 * @see CartesianCoordinates
 */
public final class StereographicProjection implements Function<HorizontalCoordinates, CartesianCoordinates> {

    // HorizontalCoordinates of the center point of the projection
    private final HorizontalCoordinates center;
    // Store values exclusive to the projection center to compute the projection
    private final double centerAz;
    private final double centerAlt;
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
        this.centerAlt = center.alt();
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
        double altitudeSinus = Math.sin(azAlt.alt());
        double altitudeCosine = Math.cos(azAlt.alt());

        double azimuthDifference = azAlt.az() - centerAz;
        double azimuthDifferenceCosine = Math.cos(azimuthDifference);
        double d = 1d / (1 + altitudeSinus * sinCenterAlt + altitudeCosine * cosCenterAlt * azimuthDifferenceCosine);

        return CartesianCoordinates.of(
                d * altitudeCosine * Math.sin(azimuthDifference),
                d * (altitudeSinus * cosCenterAlt - altitudeCosine * sinCenterAlt * azimuthDifferenceCosine)
        );
    }

    /**
     * Compute the inverse StereographicProjection of given cartesian coordinates
     *
     * @param xy CartesianCoordinates to inverse project
     * @return HorizontalCoordinates resulting from the inverse projection
     */
    public HorizontalCoordinates inverseApply(CartesianCoordinates xy) {
        double x = xy.x();
        double y = xy.y();

        // compute the rho value, the sinus and cosine of the implicit angle c
        double rho = Math.sqrt(x * x + y * y);
        double rhoSquared = rho * rho;
        double sinC = (2 * rho) / (rhoSquared + 1);
        double cosC = (1 - rhoSquared) / (rhoSquared + 1);

        return x == 0 && y == 0 ?
                HorizontalCoordinates.of(
                        Angle.normalizePositive(centerAz),
                        centerAlt
                ) :
                HorizontalCoordinates.of(
                        Angle.normalizePositive(Math.atan2(x * sinC, rho * cosCenterAlt * cosC - y * sinCenterAlt * sinC) + centerAz),
                        Math.asin(cosC * sinCenterAlt + (y * sinC * cosCenterAlt) / rho)
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
    /*
    @Override
    public final boolean equals(Object obj) throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }*/

    @Override
    public String toString() {
        return String.format(Locale.ROOT, "StereographicProjection of center : %s", center);
    }
}

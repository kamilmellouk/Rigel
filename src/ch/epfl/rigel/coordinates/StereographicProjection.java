package ch.epfl.rigel.coordinates;

import java.util.Locale;
import java.util.function.Function;

/**
 * @author Bastien Faivre (310929)
 * @author Kamil Mellouk (312327)
 */
public final class StereographicProjection implements Function<HorizontalCoordinates, CartesianCoordinates> {

    // HorizontalCoordinates of the center point of the projection
    private final HorizontalCoordinates center;
    // Storing values exclusive to the projection center to help with computing the projection
    private final double centerAz, centerAlt, cosCenterAlt, sinCenterAlt;

    public StereographicProjection(HorizontalCoordinates center) {
        this.center = center;
        this.centerAz = center.az();
        this.centerAlt = center.alt();
        this.cosCenterAlt = Math.cos(centerAlt);
        this.sinCenterAlt = Math.sin(centerAlt);
    }

    /**
     * Computing the coordinates for the circle center of the StereographicProjection of a parallel
     * @param hor HorizontalCoordinates of the parallel
     * @return xy-coordinates for the circle center of the projection of the parallel
     */
    public CartesianCoordinates circleCenterForParallel(HorizontalCoordinates hor) {
        return CartesianCoordinates.of(0, cosCenterAlt/(Math.sin(hor.alt()) + sinCenterAlt));
    }

    /**
     * Computing the circle radius for the StereographicProjection of a parallel
     * @param parallel to project
     * @return circle radius of the StereographicProjection of parallel
     */
    public double  circleRadiusForParallel(HorizontalCoordinates parallel) {
        return Math.cos(parallel.alt())/(Math.sin(parallel.alt()) + sinCenterAlt);
    }

    /**
     * Computing the diameter of an object of given angular size
     * @param rad angular size of the object
     * @return actual diameter of the object of given angular size
     */
    public double applyToAngle(double rad) {
        return 2*Math.tan(rad/4);
    }

    @Override
    public CartesianCoordinates apply(HorizontalCoordinates azAlt) {
        double deltaAz = azAlt.az() - centerAz;
        double d = 1/(1 + Math.sin(azAlt.alt())*sinCenterAlt + Math.cos(azAlt.alt()) * cosCenterAlt * Math.cos(deltaAz));

        return CartesianCoordinates.of(
                d * Math.cos(azAlt.alt()) * Math.sin(deltaAz),
                d*(Math.sin(azAlt.alt())*cosCenterAlt - Math.cos(azAlt.alt()) * cosCenterAlt * Math.cos(deltaAz))
        );
    }

    /**
     * Computing the inverse StereographicProjection of given cartesian coordinates
     * @param xy CartesianCoordinates to inverse project
     * @return HorizontalCoordinates resulting from the inverse projection
     */
    public HorizontalCoordinates inverseApply(CartesianCoordinates xy) {
        double p = Math.sqrt(xy.x()*xy.x() + xy.y()*xy.y());
        double sinC = (2*p)/(p*p + 1);
        double cosC = (1 - p*p)/(p*p + 1);

        return HorizontalCoordinates.of(
                Math.atan2(xy.x()*sinC, p*cosCenterAlt*cosC - xy.y()*sinCenterAlt*sinC) + centerAz,
                Math.asin(cosC*sinCenterAlt + (xy.y()*sinC*cosCenterAlt)/p)
        );
    }

    @Override
    public String toString() {
        // TODO Find what to display
        return String.format(Locale.ROOT, "StereographicProjection of center %s : %s --> %s", center);
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

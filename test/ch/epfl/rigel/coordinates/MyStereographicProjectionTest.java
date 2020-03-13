package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.math.Angle;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Mohamed Kamil MELLOUK
 * 05.03.20
 */
public class MyStereographicProjectionTest {

    @Test
    void applyWorks() {
        HorizontalCoordinates h1 = HorizontalCoordinates.of(Math.PI / 4, Math.PI / 6);
        HorizontalCoordinates center1 = HorizontalCoordinates.of(0, 0);
        StereographicProjection e = new StereographicProjection(center1);
        double p = Math.sqrt(6);
        CartesianCoordinates a1 = CartesianCoordinates.of(p / (4 + p), 2 / (4 + p));
        CartesianCoordinates c1 = e.apply(h1);
        assertEquals(a1.x(), c1.x(), 1e-15);
        assertEquals(a1.y(), c1.y(), 1e-15);

        HorizontalCoordinates h2 = HorizontalCoordinates.of(Math.PI / 2, Math.PI / 2);
        HorizontalCoordinates center2 = HorizontalCoordinates.of(Math.PI / 4, Math.PI / 4);
        StereographicProjection e2 = new StereographicProjection(center2);
        double p2 = Math.sqrt(2);
        CartesianCoordinates a2 = CartesianCoordinates.of(0, p2 / (2 + p2));
        CartesianCoordinates c2 = e2.apply(h2);
        assertEquals(a2.x(), c2.x(), 1e-15);
        assertEquals(a2.y(), c2.y(), 1e-15);
    }

    @Test
    void circleCenterForParallelReturnsInfinity() {
        assertEquals("(x=0.0000, y=Infinity)",
                (new StereographicProjection(HorizontalCoordinates.of(0, 0)).circleCenterForParallel(HorizontalCoordinates.of(0, 0)).toString()));
    }

    @Test
    void circleCenterForParallelWorks() {
        HorizontalCoordinates h1 = HorizontalCoordinates.of(Math.PI / 4, Math.PI / 6);
        HorizontalCoordinates center1 = HorizontalCoordinates.of(0, 0);
        StereographicProjection s = new StereographicProjection(center1);
        CartesianCoordinates a1 = s.circleCenterForParallel(h1);
        assertEquals(0, a1.x(), 1e-15);
        assertEquals(2, a1.y(), 1e-15);
    }

    @Test
    void circleRadiusForParallelWorks() {
        HorizontalCoordinates h2 = HorizontalCoordinates.of(Math.PI / 2, Math.PI / 2);
        HorizontalCoordinates center2 = HorizontalCoordinates.of(Math.PI / 4, Math.PI / 4);
        StereographicProjection e2 = new StereographicProjection(center2);
        double rho1 = e2.circleRadiusForParallel(h2);
        assertEquals(0, rho1, 1e-16);
    }

    @Test
    void circleRadiusForParallelWorks2() {
        HorizontalCoordinates h2 = HorizontalCoordinates.of(0, Math.PI / 3);
        HorizontalCoordinates center2 = HorizontalCoordinates.of(Math.PI / 4, 0);
        StereographicProjection e2 = new StereographicProjection(center2);
        double rho1 = e2.circleRadiusForParallel(h2);
        assertEquals(1 / Math.sqrt(3), rho1, 1e-15);
    }

    @Test
    void applyToAngle() {
        HorizontalCoordinates center2 = HorizontalCoordinates.of(Math.PI / 4, Math.PI / 4);
        StereographicProjection e2 = new StereographicProjection(center2);
        double z = e2.applyToAngle(Math.PI);
        assertEquals(2, z, 10e-16);
    }

    @Test
    void applyToAngleWorks() {
        HorizontalCoordinates center2 = HorizontalCoordinates.of(0, 0);
        StereographicProjection e2 = new StereographicProjection(center2);
        double z = e2.applyToAngle(Math.PI / 2);
        assertEquals(0.8284271247461900976033774484194, z, 10e-20);
    }

    @Test
    void InverseApplyWorks() {
        HorizontalCoordinates h1 = HorizontalCoordinates.of(Math.PI / 4, Math.PI / 6);
        HorizontalCoordinates center1 = HorizontalCoordinates.of(0, 0);
        StereographicProjection e = new StereographicProjection(center1);
        double p = Math.sqrt(6);
        CartesianCoordinates a1 = CartesianCoordinates.of(p / (4 + p), 2 / (4 + p));
        CartesianCoordinates c1 = e.apply(h1);
        assertEquals(e.inverseApply(a1).az(), e.inverseApply(c1).az(), 1e-15);
        assertEquals(e.inverseApply(a1).alt(), e.inverseApply(c1).alt(), 1e-15);

        HorizontalCoordinates h2 = HorizontalCoordinates.of(Math.PI / 2, Math.PI / 2);
        HorizontalCoordinates center2 = HorizontalCoordinates.of(Math.PI / 4, Math.PI / 4);
        StereographicProjection e2 = new StereographicProjection(center2);
        double p2 = Math.sqrt(2);
        CartesianCoordinates a2 = CartesianCoordinates.of(0, p2 / (2 + p2));
        CartesianCoordinates c2 = e2.apply(h2);
        assertEquals(e2.inverseApply(a2).az(), e2.inverseApply(CartesianCoordinates.of(0,c2.y())).az(), 1e-15);
        assertEquals(e2.inverseApply(a2).alt(), e2.inverseApply(c2).alt(), 1e-15);
    }

    @Test
    void toStringWorks() {
        assertEquals("StereographicProjection of center : x=0.0000, y=0.0000", new StereographicProjection(HorizontalCoordinates.of(0,0)).toString());
        assertEquals("StereographicProjection of center : x=0.0000, y=0.0000", new StereographicProjection(HorizontalCoordinates.of(0,0)).toString());
    }

    @Test
    void equalsWorks() {
        assertThrows(UnsupportedOperationException.class, () -> new StereographicProjection(HorizontalCoordinates.of(0,0)).equals(null));
    }

    @Test
    void hashcodeWorks() {
        assertThrows(UnsupportedOperationException.class, () -> new StereographicProjection(HorizontalCoordinates.of(0,0)).hashCode());
    }

    HorizontalCoordinates center = HorizontalCoordinates.of(1,1);
    StereographicProjection sProj = new StereographicProjection(center);
    HorizontalCoordinates testApplyHor = HorizontalCoordinates.ofDeg(10,20);

    StereographicProjection a = new StereographicProjection (HorizontalCoordinates.ofDeg(45,45));
    StereographicProjection c = new StereographicProjection(HorizontalCoordinates.ofDeg(23, 45));

    @Test
    void doesApplywork() {
        assertEquals(-0.1316524976, a.apply(HorizontalCoordinates.ofDeg(45, 30)).y(), 1e-8);
    }

    @Test
    void doesInverseApplyWork() {
        assertEquals(3.648704525474978, a.inverseApply(CartesianCoordinates.of(10, 0)).az(), 1e-6);
    }

    @Test
    void doesApplyToAngleWork() {
        assertEquals(0.004363316207379377, c.applyToAngle(Angle.ofDeg(1 / 2.0)), 1e-7);
    }

    @Test
    void doesCircleCenterWork() {
        assertEquals(0.6089987401, a.circleCenterForParallel(HorizontalCoordinates.ofDeg(0, 27)).y(), 1e-8);
    }

    @Test
    void doesCircleRadiusParallelWork() {
        assertEquals(0.7673831804, a.circleRadiusForParallel(HorizontalCoordinates.ofDeg(0, 27)), 1e-8);
    }

    @Test
    void doesHashCodeAndEqualsFail() {
        assertThrows(UnsupportedOperationException.class, () -> {
            sProj.hashCode();
        });
        assertThrows(UnsupportedOperationException.class, () -> {
            sProj.equals(null);
        });
    }

    @Test
    void doesApplyAndReverseApplyReturnTheBaseCoords() {
        HorizontalCoordinates actualApplyHor = sProj.inverseApply(sProj.apply(testApplyHor));
        assertEquals(testApplyHor.lon(), actualApplyHor.lon(), 1e-10);
        assertEquals(testApplyHor.lat(), actualApplyHor.lat(), 1e-10);
    }

}

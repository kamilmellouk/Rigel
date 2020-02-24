package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.math.Angle;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Bastien Faivre (310929)
 * @author Kamil Mellouk (312327)
 */

public class HorizontalCoordinatesTest {

    @Test
    void ofTest() {
        assertThrows(IllegalArgumentException.class, () -> {
            HorizontalCoordinates.of(-1, 0);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            HorizontalCoordinates.of(0, Math.PI);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            HorizontalCoordinates.of(Angle.TAU, Math.PI / 2);
        });
    }

    @Test
    void ofDegTest() {
        assertThrows(IllegalArgumentException.class, () -> {
            HorizontalCoordinates.ofDeg(-1, 0);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            HorizontalCoordinates.ofDeg(0, 180);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            HorizontalCoordinates.ofDeg(360, 90);
        });
    }

    @Test
    void returnAzTest() {
        assertEquals(1, HorizontalCoordinates.of(1, 0).az());
        assertEquals(2, HorizontalCoordinates.of(2, 0).az());
    }

    @Test
    void returnAzDegTest() {
        assertEquals(0, HorizontalCoordinates.of(0, 0).azDeg());
        assertEquals(90, HorizontalCoordinates.of(Math.PI / 2, 0).azDeg());
    }

    @Test
    void returnAltTest() {
        assertEquals(1, HorizontalCoordinates.of(0, 1).alt());
        assertEquals(0.5, HorizontalCoordinates.of(0, 0.5).alt());
    }

    @Test
    void returnAltDegTest() {
        assertEquals(0, HorizontalCoordinates.of(0, 0).altDeg());
        assertEquals(90, HorizontalCoordinates.of(0, Math.PI / 2).altDeg());
    }

    @Test
    void asOctantNameBoundsTest() {

    }

    @Test
    void asOctantNameNormalTest() {
        assertEquals("N", HorizontalCoordinates.ofDeg(350, 0).azOctantName("N", "E", "S", "W"));
        assertEquals("N", HorizontalCoordinates.ofDeg(0, 0).azOctantName("N", "E", "S", "W"));
        assertEquals("N", HorizontalCoordinates.ofDeg(10, 0).azOctantName("N", "E", "S", "W"));
        assertEquals("NE", HorizontalCoordinates.ofDeg(35, 0).azOctantName("N", "E", "S", "W"));
        assertEquals("NE", HorizontalCoordinates.ofDeg(45, 0).azOctantName("N", "E", "S", "W"));
        assertEquals("NE", HorizontalCoordinates.ofDeg(55, 0).azOctantName("N", "E", "S", "W"));
        assertEquals("E", HorizontalCoordinates.ofDeg(80, 0).azOctantName("N", "E", "S", "W"));
        assertEquals("E", HorizontalCoordinates.ofDeg(90, 0).azOctantName("N", "E", "S", "W"));
        assertEquals("E", HorizontalCoordinates.ofDeg(100, 0).azOctantName("N", "E", "S", "W"));
        assertEquals("SE", HorizontalCoordinates.ofDeg(125, 0).azOctantName("N", "E", "S", "W"));
        assertEquals("SE", HorizontalCoordinates.ofDeg(135, 0).azOctantName("N", "E", "S", "W"));
        assertEquals("SE", HorizontalCoordinates.ofDeg(145, 0).azOctantName("N", "E", "S", "W"));
        assertEquals("S", HorizontalCoordinates.ofDeg(170, 0).azOctantName("N", "E", "S", "W"));
        assertEquals("S", HorizontalCoordinates.ofDeg(180, 0).azOctantName("N", "E", "S", "W"));
        assertEquals("S", HorizontalCoordinates.ofDeg(190, 0).azOctantName("N", "E", "S", "W"));
        assertEquals("SW", HorizontalCoordinates.ofDeg(215, 0).azOctantName("N", "E", "S", "W"));
        assertEquals("SW", HorizontalCoordinates.ofDeg(225, 0).azOctantName("N", "E", "S", "W"));
        assertEquals("SW", HorizontalCoordinates.ofDeg(235, 0).azOctantName("N", "E", "S", "W"));
        assertEquals("W", HorizontalCoordinates.ofDeg(260, 0).azOctantName("N", "E", "S", "W"));
        assertEquals("W", HorizontalCoordinates.ofDeg(270, 0).azOctantName("N", "E", "S", "W"));
        assertEquals("W", HorizontalCoordinates.ofDeg(280, 0).azOctantName("N", "E", "S", "W"));
        assertEquals("NW", HorizontalCoordinates.ofDeg(305, 0).azOctantName("N", "E", "S", "W"));
        assertEquals("NW", HorizontalCoordinates.ofDeg(315, 0).azOctantName("N", "E", "S", "W"));
        assertEquals("NW", HorizontalCoordinates.ofDeg(325, 0).azOctantName("N", "E", "S", "W"));
    }

    @Test
    void angularDistanceBoundsTest() {
        assertEquals(0, HorizontalCoordinates.of(0, 0).angularDistanceTo(HorizontalCoordinates.of(0, 0)));
        assertEquals(0, HorizontalCoordinates.of(Math.PI, 0).angularDistanceTo(HorizontalCoordinates.of(Math.PI, 0)));
        assertEquals(0, HorizontalCoordinates.of(0, -Math.PI / 2).angularDistanceTo(HorizontalCoordinates.of(0, -Math.PI / 2)));
        assertEquals(0, HorizontalCoordinates.of(0, Math.PI / 2).angularDistanceTo(HorizontalCoordinates.of(0, Math.PI / 2)));
        assertEquals(Math.PI, HorizontalCoordinates.of(0, 0).angularDistanceTo(HorizontalCoordinates.of(Math.PI, 0)));
        assertEquals(Math.PI, HorizontalCoordinates.of(Math.PI, 0).angularDistanceTo(HorizontalCoordinates.of(0, 0)));
        assertEquals(Math.PI, HorizontalCoordinates.of(0, -Math.PI / 2).angularDistanceTo(HorizontalCoordinates.of(0, Math.PI / 2)));
        assertEquals(Math.PI, HorizontalCoordinates.of(0, Math.PI / 2).angularDistanceTo(HorizontalCoordinates.of(0, -Math.PI / 2)));
    }

    @Test
    void angularDistanceNormalTest() {
        assertEquals(Angle.ofDeg(61.1056), HorizontalCoordinates.of(Angle.ofDeg(27.634), Angle.ofDeg(3.65432)).angularDistanceTo(HorizontalCoordinates.of(Angle.ofDeg(64.6243), Angle.ofDeg(-48.2532))), 1e-4);
        assertEquals(Angle.ofDeg(94.8065), HorizontalCoordinates.of(Angle.ofDeg(174.2526), Angle.ofDeg(87.5235)).angularDistanceTo(HorizontalCoordinates.of(Angle.ofDeg(23.857), Angle.ofDeg(-2.6541))), 1e-4);
        assertEquals(Angle.ofDeg(131.3288), HorizontalCoordinates.of(Angle.ofDeg(126.5262), Angle.ofDeg(-67.523)).angularDistanceTo(HorizontalCoordinates.of(Angle.ofDeg(12.5232), Angle.ofDeg(35.2564))), 1e-4);
    }

    @Test
    void toStringTest() {
        assertEquals("(az=0.0000°, alt=0.0000°)", HorizontalCoordinates.of(0, 0).toString());
        assertEquals("(az=90.0000°, alt=22.5000°)", HorizontalCoordinates.of(Math.PI / 2, Math.PI / 8).toString());
        assertEquals("(az=60.0000°, alt=36.0000°)", HorizontalCoordinates.of(Math.PI / 3, Math.PI / 5).toString());
        assertEquals("(az=70.7316°, alt=24.7575°)", HorizontalCoordinates.of(1.2345, 0.4321).toString());
    }

}

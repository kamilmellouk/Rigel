package ch.epfl.rigel.gui;

import ch.epfl.rigel.astronomy.ObservedSky;
import ch.epfl.rigel.astronomy.Star;
import ch.epfl.rigel.coordinates.StereographicProjection;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.ClosedInterval;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.transform.Transform;

/**
 * @author Mohamed Kamil MELLOUK
 * 11.04.20
 */
public class SkyCanvasPainter {

    private Canvas canvas;
    private GraphicsContext ctx;

    SkyCanvasPainter(Canvas canvas) {
        this.canvas = canvas;
        ctx = canvas.getGraphicsContext2D();
    }

    public void clear() {
        canvas.getGraphicsContext2D().clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }

    public void drawStars(ObservedSky sky, StereographicProjection projection, Transform planeToCanvas) {

        for (Star s : sky.stars()) {

        }
    }

    public void drawPlanets(ObservedSky sky, StereographicProjection projection, Transform planeToCanvas) {

    }

    public void drawSun(ObservedSky sky, StereographicProjection projection, Transform planeToCanvas) {

    }

    public void drawMoon(ObservedSky sky, StereographicProjection projection, Transform planeToCanvas) {

    }

    public void drawHorizon(ObservedSky sky, StereographicProjection projection, Transform planeToCanvas) {

    }

    /**
     * Computing the on-screen diameter of a CelestialObject with its given magnitude
     *
     * @param magnitude of the object to draw
     * @return diameter of the object's on-screen representation
     */
    private double magBasedSize(double magnitude) {
        double m = ClosedInterval.of(-2, 5).clip(magnitude);
        double sizeFactor = (99 - 17 * m) / 140;
        return sizeFactor * 2 * Math.tan(Angle.ofDeg(0.5) / 4);
    }

}

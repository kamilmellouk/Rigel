package ch.epfl.rigel.gui;

import ch.epfl.rigel.astronomy.ObservedSky;
import ch.epfl.rigel.astronomy.Star;
import ch.epfl.rigel.astronomy.Sun;
import ch.epfl.rigel.coordinates.*;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.ClosedInterval;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
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
        ctx.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        ctx.setFill(Color.BLACK);
        ctx.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }

    public void drawStars(ObservedSky sky, StereographicProjection projection, Transform planeToCanvas) {

        double[] transformedCoords = new double[sky.starPositions().length];
        planeToCanvas.transform2DPoints(sky.starPositions(), 0, transformedCoords, 0, sky.starPositions().length/2);
        for (Star s : sky.stars()) {
            CartesianCoordinates pos = sky.getPosition(s);
            Point2D coords = planeToCanvas.transform(pos.x(), pos.y());
            ctx.setFill(BlackBodyColor.colorForTemperature(s.colorTemperature()));

            Point2D p = planeToCanvas.deltaTransform(magBasedSize(s.magnitude(), projection), magBasedSize(s.magnitude(), projection));
            double diameter = Math.abs(p.getX()) + Math.abs(p.getY());
            ctx.fillOval(coords.getX(), coords.getY(), diameter, diameter);

        }
    }

    public void drawPlanets(ObservedSky sky, StereographicProjection projection, Transform planeToCanvas) {

    }

    public void drawSun(ObservedSky sky, StereographicProjection projection, Transform planeToCanvas) {
        Sun s = sky.sun();
        Point2D p = planeToCanvas.transform(sky.sunPosition().x(), sky.sunPosition().y());
        ctx.setFill(Color.YELLOW);
        ctx.fillOval(p.getX(), p.getY(), 3, 3);
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
    private static double magBasedSize(double magnitude, StereographicProjection projection) {
        double m = ClosedInterval.of(-2, 5).clip(magnitude);
        double factor = (99 - 17*m) / 140;
        return factor * projection.applyToAngle(Angle.ofDeg(0.5));
    }

}

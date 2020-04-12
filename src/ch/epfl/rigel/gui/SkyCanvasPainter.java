package ch.epfl.rigel.gui;

import ch.epfl.rigel.astronomy.*;
import ch.epfl.rigel.coordinates.CartesianCoordinates;
import ch.epfl.rigel.coordinates.StereographicProjection;
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

    /**
     * Constructing a Painter with a given canvas
     *
     * @param canvas the given canvas
     */
    SkyCanvasPainter(Canvas canvas) {
        this.canvas = canvas;
        ctx = canvas.getGraphicsContext2D();
    }

    /**
     * Emptying the canvas then filling it in black
     */
    public void clear() {
        ctx.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        ctx.setFill(Color.WHITE);
        ctx.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }

    /**
     * Representing stars on the canvas
     *
     * @param sky           to represent
     * @param projection    used
     * @param planeToCanvas transformation
     */
    public void drawStars(ObservedSky sky, StereographicProjection projection, Transform planeToCanvas) {
        for (Star s : sky.stars()) {
            Point2D pos = planeToCanvas.transform(sky.getPosition(s).x(), sky.getPosition(s).y());
            double diameter = transformedDiameter(s.magnitude(), projection, planeToCanvas);

            ctx.setFill(BlackBodyColor.colorForTemperature(s.colorTemperature()));
            ctx.fillOval(pos.getX(), pos.getY(), diameter, diameter);
        }
    }

    /**
     * Representing asterisms by linking their stars on the canvas
     *
     * @param sky           to represent
     * @param projection    used
     * @param planeToCanvas transformation
     */
    public void drawAsterisms(ObservedSky sky, StereographicProjection projection, Transform planeToCanvas) {
        ctx.beginPath();
        ctx.setStroke(Color.BLUE);
        ctx.setLineWidth(1);
        ctx.moveTo(50, 50);
        ctx.lineTo(-20, -20);
        ctx.stroke();
        for (Asterism a : sky.asterisms()) {
            Point2D startPos = planeToCanvas.transform(sky.getPosition(a.stars().get(0)).x(), sky.getPosition(a.stars().get(0)).y());
            ctx.moveTo(startPos.getX(), startPos.getY());

            for (int i = 1; i < a.stars().size(); i++) {
                Point2D nextPos = planeToCanvas.transform(sky.getPosition(a.stars().get(i)).x(), sky.getPosition(a.stars().get(i)).y());
                ctx.lineTo(nextPos.getX(), nextPos.getY());
                ctx.stroke();
                ctx.moveTo(nextPos.getX(), nextPos.getY());

            }
        }
    }

    /**
     * Representing planets on the canvas
     *
     * @param sky           to represent
     * @param projection    used
     * @param planeToCanvas transformation
     */
    public void drawPlanets(ObservedSky sky, StereographicProjection projection, Transform planeToCanvas) {
        for (Planet p : sky.planets()) {
            Point2D pos = planeToCanvas.transform(sky.getPosition(p).x(), sky.getPosition(p).y());
            double diameter = transformedDiameter(p.magnitude(), projection, planeToCanvas);

            ctx.setFill(Color.LIGHTGRAY);
            ctx.fillOval(pos.getX(), pos.getY(), diameter, diameter);
        }
    }

    /**
     * Representing the Sun (if visible) on the canvas
     *
     * @param sky           to represent
     * @param projection    used
     * @param planeToCanvas transformation
     */
    public void drawSun(ObservedSky sky, StereographicProjection projection, Transform planeToCanvas) {
        Sun s = sky.sun();
        Point2D pos = planeToCanvas.transform(sky.sunPosition().x(), sky.sunPosition().y());
        double diameter = transformedDiameter(s.magnitude(), projection, planeToCanvas);

        // yellow halo around the sun
        ctx.setFill(Color.YELLOW.deriveColor(0, 0, 1, 0.25));
        ctx.fillOval(pos.getX(), pos.getY(), 2.2 * diameter, 2.2 * diameter);

        ctx.setGlobalAlpha(1);
        ctx.setFill(Color.YELLOW);
        ctx.fillOval(pos.getX(), pos.getY(), diameter + 2, diameter + 2);

        ctx.setFill(Color.WHITE);
        ctx.fillOval(pos.getX(), pos.getY(), diameter, diameter);
    }

    /**
     * Representing the Moon (if visible) on the canvas
     *
     * @param sky           to represent
     * @param projection    used
     * @param planeToCanvas transformation
     */
    public void drawMoon(ObservedSky sky, StereographicProjection projection, Transform planeToCanvas) {
        Moon m = sky.moon();
        Point2D pos = planeToCanvas.transform(sky.moonPosition().x(), sky.moonPosition().y());
        double diameter = transformedDiameter(m.magnitude(), projection, planeToCanvas);

        ctx.setFill(Color.WHITE);
        ctx.fillOval(pos.getX(), pos.getY(), diameter, diameter);
    }

    /**
     * Representing the horizon (if visible) on the canvas
     *
     * @param sky           to represent
     * @param projection    used
     * @param planeToCanvas transformation
     */
    public void drawHorizon(ObservedSky sky, StereographicProjection projection, Transform planeToCanvas) {

    }

    /**
     * Computing the on-screen diameter of a CelestialObject
     *
     * @param magnitude  of the CelestialObject
     * @param projection used
     * @param transform  to apply
     * @return
     */
    private static double transformedDiameter(double magnitude, StereographicProjection projection, Transform transform) {
        double clippedM = ClosedInterval.of(-2, 5).clip(magnitude);
        double factor = (99 - 17 * clippedM) / 140;
        double m = factor * projection.applyToAngle(Angle.ofDeg(0.5));
        Point2D size = transform.deltaTransform(m, m);
        return Math.abs(size.getX());
    }

}

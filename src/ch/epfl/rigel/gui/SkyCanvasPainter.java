package ch.epfl.rigel.gui;

import ch.epfl.rigel.astronomy.*;
import ch.epfl.rigel.coordinates.CartesianCoordinates;
import ch.epfl.rigel.coordinates.HorizontalCoordinates;
import ch.epfl.rigel.coordinates.StereographicProjection;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.ClosedInterval;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.transform.Transform;

/**
 * @author Bastien Faivre (310929)
 * @author Kamil Mellouk (312327)
 */

public class SkyCanvasPainter {

    private final Canvas canvas;
    private final GraphicsContext ctx;

    /**
     * Constructor of a Painter with a given canvas
     *
     * @param canvas the given canvas
     */
    SkyCanvasPainter(Canvas canvas) {
        this.canvas = canvas;
        ctx = canvas.getGraphicsContext2D();
    }

    /**
     * Clear the canvas then filling it in black
     */
    public void clear() {
        ctx.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        ctx.setFill(Color.BLACK);
        ctx.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }

    /**
     * Represent the stars on the canvas
     *
     * @param sky           to represent
     * @param projection    used
     * @param planeToCanvas transformation
     */
    public void drawStars(ObservedSky sky, StereographicProjection projection, Transform planeToCanvas) {
        for (Star star : sky.stars()) {
            Point2D position = planeToCanvas.transform(sky.getPosition(star).x(), sky.getPosition(star).y());
            double diameter = transformedDiameter(star.magnitude(), projection, planeToCanvas);
            ctx.setFill(BlackBodyColor.colorForTemperature(star.colorTemperature()));
            ctx.fillOval(position.getX(), position.getY(), diameter, diameter);
        }
    }

    /**
     * Represent the asterisms by linking their stars on the canvas
     *
     * @param sky           to represent
     * @param planeToCanvas transformation
     */
    public void drawAsterisms(ObservedSky sky, Transform planeToCanvas) {
        ctx.setStroke(Color.BLUE);
        ctx.setLineWidth(1);

        for (Asterism asterism : sky.asterisms()) {
            ctx.beginPath();
            Point2D startingPosition = planeToCanvas.transform(sky.getPosition(asterism.stars().get(0)).x(), sky.getPosition(asterism.stars().get(0)).y());
            ctx.moveTo(startingPosition.getX(), startingPosition.getY());

            for (int i = 1; i < asterism.stars().size(); i++) {
                Point2D nextPos = planeToCanvas.transform(sky.getPosition(asterism.stars().get(i)).x(), sky.getPosition(asterism.stars().get(i)).y());
                ctx.lineTo(nextPos.getX(), nextPos.getY());
            }
            ctx.stroke();
        }
    }

    /**
     * Represent the planets on the canvas
     *
     * @param sky           to represent
     * @param projection    used
     * @param planeToCanvas transformation
     */
    public void drawPlanets(ObservedSky sky, StereographicProjection projection, Transform planeToCanvas) {
        ctx.setFill(Color.LIGHTGRAY);
        for (Planet planet : sky.planets()) {
            Point2D pos = planeToCanvas.transform(sky.getPosition(planet).x(), sky.getPosition(planet).y());
            double diameter = transformedDiameter(planet.magnitude(), projection, planeToCanvas);
            ctx.fillOval(pos.getX(), pos.getY(), diameter, diameter);
        }
    }

    /**
     * Represent the Sun (if visible) on the canvas
     *
     * @param sky           to represent
     * @param projection    used
     * @param planeToCanvas transformation
     */
    public void drawSun(ObservedSky sky, StereographicProjection projection, Transform planeToCanvas) {
        Point2D pos = planeToCanvas.transform(sky.sunPosition().x(), sky.sunPosition().y());
        double diameter = transformedDiameter(sky.sun().magnitude(), projection, planeToCanvas);

        // yellow halo around the sun
        ctx.setFill(Color.YELLOW.deriveColor(0, 0, 1, 0.25));
        ctx.fillOval(pos.getX() - 3.4, pos.getY() - 3.4, 2.2 * diameter, 2.2 * diameter);

        ctx.setFill(Color.YELLOW);
        ctx.fillOval(pos.getX() - 1, pos.getY() - 1, diameter + 2, diameter + 2);

        ctx.setFill(Color.WHITE);
        ctx.fillOval(pos.getX(), pos.getY(), diameter, diameter);
    }

    /**
     * Represent the Moon (if visible) on the canvas
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
     * Represent the horizon (if visible) on the canvas
     *
     * @param sky           to represent
     * @param projection    used
     * @param planeToCanvas transformation
     */
    public void drawHorizon(ObservedSky sky, StereographicProjection projection, Transform planeToCanvas) {
        CartesianCoordinates center = projection.circleCenterForParallel(HorizontalCoordinates.of(0, 0));
        double radius = projection.circleRadiusForParallel(HorizontalCoordinates.of(0, 0));

        ctx.setStroke(Color.RED);
        ctx.setLineWidth(2);
        ctx.strokeOval(center.x(), center.y(), radius, radius);
    }

    /**
     * Compute the on-screen diameter of a CelestialObject
     *
     * @param magnitude  of the CelestialObject
     * @param projection used
     * @param transform  to apply
     * @return the on-screen diameter of a CelestialObject
     */
    private static double transformedDiameter(double magnitude, StereographicProjection projection, Transform transform) {
        double clippedMagnitude = ClosedInterval.of(-2, 5).clip(magnitude);
        double factor = (99 - 17 * clippedMagnitude) / 140d;
        double diameter = factor * projection.applyToAngle(Angle.ofDeg(0.5));
        Point2D size = transform.deltaTransform(diameter, diameter);
        return size.getX();
    }

}

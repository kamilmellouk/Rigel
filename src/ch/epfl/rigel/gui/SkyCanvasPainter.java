package ch.epfl.rigel.gui;

import ch.epfl.rigel.Preconditions;
import ch.epfl.rigel.astronomy.Asterism;
import ch.epfl.rigel.astronomy.ObservedSky;
import ch.epfl.rigel.astronomy.Planet;
import ch.epfl.rigel.astronomy.Star;
import ch.epfl.rigel.coordinates.CartesianCoordinates;
import ch.epfl.rigel.coordinates.HorizontalCoordinates;
import ch.epfl.rigel.coordinates.StereographicProjection;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.ClosedInterval;
import javafx.geometry.Point2D;
import javafx.geometry.VPos;
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
            ctx.fillOval(position.getX() - diameter / 2d, position.getY() - diameter / 2d, diameter, diameter);
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
            Point2D currentPos = planeToCanvas.transform(sky.getPosition(asterism.stars().get(0)).x(), sky.getPosition(asterism.stars().get(0)).y());
            ctx.moveTo(currentPos.getX(), currentPos.getY());
            Point2D nextPos;

            for (Star star : asterism.stars()) {
                nextPos = planeToCanvas.transform(sky.getPosition(star).x(), sky.getPosition(star).y());
                // skip the line between two stars that are invisible on screen
                if (canvas.getBoundsInLocal().contains(currentPos) || canvas.getBoundsInLocal().contains(nextPos)) {
                    ctx.lineTo(nextPos.getX(), nextPos.getY());
                    currentPos = nextPos;
                }
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

        double[] planetPositions = new double[14];
        planeToCanvas.transform2DPoints(sky.planetPositions(), 0, planetPositions, 0, 7);

        int index = 0;
        for (Planet planet : sky.planets()) {
            double diameter = transformedDiameter(planet.magnitude(), projection, planeToCanvas);
            ctx.fillOval(planetPositions[index], planetPositions[index + 1], diameter, diameter);
            index += 2;
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
        double positionDifference = (2.2 * diameter - diameter) / 2d;
        ctx.setFill(Color.YELLOW.deriveColor(0, 0, 1, 0.25));
        ctx.fillOval(pos.getX() - positionDifference, pos.getY() - positionDifference, 2.2 * diameter, 2.2 * diameter);

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
        ctx.setFill(Color.WHITE);
        Point2D pos = planeToCanvas.transform(sky.moonPosition().x(), sky.moonPosition().y());
        double diameter = transformedDiameter(sky.moon().magnitude(), projection, planeToCanvas);
        ctx.fillOval(pos.getX(), pos.getY(), diameter, diameter);
    }

    /**
     * Represent the horizon (if visible) on the canvas
     *
     * @param projection used
     */
    public void drawHorizon(StereographicProjection projection, Transform planeToCanvas) {
        ctx.setStroke(Color.RED);
        ctx.setLineWidth(2);
        CartesianCoordinates center = projection.circleCenterForParallel(HorizontalCoordinates.of(0, 0));
        Point2D pos = planeToCanvas.transform(center.x(), center.y());
        double radius = projection.circleRadiusForParallel(HorizontalCoordinates.of(0, 0));
        double transformedRadius = planeToCanvas.deltaTransform(radius, 0).getX();

        ctx.strokeOval(pos.getX() - transformedRadius, pos.getY() - transformedRadius, transformedRadius * 2, transformedRadius * 2);
    }

    /**
     * Represent the cardinal and inter cardinal points on the canvas
     *
     * @param projection    used
     * @param planeToCanvas transformation
     */
    public void drawCardinalPoints(StereographicProjection projection, Transform planeToCanvas) {
        ctx.setStroke(Color.RED);
        ctx.setLineWidth(1);
        ctx.setTextBaseline(VPos.TOP);

        Point2D transformedN = transformedCardPoint("N", projection, planeToCanvas);
        ctx.strokeText("N", transformedN.getX(), transformedN.getY());

        Point2D transformedNE = transformedCardPoint("NE", projection, planeToCanvas);
        ctx.strokeText("NE", transformedNE.getX(), transformedNE.getY());

        Point2D transformedE = transformedCardPoint("E", projection, planeToCanvas);
        ctx.strokeText("E", transformedE.getX(), transformedE.getY());

        Point2D transformedSE = transformedCardPoint("SE", projection, planeToCanvas);
        ctx.strokeText("SE", transformedSE.getX(), transformedSE.getY());

        Point2D transformedS = transformedCardPoint("S", projection, planeToCanvas);
        ctx.strokeText("S", transformedS.getX(), transformedS.getY());

        Point2D transformedSO = transformedCardPoint("SO", projection, planeToCanvas);
        ctx.strokeText("SO", transformedSO.getX(), transformedSO.getY());

        Point2D transformedO = transformedCardPoint("O", projection, planeToCanvas);
        ctx.strokeText("O", transformedO.getX(), transformedO.getY());

        Point2D transformedNO = transformedCardPoint("NO", projection, planeToCanvas);
        ctx.strokeText("NO", transformedNO.getX(), transformedNO.getY());


    }

    /**
     * Computes the on-screen diameter of a CelestialObject
     *
     * @param magnitude     of the CelestialObject
     * @param projection    used
     * @param planeToCanvas transformation to apply
     * @return the on-screen diameter of the CelestialObject
     */
    private static double transformedDiameter(double magnitude, StereographicProjection projection, Transform planeToCanvas) {
        double clippedMagnitude = ClosedInterval.of(-2, 5).clip(magnitude);
        double factor = (99 - 17 * clippedMagnitude) / 140d;
        double diameter = factor * projection.applyToAngle(Angle.ofDeg(0.5));
        Point2D size = planeToCanvas.deltaTransform(diameter, diameter);
        return size.getX();
    }

    /**
     * Computes the on-screen position of a cardinal point
     *
     * @param cardPoint     string representation of the cardinal point (N, NE, E, SE, S, SO, O or NO)
     * @param projection    used
     * @param planeToCanvas transformation to apply
     * @return the on-screen position of the cardinal point
     */
    private static Point2D transformedCardPoint(String cardPoint, StereographicProjection projection, Transform planeToCanvas) {
        double az = 0;
        switch (cardPoint) {
            case "N":
                az = 0;
                break;
            case "NE":
                az = 45;
                break;
            case "E":
                az = 90;
                break;
            case "SE":
                az = 135;
                break;
            case "S":
                az = 180;
                break;
            case "SO":
                az = 225;
                break;
            case "O":
                az = 270;
                break;
            case "NO":
                az = 315;
                break;
            default:
                Preconditions.checkArgument(false);
        }

        CartesianCoordinates card = projection.apply(HorizontalCoordinates.of(Angle.ofDeg(az), Angle.ofDeg(-0.5)));
        return planeToCanvas.transform(card.x(), card.y());

    }

}

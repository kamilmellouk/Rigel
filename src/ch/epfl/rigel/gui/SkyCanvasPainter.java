package ch.epfl.rigel.gui;

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
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.transform.Transform;

import java.util.List;

/**
 * Painter for the sky canvas
 *
 * @author Bastien Faivre (310929)
 * @author Kamil Mellouk (312327)
 */
public class SkyCanvasPainter {

    private final Canvas canvas;
    private final GraphicsContext ctx;

    // Constants used to compute the on-screen diameter of an object
    private static final ClosedInterval MAG_INTERVAL = ClosedInterval.of(-2, 5);
    private static final double ZERO_FIVE_DEG_TO_RAD = Angle.ofDeg(0.5);

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
    public void clear(boolean isNight) {
        ctx.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        if(isNight) {
            ctx.setFill(Color.BLACK);
        } else {
            ctx.setFill(Color.SKYBLUE);
        }
        ctx.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }

    /**
     * Drawing the stars and asterisms, used to manage drawStars() and drawAsterisms() with the same position array
     *
     * @param sky           to represent
     * @param projection    used
     * @param planeToCanvas transformation
     * @param stars         boolean indicating whether to draw the stars or not
     * @param asterisms     boolean indicating whether to draw the asterisms or not
     */
    public void drawStarsAsterisms(ObservedSky sky, StereographicProjection projection, Transform planeToCanvas,
                                   boolean stars, boolean asterisms) {
        // transform all positions of the stars
        double[] starPositions = sky.starPositions();
        double[] transformedPos = new double[starPositions.length];
        planeToCanvas.transform2DPoints(starPositions, 0, transformedPos, 0, sky.stars().size());

        if (asterisms) drawAsterisms(sky, transformedPos);
        if (stars) drawStars(sky, projection, planeToCanvas, transformedPos);
    }

    /**
     * Represent the stars on the canvas
     *
     * @param sky           to represent
     * @param projection    used
     * @param planeToCanvas transformation
     */
    public void drawStars(ObservedSky sky, StereographicProjection projection, Transform planeToCanvas,
                          double[] transformedPos) {
        ctx.setStroke(Color.RED);
        ctx.setLineWidth(1);
        ctx.setTextBaseline(VPos.BOTTOM);
        int index = 0;
        for (Star star : sky.stars()) {
            double diameter = transformedDiameter(star.magnitude(), projection, planeToCanvas);
            fillDisk(transformedPos[index], transformedPos[index + 1], diameter,
                    BlackBodyColor.colorForTemperature(star.colorTemperature()));
            if(star.colorTemperature() > 20000)
                ctx.strokeText(star.info(), transformedPos[index], transformedPos[index+1]);
            index += 2;
        }
    }

    /**
     * Represent the asterisms by linking their stars on the canvas
     *
     * @param sky to represent
     */
    public void drawAsterisms(ObservedSky sky, double[] transformedPos) {
        ctx.setStroke(Color.BLUE);
        ctx.setLineWidth(1);

        var bounds = canvas.getBoundsInLocal();
        for (Asterism asterism : sky.asterisms()) {
            ctx.beginPath();
            List<Integer> indices = sky.asterismIndices(asterism);
            // get the position of the first star of the asterism
            Point2D currentPos = new Point2D(
                    transformedPos[indices.get(0) * 2],
                    transformedPos[indices.get(0) * 2 + 1]
            );
            ctx.moveTo(currentPos.getX(), currentPos.getY());
            Point2D nextPos;

            for (int index : indices) {
                nextPos = new Point2D(
                        transformedPos[index * 2],
                        transformedPos[index * 2 + 1]
                );
                // skip the line between two stars that are both invisible on screen
                if (bounds.contains(currentPos) || bounds.contains(nextPos)) {
                    ctx.lineTo(nextPos.getX(), nextPos.getY());
                } else {
                    ctx.moveTo(nextPos.getX(), nextPos.getY());
                }
                currentPos = nextPos;
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
        // transform all positions of the planets
        double[] planetPositions = sky.planetPositions();
        double[] transformedPlanetPositions = new double[planetPositions.length];
        planeToCanvas.transform2DPoints(planetPositions, 0, transformedPlanetPositions, 0, 7);

        int index = 0;
        for (Planet planet : sky.planets()) {
            double diameter = transformedDiameter(planet.magnitude(), projection, planeToCanvas);
            fillDisk(transformedPlanetPositions[index], transformedPlanetPositions[index + 1], diameter, Color.LIGHTGRAY);
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
        double tempDiam = projection.applyToAngle(sky.sun().angularSize());
        double diameter = planeToCanvas.deltaTransform(tempDiam, 0).getX();
        double haloDiameter = 2.2 * diameter;
        Image sunImage = new Image(getClass().getResourceAsStream("/sun.png"),
                haloDiameter, haloDiameter, true, true);
        ctx.drawImage(sunImage, pos.getX() - haloDiameter / 2, pos.getY() - haloDiameter / 2);

    }

    /**
     * Represent the Moon (if visible) on the canvas
     *
     * @param sky           to represent
     * @param projection    used
     * @param planeToCanvas transformation
     */
    public void drawMoon(ObservedSky sky, StereographicProjection projection, Transform planeToCanvas) {
        Point2D pos = planeToCanvas.transform(sky.moonPosition().x(), sky.moonPosition().y());
        double diameter = transformedDiameter(sky.moon().magnitude(), projection, planeToCanvas);
        fillDisk(pos.getX(), pos.getY(), diameter, Color.WHITE);
    }

    /**
     * Represent the horizon (if visible) on the canvas
     *
     * @param projection used
     */
    public void drawHorizon(StereographicProjection projection, Transform planeToCanvas) {
        CartesianCoordinates center = projection.circleCenterForParallel(HorizontalCoordinates.of(0, 0));
        Point2D pos = planeToCanvas.transform(center.x(), center.y());
        double radius = projection.circleRadiusForParallel(HorizontalCoordinates.of(0, 0));
        double transformedRadius = planeToCanvas.deltaTransform(radius, 0).getX();

        // TODO draw the earth, everything on screen but the horizon disk
        // TODO use shapes, substract
        /*ctx.setFill(Color.FORESTGREEN);
        ctx.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        ctx.setFill(Color.TRANSPARENT);
        ctx.fillOval(pos.getX() - transformedRadius, pos.getY() - transformedRadius,
                transformedRadius * 2, transformedRadius * 2);
        ctx.fillArc(pos.getX() - transformedRadius, pos.getY() - transformedRadius, 100, 100,
                100, 100, ArcType.ROUND);
         */
        ctx.setStroke(Color.RED);
        ctx.setLineWidth(2);
        ctx.strokeOval(pos.getX() - transformedRadius, pos.getY() - transformedRadius,
                transformedRadius * 2, transformedRadius * 2);
    }

    public void drawHorizontalGrid(StereographicProjection projection, Transform planeToCanvas) {
        ctx.setStroke(Color.INDIANRED);
        ctx.setLineWidth(1);
        // TODO implement
        // TODO meridian
        CartesianCoordinates center = projection.circleCenterForParallel(HorizontalCoordinates.of(0, 0));
        Point2D pos = planeToCanvas.transform(center.x(), center.y());

        for (int alt = -90; alt < 91; alt += 10) {
            double radius = projection.circleRadiusForParallel(HorizontalCoordinates.ofDeg(0, alt));
            double transformedRadius = planeToCanvas.deltaTransform(radius, 0).magnitude();

            ctx.strokeOval(pos.getX() - transformedRadius, pos.getY() - transformedRadius,
                    transformedRadius * 2, transformedRadius * 2);
        }
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
        for (int az = 0; az < 360; az += 45) {
            HorizontalCoordinates azAlt = HorizontalCoordinates.ofDeg(az, -0.5);
            CartesianCoordinates cardPos = projection.apply(azAlt);
            Point2D screenPos = planeToCanvas.transform(cardPos.x(), cardPos.y());
            ctx.strokeText(azAlt.azOctantName("N", "E", "S", "O"), screenPos.getX(), screenPos.getY());
        }
    }

    /**
     * Filling an disk of given position and diameter with a given color
     *
     * @param x x-coordinate of the disk center
     * @param y y-coordinate of the disk center
     * @param d diameter of the disk
     * @param c color to use
     */
    private void fillDisk(double x, double y, double d, Color c) {
        double r = d / 2;
        ctx.setFill(c);
        ctx.fillOval(x - r, y - r, d, d);
    }

    /**
     * Computes the on-screen diameter of a CelestialObject
     *
     * @param m   magnitude of the CelestialObject
     * @param p   projection used
     * @param ptc plane-to-canvas transformation to apply
     * @return the on-screen diameter of the CelestialObject
     */
    private static double transformedDiameter(double m, StereographicProjection p, Transform ptc) {
        double clippedM = MAG_INTERVAL.clip(m);
        double factor = (99 - 17 * clippedM) / 140d;
        double diameter = factor * p.applyToAngle(ZERO_FIVE_DEG_TO_RAD);
        Point2D size = ptc.deltaTransform(diameter, diameter);
        return size.getX();
    }
}

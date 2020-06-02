package ch.epfl.rigel.gui;

import ch.epfl.rigel.astronomy.CelestialObject;
import ch.epfl.rigel.astronomy.ObservedSky;
import ch.epfl.rigel.astronomy.StarCatalogue;
import ch.epfl.rigel.coordinates.CartesianCoordinates;
import ch.epfl.rigel.coordinates.HorizontalCoordinates;
import ch.epfl.rigel.coordinates.StereographicProjection;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.ClosedInterval;
import ch.epfl.rigel.math.RightOpenInterval;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableDoubleValue;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.transform.NonInvertibleTransformException;
import javafx.scene.transform.Transform;

import java.util.Optional;

/**
 * Manager for the sky canvas
 *
 * @author Bastien Faivre (310929)
 * @author Kamil Mellouk (312327)
 */
@SuppressWarnings("unused")
public class SkyCanvasManager {

    private static final RightOpenInterval RIGHT_OPEN_INTERVAL_0_TO_360 = RightOpenInterval.of(0, 360);
    private static final ClosedInterval CLOSED_INTERVAL_5_TO_90 = ClosedInterval.of(5, 90);

    private final Canvas canvas;
    private final SkyCanvasPainter painter;

    private final ObservableValue<ObservedSky> observedSky;
    private final ObservableValue<StereographicProjection> projection;
    private final ObservableValue<Transform> planeToCanvas;
    private final ObjectProperty<Point2D> mousePosition =
            new SimpleObjectProperty<>(Point2D.ZERO);
    private final ObservableValue<CelestialObject> objUnderMouse;
    private final ObservableValue<HorizontalCoordinates> mouseHorPos;
    private final ObservableDoubleValue mouseAzDeg;
    private final ObservableDoubleValue mouseAltDeg;

    private boolean stars, asterisms, planets, sun, moon, horizon, cardinalPoints, atmosphere;

    private boolean isNight;


    /**
     * Constructor of a sky canvas manager
     *
     * @param starCatalogue         the catalogue to use
     * @param dateTimeBean          the date time bean
     * @param observerLocationBean  the observer location bean
     * @param viewingParametersBean the viewing parameters bean
     */
    public SkyCanvasManager(StarCatalogue starCatalogue,
                            DateTimeBean dateTimeBean,
                            ObserverLocationBean observerLocationBean,
                            ViewingParametersBean viewingParametersBean) {

        canvas = new Canvas();
        painter = new SkyCanvasPainter(canvas);

        stars = true;
        asterisms = true;
        planets = true;
        sun = true;
        moon = true;
        horizon = true;
        cardinalPoints = true;
        atmosphere = true;

        //-----------------------------------------------------------------------------
        // Events
        //-----------------------------------------------------------------------------

        canvas.setOnMousePressed(
                e -> {
                    if (e.isPrimaryButtonDown()) canvas.requestFocus();
                }
        );

        canvas.setOnMouseMoved(
                e -> mousePosition.setValue(new Point2D(e.getX(), e.getY()))
        );

        canvas.setOnScroll(
                e -> viewingParametersBean.setFieldOfViewDeg(
                        viewingParametersBean.getFieldOfViewDeg() +
                                (Math.abs(e.getDeltaX()) > Math.abs(e.getDeltaY()) ? e.getDeltaX() : e.getDeltaY())
                )
        );

        canvas.setOnKeyPressed(
                e -> {
                    HorizontalCoordinates center = viewingParametersBean.getCenter();
                    switch (e.getCode()) {
                        case LEFT:
                            viewingParametersBean.setCenter(centerWithAzDiff(center, -10));
                            break;
                        case RIGHT:
                            viewingParametersBean.setCenter(centerWithAzDiff(center, 10));
                            break;
                        case UP:
                            viewingParametersBean.setCenter(centerWithAltDiff(center, 5));
                            break;
                        case DOWN:
                            viewingParametersBean.setCenter(centerWithAltDiff(center, -5));
                            break;
                        case DIGIT1:
                            stars = !stars;
                            updateSky(stars, asterisms, planets, sun, moon, horizon, cardinalPoints, isNight, atmosphere);
                            break;
                        case DIGIT2:
                            asterisms = !asterisms;
                            updateSky(stars, asterisms, planets, sun, moon, horizon, cardinalPoints, isNight, atmosphere);
                            break;
                        case DIGIT3:
                            planets = !planets;
                            updateSky(stars, asterisms, planets, sun, moon, horizon, cardinalPoints, isNight, atmosphere);
                            break;
                        case DIGIT4:
                            sun = !sun;
                            updateSky(stars, asterisms, planets, sun, moon, horizon, cardinalPoints, isNight, atmosphere);
                            break;
                        case DIGIT5:
                            moon = !moon;
                            updateSky(stars, asterisms, planets, sun, moon, horizon, cardinalPoints, isNight, atmosphere);
                            break;
                        case DIGIT6:
                            horizon = !horizon;
                            updateSky(stars, asterisms, planets, sun, moon, horizon, cardinalPoints, isNight, atmosphere);
                            break;
                        case DIGIT7:
                            cardinalPoints = !cardinalPoints;
                            updateSky(stars, asterisms, planets, sun, moon, horizon, cardinalPoints, isNight, atmosphere);
                            break;
                        case DIGIT8:
                            atmosphere = !atmosphere;
                            updateSky(stars, asterisms, planets, sun, moon, horizon, cardinalPoints, isNight, atmosphere);
                            break;
                    }
                    e.consume();
                }
        );

        canvas.setOnMouseClicked(m -> {
            if (!canvas.isFocused()) canvas.requestFocus();
            switch (m.getButton()) {
                case MIDDLE:
                    viewingParametersBean.setFieldOfViewDeg(100);
                    break;
                case SECONDARY:
                    CelestialObject objUnderMouse = getObjUnderMouse();
                    if (objUnderMouse != null) {
                        GraphicsContext ctx = canvas.getGraphicsContext2D();
                        ctx.setFill(Color.DARKBLUE);
                        ctx.fillRect(m.getX(), m.getY(), 230, 65);
                        ctx.setStroke(Color.WHITE);
                        ctx.setLineWidth(1);
                        ctx.strokeText(" Name : " + objUnderMouse.info() + "\n" +
                                        " Position : " + objUnderMouse.equatorialPos() + "\n" +
                                        " Angular Size : " + objUnderMouse.angularSize() + "\n" +
                                        " Magnitude : " + objUnderMouse.magnitude(),
                                m.getX(), m.getY());
                    }
                    break;
            }
        });

        //-----------------------------------------------------------------------------
        // Bindings and properties
        //-----------------------------------------------------------------------------

        projection = Bindings.createObjectBinding(
                () -> new StereographicProjection(viewingParametersBean.getCenter()),
                viewingParametersBean.centerProperty()
        );

        projection.addListener(
                (p, o, n) -> updateSky(stars, asterisms, planets, sun, moon, horizon, cardinalPoints, isNight, atmosphere)
        );

        observedSky = Bindings.createObjectBinding(
                () -> new ObservedSky(
                        dateTimeBean.getZonedDateTime(),
                        observerLocationBean.getCoordinates(),
                        projection.getValue(),
                        starCatalogue),
                dateTimeBean.dateProperty(),
                dateTimeBean.timeProperty(),
                dateTimeBean.zoneProperty(),
                observerLocationBean.coordinatesProperty(),
                projection
        );

        observedSky.addListener((p, o, n) -> {
                    if (n.sunHorPos().alt() < 0) isNight = true;
                    else isNight = false;
                    updateSky(stars, asterisms, planets, sun, moon, horizon, cardinalPoints, isNight, atmosphere);
                }
        );

        planeToCanvas = Bindings.createObjectBinding(
                () -> {
                    Transform t = Transform.translate(canvas.getWidth() / 2d, canvas.getHeight() / 2d);
                    double scaleFactor = canvas.getWidth() / projection.getValue().applyToAngle(
                            Angle.ofDeg(viewingParametersBean.getFieldOfViewDeg()));
                    Transform s = Transform.scale(scaleFactor, -scaleFactor);
                    return t.createConcatenation(s);
                },
                projection,
                canvas.widthProperty(),
                canvas.heightProperty(),
                viewingParametersBean.fieldOfViewDegProperty()
        );

        planeToCanvas.addListener(
                (p, o, n) -> updateSky(stars, asterisms, planets, sun, moon, horizon, cardinalPoints, isNight, atmosphere)
        );

        objUnderMouse = Bindings.createObjectBinding(
                () -> {
                    try {
                        Optional<CelestialObject> closestObj = observedSky.getValue().objectClosestTo(
                                CartesianCoordinates.of(invertedMousePos().getX(), invertedMousePos().getY()),
                                planeToCanvas.getValue().inverseDeltaTransform(10, 0).getX()
                        );
                        return closestObj.isEmpty() ? null : closestObj.get();
                    } catch (NonInvertibleTransformException e) {
                        return null;
                    }
                },
                observedSky, mousePosition
        );

        mouseHorPos = Bindings.createObjectBinding(
                () -> projection.getValue().inverseApply(CartesianCoordinates.of(
                        invertedMousePos().getX(),
                        invertedMousePos().getY())
                ),
                planeToCanvas, projection, mousePosition
        );

        mouseAzDeg = Bindings.createDoubleBinding(
                () -> mouseHorPos.getValue().azDeg(),
                mouseHorPos
        );

        mouseAltDeg = Bindings.createDoubleBinding(
                () -> mouseHorPos.getValue().altDeg(),
                mouseHorPos
        );
    }

    /**
     * Getter for the canvas
     *
     * @return the canvas
     */
    public Canvas canvas() {
        return canvas;
    }


    /**
     * Getter for the property azimuth in deg of the mouse
     *
     * @return the property azimuth in deg of the mouse
     */
    public ObservableDoubleValue mouseAzDegProperty() {
        return mouseAzDeg;
    }

    /**
     * Getter for the azimuth in deg of the mouse
     *
     * @return the azimuth in deg of the mouse
     */
    public double getMouseAzDeg() {
        return mouseAzDeg.get();
    }

    /**
     * Getter for the property altitude in deg of the mouse
     *
     * @return the property altitude in deg of the mouse
     */
    public ObservableDoubleValue mouseAltDegProperty() {
        return mouseAltDeg;
    }

    /**
     * Getter for the altitude in deg of the mouse
     *
     * @return the altitude in deg of the mouse
     */
    public double getMouseAltDeg() {
        return mouseAltDeg.get();
    }

    /**
     * Getter for the property object under mouse
     *
     * @return the property object under mouse
     */
    public ObservableValue<CelestialObject> objUnderMouseProperty() {
        return objUnderMouse;
    }

    /**
     * Getter for the object under mouse
     *
     * @return the object under mouse
     */
    public CelestialObject getObjUnderMouse() {
        return objUnderMouse.getValue();
    }

    /**
     * Updating all drawable elements of the sky
     *
     * @param stars          boolean indicating whether to draw stars
     * @param asterisms      boolean indicating whether to draw asterisms
     * @param planets        boolean indicating whether to draw planets
     * @param sun            boolean indicating whether to draw the sun
     * @param moon           boolean indicating whether to draw the moon
     * @param horizon        boolean indicating whether to draw the horizon
     * @param cardinalPoints boolean indicating whether to draw cardinal points
     * @param isNight        boolean indicating whether to draw the sky blue or black
     * @param atmosphere     boolean inditating whether to ignore or not the atmosphere
     */
    private void updateSky(boolean stars, boolean asterisms, boolean planets, boolean sun, boolean moon,
                           boolean horizon, boolean cardinalPoints, boolean isNight, boolean atmosphere) {
        ObservedSky observedSky = this.observedSky.getValue();
        StereographicProjection projection = this.projection.getValue();
        Transform planeToCanvas = this.planeToCanvas.getValue();
        if(atmosphere) painter.clear(isNight);
        else painter.clear(true);
        painter.drawStarsAsterisms(observedSky, projection, planeToCanvas, stars, asterisms);
        painter.drawHorizontalGrid(projection, planeToCanvas);
        if (planets) painter.drawPlanets(observedSky, projection, planeToCanvas);
        if (sun) painter.drawSun(observedSky, projection, planeToCanvas);
        if (moon) painter.drawMoon(observedSky, projection, planeToCanvas);
        if (horizon) painter.drawHorizon(projection, planeToCanvas);
        if (cardinalPoints) painter.drawCardinalPoints(projection, planeToCanvas);
    }

    /**
     * Return the new coordinates with the difference if possible
     *
     * @param center the current coordinates
     * @param azDiff the azimuth difference to apply
     * @return the new coordinates with the difference if possible
     */
    private HorizontalCoordinates centerWithAzDiff(HorizontalCoordinates center, double azDiff) {
        return HorizontalCoordinates.ofDeg(
                RIGHT_OPEN_INTERVAL_0_TO_360.reduce(center.azDeg() + azDiff),
                center.altDeg()
        );
    }

    /**
     * Return the new coordinates with the difference if possible
     *
     * @param center  the current coordinates
     * @param altDiff the altitude difference to apply
     * @return the new coordinates with the difference if possible
     */
    private HorizontalCoordinates centerWithAltDiff(HorizontalCoordinates center, double altDiff) {
        return HorizontalCoordinates.ofDeg(
                center.azDeg(),
                CLOSED_INTERVAL_5_TO_90.clip(center.altDeg() + altDiff)
        );
    }

    /**
     * Computing the inverse transformation of planeToCanvas on a Point2D
     *
     * @return the inverted Point2D
     */
    private Point2D invertedMousePos() {
        try {
            return planeToCanvas.getValue().inverseTransform(mousePosition.get());
        } catch (NonInvertibleTransformException e) {
            return Point2D.ZERO;
        }
    }

}

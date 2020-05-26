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
                    }
                    e.consume();
                }
        );

        //-----------------------------------------------------------------------------
        // Bindings and properties
        //-----------------------------------------------------------------------------

        projection = Bindings.createObjectBinding(
                () -> new StereographicProjection(viewingParametersBean.getCenter()),
                viewingParametersBean.centerProperty()
        );

        projection.addListener(
                (p, o, n) -> updateSky()
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

        observedSky.addListener(
                (p, o, n) -> updateSky()
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
                (p, o, n) -> updateSky()
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
     * Update/draw all elements of the sky
     */
    private void updateSky() {
        ObservedSky observedSkyValue = observedSky.getValue();
        StereographicProjection projectionValue = projection.getValue();
        Transform planeToCanvasValue = planeToCanvas.getValue();
        painter.clear();
        painter.drawStars(observedSkyValue, projectionValue, planeToCanvasValue);
        painter.drawPlanets(observedSkyValue, projectionValue, planeToCanvasValue);
        painter.drawSun(observedSkyValue, projectionValue, planeToCanvasValue);
        painter.drawMoon(observedSkyValue, projectionValue, planeToCanvasValue);
        painter.drawHorizon(projectionValue, planeToCanvasValue);
        painter.drawCardinalPoints(projectionValue, planeToCanvasValue);
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

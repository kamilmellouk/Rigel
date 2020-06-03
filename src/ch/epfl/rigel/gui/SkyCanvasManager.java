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
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
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

    private final BooleanProperty drawStars = new SimpleBooleanProperty(true);
    private final BooleanProperty drawAsterisms = new SimpleBooleanProperty(true);
    private final BooleanProperty drawPlanets = new SimpleBooleanProperty(true);
    private final BooleanProperty drawSun = new SimpleBooleanProperty(true);
    private final BooleanProperty drawMoon = new SimpleBooleanProperty(true);
    private final BooleanProperty drawHorizon = new SimpleBooleanProperty(true);
    private final BooleanProperty drawCardinalPoints = new SimpleBooleanProperty(true);
    private final BooleanProperty drawAtmosphere = new SimpleBooleanProperty(false);
    private Color skyColor;

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
                        case DIGIT1:
                            drawStars.set(!drawStars.get());
                            break;
                        case DIGIT2:
                            drawAsterisms.set(!drawAsterisms.get());
                            break;
                        case DIGIT3:
                            drawPlanets.set(!drawPlanets.get());
                            break;
                        case DIGIT4:
                            drawSun.set(!drawSun.get());
                            break;
                        case DIGIT5:
                            drawMoon.set(!drawMoon.get());
                            break;
                        case DIGIT6:
                            drawHorizon.set(!drawHorizon.get());
                            break;
                        case DIGIT7:
                            drawCardinalPoints.set(!drawCardinalPoints.get());
                            break;
                        case DIGIT8:
                            drawAtmosphere.set(!drawAtmosphere.get());
                            break;
                    }
                    updateSky();
                    e.consume();
                }
        );

        canvas.setOnMouseClicked(e -> {
            if (!canvas.isFocused()) canvas.requestFocus();
            updateSky();
            switch (e.getButton()) {
                case MIDDLE:
                    viewingParametersBean.setFieldOfViewDeg(100);
                    break;
                case SECONDARY:
                    // display information of the object under mouse
                    CelestialObject objUnderMouse = getObjUnderMouse();
                    if (objUnderMouse != null) {
                        GraphicsContext ctx = canvas.getGraphicsContext2D();
                        ctx.setFill(Color.valueOf("#373e43"));
                        ctx.fillRect(e.getX(), e.getY(), 230, 65);
                        ctx.setStroke(Color.WHITE);
                        ctx.setLineWidth(1);
                        ctx.strokeText(" Name : " + objUnderMouse.info() + "\n" +
                                        " Position : " + objUnderMouse.equatorialPos() + "\n" +
                                        " Angular Size : " + objUnderMouse.angularSize() + "\n" +
                                        " Magnitude : " + objUnderMouse.magnitude(),
                                e.getX(), e.getY());
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

        observedSky.addListener((p, o, n) -> {
                    // define the color of the sky depending on the vertical position of the sun
                    double altDeg = n.sunHorPos().altDeg();
                    if (altDeg <= -20) {
                        skyColor = Color.BLACK;
                    } else if (altDeg >= 20) {
                        skyColor = Color.rgb(0, 195, 255);
                    } else {
                        int greenValue = (int) ((195 * (altDeg + 20)) / 40d);
                        int blueValue = (int) ((255 * (altDeg + 20)) / 40d);
                        skyColor = Color.rgb(0, greenValue, blueValue);
                    }
                    updateSky();
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

        drawStars.addListener((p, o, n) -> updateSky());
        drawAsterisms.addListener((p, o, n) -> updateSky());
        drawPlanets.addListener((p, o, n) -> updateSky());
        drawSun.addListener((p, o, n) -> updateSky());
        drawMoon.addListener((p, o, n) -> updateSky());
        drawHorizon.addListener((p, o, n) -> updateSky());
        drawCardinalPoints.addListener((p, o, n) -> updateSky());
        drawAtmosphere.addListener((p, o, n) -> updateSky());
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
     */
    private void updateSky() {
        ObservedSky observedSky = this.observedSky.getValue();
        StereographicProjection projection = this.projection.getValue();
        Transform planeToCanvas = this.planeToCanvas.getValue();
        // draw elements depending on the display settings
        if (drawAtmosphere.get()) painter.clear(skyColor);
        else painter.clear(Color.BLACK);
        painter.drawStarsAsterisms(observedSky, projection, planeToCanvas, drawStars.get(), drawAsterisms.get());
        //painter.drawHorizontalGrid(projection, planeToCanvas);
        if (drawPlanets.get()) painter.drawPlanets(observedSky, projection, planeToCanvas);
        if (drawSun.get()) painter.drawSun(observedSky, projection, planeToCanvas);
        if (drawMoon.get()) painter.drawMoon(observedSky, projection, planeToCanvas);
        if (drawHorizon.get()) painter.drawHorizon(projection, planeToCanvas);
        if (drawCardinalPoints.get()) painter.drawCardinalPoints(projection, planeToCanvas);
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

    /**
     * Getter for the drawStarsProperty
     *
     * @return the drawStarsProperty
     */
    public BooleanProperty drawStarsProperty() {
        return drawStars;
    }

    /**
     * Getter for the drawAsterismsProperty
     *
     * @return the drawAsterismsProperty
     */
    public BooleanProperty drawAsterismsProperty() {
        return drawAsterisms;
    }

    /**
     * Getter for the drawPlanetsProperty
     *
     * @return the drawPlanetsProperty
     */
    public BooleanProperty drawPlanetsProperty() {
        return drawPlanets;
    }

    /**
     * Getter for the drawSunProperty
     *
     * @return the drawSunProperty
     */
    public BooleanProperty drawSunProperty() {
        return drawSun;
    }

    /**
     * Getter for the drawMoonProperty
     *
     * @return the drawMoonProperty
     */
    public BooleanProperty drawMoonProperty() {
        return drawMoon;
    }

    /**
     * Getter for the drawHorizonProperty
     *
     * @return the drawHorizonProperty
     */
    public BooleanProperty drawHorizonProperty() {
        return drawHorizon;
    }

    /**
     * Getter for the drawCardinalPointsProperty
     *
     * @return the drawCardinalPointsProperty
     */
    public BooleanProperty drawCardinalPointsProperty() {
        return drawCardinalPoints;
    }

    /**
     * Getter for the drawAtmosphereProperty
     *
     * @return the drawAtmosphereProperty
     */
    public BooleanProperty drawAtmosphereProperty() {
        return drawAtmosphere;
    }
}

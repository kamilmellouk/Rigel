package ch.epfl.rigel.gui;

import ch.epfl.rigel.astronomy.CelestialObject;
import ch.epfl.rigel.astronomy.ObservedSky;
import ch.epfl.rigel.astronomy.StarCatalogue;
import ch.epfl.rigel.coordinates.CartesianCoordinates;
import ch.epfl.rigel.coordinates.HorizontalCoordinates;
import ch.epfl.rigel.coordinates.StereographicProjection;
import ch.epfl.rigel.math.ClosedInterval;
import ch.epfl.rigel.math.RightOpenInterval;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableDoubleValue;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.KeyCode;
import javafx.scene.transform.Transform;

import java.util.Optional;

/**
 * @author Bastien Faivre (310929)
 * @author Kamil Mellouk (312327)
 */

public class SkyCanvasManager {

    private static final RightOpenInterval RIGHT_OPEN_INTERVAL_0_TO_360 = RightOpenInterval.of(0, 360);
    private static final ClosedInterval CLOSED_INTERVAL_5_TO_90 = ClosedInterval.of(5, 90);

    private final Canvas canvas;
    private final SkyCanvasPainter painter;

    private final ObservableValue<ObservedSky> observedSky;
    private final ObservableValue<StereographicProjection> projection;
    private final ObservableValue<Transform> planeToCanvas;
    private final ObjectProperty<CartesianCoordinates> mousePosition = new SimpleObjectProperty<>(CartesianCoordinates.of(0, 0));
    private final ObservableValue<CelestialObject> objectUnderMouse;
    private final ObservableValue<HorizontalCoordinates> mouseHorizontalPosition;
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
                e -> mousePosition.setValue(CartesianCoordinates.of(e.getX(), e.getY()))
        );

        canvas.setOnScroll(
                e -> viewingParametersBean.setFieldOfViewDeg(
                        viewingParametersBean.getFieldOfViewDeg() + (Math.abs(e.getDeltaX()) > Math.abs(e.getDeltaY()) ? e.getDeltaX() : e.getDeltaY())
                )
        );

        canvas.setOnKeyPressed(
                e -> {
                    HorizontalCoordinates center = viewingParametersBean.getCenter();
                    switch (e.getCode()) {
                        case LEFT:
                            System.out.println("LEFT");
                            //viewingParametersBean.setCenter(centerWithAzDifference(center, -10));
                        case RIGHT:
                            System.out.println("RIGHT");
                            //viewingParametersBean.setCenter(centerWithAzDifference(center, 10));
                        case UP:
                            System.out.println("UP");
                            //viewingParametersBean.setCenter(centerWithAltDifference(center, 5));
                        case DOWN:
                            System.out.println("DOWN");
                            //viewingParametersBean.setCenter(centerWithAltDifference(center, -5));
                    }
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
                () -> new ObservedSky(dateTimeBean.getZonedDateTime(), observerLocationBean.getCoordinates(), projection.getValue(), starCatalogue),
                dateTimeBean.dateProperty(),
                dateTimeBean.timeProperty(),
                dateTimeBean.zoneProperty(),
                observerLocationBean.coordinatesProperty(),
                projection
        );
        observedSky.addListener(
                (p, o, n) -> updateSky()
        );

        // TODO: 28/04/2020 How do we create the Tranform ?
        planeToCanvas = Bindings.createObjectBinding(
                () -> Transform.affine(1300, 0, 0, -1300, 400, 300),
                projection, canvas.widthProperty(), canvas.heightProperty(), viewingParametersBean.fieldOfViewDegProperty()
        );
        planeToCanvas.addListener(
                (p, o, n) -> updateSky()
        );


        // TODO: 28/04/2020 what is 10 units on the canvas ?
        objectUnderMouse = Bindings.createObjectBinding(
                () -> {
                    Optional<CelestialObject> objectClosest = observedSky.getValue().objectClosestTo(mousePosition.getValue(), 100);
                    return objectClosest.isEmpty() ? null : objectClosest.get();
                },
                observedSky, mousePosition
        );

        mouseHorizontalPosition = Bindings.createObjectBinding(
                () -> {
                    Point2D inverseTransformedMousePosition = planeToCanvas.getValue().inverseTransform(mousePosition.getValue().x(), mousePosition.getValue().y());
                    HorizontalCoordinates horizontalPosition = projection.getValue().inverseApply(CartesianCoordinates.of(inverseTransformedMousePosition.getX(), inverseTransformedMousePosition.getY()));
                    return HorizontalCoordinates.of(
                            horizontalPosition.az(),
                            horizontalPosition.alt()
                    );
                },
                planeToCanvas, projection, mousePosition
        );

        mouseAzDeg = Bindings.createDoubleBinding(
                () -> mouseHorizontalPosition.getValue().azDeg(),
                mouseHorizontalPosition
        );

        mouseAltDeg = Bindings.createDoubleBinding(
                () -> mouseHorizontalPosition.getValue().altDeg(),
                mouseHorizontalPosition
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

    // TODO: 28/04/2020 Any setter for these 3 properties ?
    // TODO: 28/04/2020 Do the other bindings need getter and setter too ?

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
    public ObservableValue<CelestialObject> objectUnderMouseProperty() {
        return objectUnderMouse;
    }

    /**
     * Getter for the object under mouse
     *
     * @return the object under mouse
     */
    public CelestialObject getObjectUnderMouse() {
        return objectUnderMouse.getValue();
    }

    private void updateSky() {
        painter.clear();
        painter.drawStars(observedSky.getValue(), projection.getValue(), planeToCanvas.getValue());
        painter.drawPlanets(observedSky.getValue(), projection.getValue(), planeToCanvas.getValue());
        painter.drawSun(observedSky.getValue(), projection.getValue(), planeToCanvas.getValue());
        painter.drawMoon(observedSky.getValue(), projection.getValue(), planeToCanvas.getValue());
        painter.drawHorizon(projection.getValue(), planeToCanvas.getValue());
        painter.drawCardinalPoints(projection.getValue(), planeToCanvas.getValue());
    }

    /**
     * Return the new coordinates with the difference if possible
     *
     * @param center       the current coordinates
     * @param azDifference the azimuth difference to apply
     * @return the new coordinates with the difference if possible
     */
    private HorizontalCoordinates centerWithAzDifference(HorizontalCoordinates center, double azDifference) {
        double newAz = center.azDeg() + azDifference;
        return HorizontalCoordinates.ofDeg(
                RIGHT_OPEN_INTERVAL_0_TO_360.contains(newAz) ? newAz : center.azDeg(),
                center.altDeg()
        );
    }

    /**
     * Return the new coordinates with the difference if possible
     *
     * @param center        the current coordinates
     * @param altDifference the altitude difference to apply
     * @return the new coordinates with the difference if possible
     */
    private HorizontalCoordinates centerWithAltDifference(HorizontalCoordinates center, double altDifference) {
        double newAlt = center.altDeg() + altDifference;
        return HorizontalCoordinates.ofDeg(
                center.azDeg(),
                CLOSED_INTERVAL_5_TO_90.clip(newAlt)
        );
    }

}

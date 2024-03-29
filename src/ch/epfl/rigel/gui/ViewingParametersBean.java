package ch.epfl.rigel.gui;

import ch.epfl.rigel.coordinates.HorizontalCoordinates;
import ch.epfl.rigel.math.ClosedInterval;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 * Bean for the viewing parameters
 *
 * @author Bastien Faivre (310929)
 * @author Kamil Mellouk (312327)
 */
public class ViewingParametersBean {

    private static final ClosedInterval CLOSED_INTERVAL_30_TO_150 = ClosedInterval.of(30, 150);

    private final DoubleProperty fieldOfViewDeg = new SimpleDoubleProperty();
    private final ObjectProperty<HorizontalCoordinates> center = new SimpleObjectProperty<>(null);

    /**
     * Getter for the property field of view in deg
     *
     * @return the property field of view in deg
     */
    public DoubleProperty fieldOfViewDegProperty() {
        return fieldOfViewDeg;
    }

    /**
     * Getter for the field of view in deg
     *
     * @return the field of view in deg
     */
    public double getFieldOfViewDeg() {
        return fieldOfViewDeg.get();
    }

    /**
     * Setter for the property field of view in deg
     *
     * @param fieldOfViewDeg the new field of view in deg
     */
    public void setFieldOfViewDeg(double fieldOfViewDeg) {
        this.fieldOfViewDeg.setValue(CLOSED_INTERVAL_30_TO_150.clip(fieldOfViewDeg));
    }

    /**
     * Getter for the property center
     *
     * @return the property center
     */
    public ObjectProperty<HorizontalCoordinates> centerProperty() {
        return center;
    }

    /**
     * Getter for the coordinates of the center
     *
     * @return the coordinates of the center
     */
    public HorizontalCoordinates getCenter() {
        return center.get();
    }

    /**
     * Setter for the property center
     *
     * @param center the new center
     */
    public void setCenter(HorizontalCoordinates center) {
        this.center.setValue(center);
    }

}

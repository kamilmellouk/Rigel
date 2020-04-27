package ch.epfl.rigel.gui;

import ch.epfl.rigel.coordinates.HorizontalCoordinates;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 * @author Bastien Faivre (310929)
 * @author Kamil Mellouk (312327)
 */

public class ViewingParametersBean {

    private final SimpleDoubleProperty fieldOfViewDeg = new SimpleDoubleProperty();
    private final ObjectProperty<HorizontalCoordinates> center = new SimpleObjectProperty<>(null);

    /**
     * Getter for the property field of view in deg
     *
     * @return the property field of view in deg
     */
    public SimpleDoubleProperty fieldOfViewDegProperty() {
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
        this.fieldOfViewDeg.set(fieldOfViewDeg);
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

package ch.epfl.rigel.gui;

import ch.epfl.rigel.coordinates.GeographicCoordinates;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ObservableValue;


/**
 * @author Bastien Faivre (310929)
 * @author Kamil Mellouk (312327)
 */

public class ObserverLocationBean {

    // TODO: 27/04/2020 bidirectional binding?
    private final SimpleDoubleProperty lonDeg = new SimpleDoubleProperty();
    private final SimpleDoubleProperty latDeg = new SimpleDoubleProperty();
    // TODO: 27/04/2020 write getter and setter for coordinates too?
    private final ObservableValue<GeographicCoordinates> coordinates = Bindings.createObjectBinding(
            () -> GeographicCoordinates.ofDeg(lonDeg.get(), latDeg.get()),
            lonDeg, latDeg);

    /**
     * Getter for the property longitude in deg
     *
     * @return the property longitude in deg
     */
    public SimpleDoubleProperty lonDegProperty() {
        return lonDeg;
    }

    /**
     * Getter for the longitude in deg
     *
     * @return the longitude in deg
     */
    public double getLonDeg() {
        return lonDeg.get();
    }

    /**
     * Setter for the property longitude in deg
     *
     * @param lonDeg the new longitude in deg
     */
    public void setLonDeg(double lonDeg) {
        this.lonDeg.setValue(lonDeg);
    }

    /**
     * Getter for the property latitude in deg
     *
     * @return the property latitude in deg
     */
    public SimpleDoubleProperty latDegProperty() {
        return latDeg;
    }

    /**
     * Getter for the latitude in deg
     *
     * @return the latitude in deg
     */
    public double getLatDeg() {
        return latDeg.get();
    }

    /**
     * Setter for the property latitude in deg
     *
     * @param latDeg the new latitude in deg
     */
    public void setLatDeg(double latDeg) {
        this.latDeg.setValue(latDeg);
    }

}

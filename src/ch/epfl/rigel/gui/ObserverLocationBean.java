package ch.epfl.rigel.gui;

import ch.epfl.rigel.coordinates.GeographicCoordinates;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ObservableValue;


/**
 * Bean for the observer location
 *
 * @author Bastien Faivre (310929)
 * @author Kamil Mellouk (312327)
 */
public class ObserverLocationBean {

    private final DoubleProperty lonDeg = new SimpleDoubleProperty();
    private final DoubleProperty latDeg = new SimpleDoubleProperty();
    private final ObservableValue<GeographicCoordinates> coordinates = Bindings.createObjectBinding(
            () -> GeographicCoordinates.ofDeg(lonDeg.get(), latDeg.get()),
            lonDeg, latDeg);

    /**
     * Getter for the property longitude in deg
     *
     * @return the property longitude in deg
     */
    public DoubleProperty lonDegProperty() {
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
    public DoubleProperty latDegProperty() {
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

    /**
     * Getter for the property coordinates
     *
     * @return the property coordinates
     */
    public ObservableValue<GeographicCoordinates> coordinatesProperty() {
        return coordinates;
    }

    /**
     * Getter for the coordinates
     *
     * @return the coordinates
     */
    public GeographicCoordinates getCoordinates() {
        return coordinates.getValue();
    }

    /**
     * Setter for the property coordinates
     *
     * @param coordinates the new coordinates
     */
    public void setCoordinates(GeographicCoordinates coordinates) {
        lonDeg.set(coordinates.lonDeg());
        latDeg.set(coordinates.latDeg());
    }

}

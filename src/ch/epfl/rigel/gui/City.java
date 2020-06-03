package ch.epfl.rigel.gui;

import ch.epfl.rigel.coordinates.GeographicCoordinates;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * @author Bastien Faivre (310929)
 * @author Kamil Mellouk (312327)
 */

public final class City {

    private StringProperty name;
    private StringProperty country;
    private GeographicCoordinates coordinates;

    /**
     * Constructor of a city
     *
     * @param name        the name
     * @param country     the corresponding country
     * @param coordinates the coordinates
     */
    public City(String name, String country, GeographicCoordinates coordinates) {
        this.name = new SimpleStringProperty(name);
        this.country = new SimpleStringProperty(country);
        this.coordinates = coordinates;
    }

    /**
     * Getter for the name
     *
     * @return the name
     */
    public String getName() {
        return name.get();
    }

    /**
     * Getter for the name property
     *
     * @return the name property
     */
    public StringProperty nameProperty() {
        return name;
    }

    /**
     * Getter for the country
     *
     * @return the country
     */
    public String getCountry() {
        return country.get();
    }

    /**
     * Getter for the country property
     *
     * @return the country property
     */
    public StringProperty countryProperty() {
        return country;
    }

    /**
     * Getter for the name
     *
     * @return the name
     */
    public String name() {
        return name.get();
    }

    /**
     * Getter for the coordinates
     *
     * @return the coordinates
     */
    public GeographicCoordinates coordinates() {
        return coordinates;
    }

    @Override
    public String toString() {
        return name + ", " + country;
    }
}

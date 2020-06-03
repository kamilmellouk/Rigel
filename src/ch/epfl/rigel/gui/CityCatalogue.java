package ch.epfl.rigel.gui;

import ch.epfl.rigel.coordinates.GeographicCoordinates;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Bastien Faivre (310929)
 * @author Kamil Mellouk (312327)
 */

public final class CityCatalogue {

    // the list containing all the cities
    private final List<City> cities;

    /**
     * Constructor of a city catalogue
     *
     * @param cities the list of cities
     */
    public CityCatalogue(List<City> cities) {
        this.cities = List.copyOf(cities);
    }

    /**
     * Getter for the cities
     *
     * @return the list of the cities
     */
    public List<City> cities() {
        return cities;
    }

    /**
     * Builder of a CityCatalogue
     *
     * @author Bastien Faivre (310929)
     * @author Kamil Mellouk (312327)
     */
    public static final class Builder {

        // the list containing all the cities
        private final List<City> cities;

        // Constructor for the builder
        public Builder() {
            cities = new ArrayList<>();
        }

        /**
         * Add the city to the list
         *
         * @param c the city to add
         * @return the builder
         */
        public Builder addCity(City c) {
            cities.add(c);
            return this;
        }

        /**
         * @param inputStream the input stream
         * @param loader      the loader
         * @return this
         * @throws IOException in case of input/output error
         */
        public CityCatalogue.Builder loadFrom(InputStream inputStream, CityCatalogue.Loader loader) throws IOException {
            loader.load(inputStream, this);
            return this;
        }

        /**
         * Build method for the StarCatalogue
         *
         * @return new CityCatalogue of these cities
         */
        public CityCatalogue build() {
            return new CityCatalogue(cities);
        }
    }

    /**
     * Loader of a CityCatalogue
     *
     * @author Bastien Faivre (310929)
     * @author Kamil Mellouk (312327)
     */
    public interface Loader {

        /**
         * Loading the cities from a text file (to be redefined)
         *
         * @param inputStream input of the file
         * @param builder     where to add the stars
         * @throws IOException in case of any input/output error
         */
        void load(InputStream inputStream, CityCatalogue.Builder builder) throws IOException;
    }

    public enum CityLoader implements CityCatalogue.Loader {
        INSTANCE();

        // column indexes of elements needed
        private static final int nameIndex = 1;
        private static final int latIndex = 2;
        private static final int lonIndex = 3;
        private static final int countryIndex = 4;

        @Override
        public void load(InputStream inputStream, CityCatalogue.Builder builder) throws IOException {
            try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {

                // add all cities
                bufferedReader.lines()
                        .skip(1)
                        .forEach(l -> {
                            String[] columns = l.split(",");
                            builder.addCity(new City(
                                    ignoreQuotes(columns[nameIndex]),
                                    ignoreQuotes(columns[countryIndex]),
                                    GeographicCoordinates.ofDeg(
                                            Double.parseDouble(ignoreQuotes(columns[lonIndex])),
                                            Double.parseDouble((ignoreQuotes(columns[latIndex])))
                                    )
                            ));
                        });

            }
        }

        /**
         * Ignore the quotes of the given string
         *
         * @param s the given string
         * @return the given string without quotes
         */
        private static String ignoreQuotes(String s) {
            return s.substring(1, s.length() - 1);
        }
    }
}

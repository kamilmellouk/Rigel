package ch.epfl.rigel.gui;

import ch.epfl.rigel.astronomy.AsterismLoader;
import ch.epfl.rigel.astronomy.HygDatabaseLoader;
import ch.epfl.rigel.astronomy.Star;
import ch.epfl.rigel.astronomy.StarCatalogue;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;
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
    private final List<City> cities;

    public CityCatalogue(List<City> cities) {
        this.cities = List.copyOf(cities);
    }

    public List<City> cities() {
        return cities;
    }

    public static final class Builder {
        private final List<City> cities;

        public Builder() {
            cities = new ArrayList<>();
        }

        public Builder addCity(City c) {
            cities.add(c);
            return this;
        }

        public List<City> cities() {
            return cities;
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

        @Override
        public void load(InputStream inputStream, CityCatalogue.Builder builder) throws IOException {
            try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
                // skipping the first line (column headers)
                bufferedReader.readLine();
                String s;
                int nameIndex = 1;
                int latIndex = 2;
                int lonIndex = 3;
                int countryIndex = 4;
                while ((s = bufferedReader.readLine()) != null) {
                    // Splitting the line into an array containing its different columns
                    String[] col = s.split(",");

                    builder.addCity(new City(
                            ignoreQuotes(col[nameIndex]),
                            ignoreQuotes(col[countryIndex]),
                            GeographicCoordinates.ofDeg(
                                    Double.parseDouble(ignoreQuotes(col[lonIndex])),
                                    Double.parseDouble((ignoreQuotes(col[latIndex])))
                            )
                    ));
                }
            }
        }

        private static String ignoreQuotes(String s) {
            return s.substring(1, s.length() - 1);
        }
    }
}

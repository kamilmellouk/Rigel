package ch.epfl.rigel.gui;

import ch.epfl.rigel.Preconditions;
import ch.epfl.rigel.math.ClosedInterval;
import javafx.scene.paint.Color;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * Representation of the color of a black body
 *
 * @author Bastien Faivre (310929)
 * @author Kamil Mellouk (312327)
 */

public final class BlackBodyColor {

    private static final String COLOR_FILE = "/bbr_color.txt";
    private static final Map<Double, String> TEMPERATURE_WITH_COLOR = load();
    private static final ClosedInterval CLOSED_INTERVAL_1000_TO_40000 = ClosedInterval.of(1000, 40000);

    /**
     * Private constructor to ensure that the class isn't instantiable
     */
    private BlackBodyColor() {
    }

    /**
     * Return the color corresponding to the given temperature
     *
     * @param temperature the given temperature
     * @return the color corresponding to the given temperature
     * @throws IllegalArgumentException if the temperature doesn't belong to the interval [1000, 40000]
     */
    public static Color colorForTemperature(double temperature) throws IllegalArgumentException {
        // check exception
        Preconditions.checkInInterval(CLOSED_INTERVAL_1000_TO_40000, temperature);

        // round the temperature to the closest hundred
        double closestColor = (double) Math.round(temperature / 100d) * 100;
        return Color.valueOf(TEMPERATURE_WITH_COLOR.get(closestColor));
    }

    /**
     * Return the map containing all temperatures with their associated color
     *
     * @return the map containing all temperatures with their associated color
     * @throws UncheckedIOException in case of input/output exception
     */
    private static Map<Double, String> load() throws UncheckedIOException {
        try (InputStream inputStream = BlackBodyColor.class.getResourceAsStream(COLOR_FILE);
             InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.US_ASCII);
             BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {

            Map<Double, String> loaderMap = new HashMap<>();

            // add all pairs (temperature, color)
            bufferedReader.lines()
                    .filter(l -> !(l.charAt(0) == '#') && l.startsWith("10deg", 10))
                    .forEach(l -> loaderMap.put(Double.parseDouble(l.substring(1, 6)), l.substring(80, 87)));

            return loaderMap;

        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

}

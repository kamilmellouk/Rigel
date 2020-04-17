package ch.epfl.rigel.gui;

import ch.epfl.rigel.Preconditions;
import ch.epfl.rigel.math.ClosedInterval;
import javafx.scene.paint.Color;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Bastien Faivre (310929)
 * @author Kamil Mellouk (312327)
 */

public final class BlackBodyColor {

    private static final String colorFile = "/bbr_color.txt";
    private static final Map<Double, String> temperatureWithColor = load();

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
        Preconditions.checkInInterval(ClosedInterval.of(1000, 40000), temperature);

        // round the temperature to the closest hundred
        double closestColor = (double) Math.round(temperature / 100d) * 100;
        return Color.valueOf(temperatureWithColor.get(closestColor));
    }

    /**
     * Return the map containing all temperatures with their associated color
     *
     * @return the map containing all temperatures with their associated color
     * @throws UncheckedIOException in case of input/output exception
     */
    private static Map<Double, String> load() throws UncheckedIOException {
        try (InputStream inputStream = BlackBodyColor.class.getResourceAsStream(colorFile);
             InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.US_ASCII);
             BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {

            Map<Double, String> loaderMap = new HashMap<>();

            // add all pairs (temperature, color)
            String s;
            while ((s = bufferedReader.readLine()) != null) {
                if (!(s.charAt(0) == '#') && s.startsWith("10deg", 10)) {
                    double temperature = Double.parseDouble(s.substring(1, 6));
                    String color = s.substring(80, 87);
                    loaderMap.put(temperature, color);
                }
            }

            return loaderMap;

        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

}

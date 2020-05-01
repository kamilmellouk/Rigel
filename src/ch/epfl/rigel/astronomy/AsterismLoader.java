package ch.epfl.rigel.astronomy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * Enum designed to load asterism onto a StarCatalogue
 *
 * @author Bastien Faivre (310929)
 * @author Kamil Mellouk (312327)
 */
public enum AsterismLoader implements StarCatalogue.Loader {
    INSTANCE();

    @Override
    public void load(InputStream inputStream, StarCatalogue.Builder builder) throws IOException {
        // Map containing an hipparcos ID with its corresponding star
        Map<Integer, Star> hipWithStar = new HashMap<>();
        builder.stars().forEach(star -> hipWithStar.put(star.hipparcosId(), star));

        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.US_ASCII))) {
            String s;
            while ((s = bufferedReader.readLine()) != null) {

                // array containing each element of the line
                String[] tabLine = s.split(",");
                int lineLength = tabLine.length;

                // the list that will contain the stars
                List<Star> starList = new ArrayList<>(lineLength);

                // add the stars
                for (String value : tabLine) {
                    int hipId = Integer.parseInt(value);
                    starList.add(hipWithStar.get(hipId));
                }

                builder.addAsterism(new Asterism(starList));
            }
        }
    }

}

package ch.epfl.rigel.astronomy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * Enum designed to loading asterisms onto a StarCatalogue
 *
 * @author Bastien Faivre (310929)
 * @author Kamil Mellouk (312327)
 */
public enum AsterismLoader implements StarCatalogue.Loader {
    INSTANCE();

    @Override
    public void load(InputStream inputStream, StarCatalogue.Builder builder) throws IOException {
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.US_ASCII);
        try (BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {
            String s;
            while ((s = bufferedReader.readLine()) != null) {

                String[] line = s.split(",");
                int lineLength = line.length;
                List<Star> starList = new ArrayList<>(lineLength);

                for (String value : line) {
                    int hipparcosId = Integer.parseInt(value);
                    for (Star star : builder.stars()) {
                        if (star.hipparcosId() == hipparcosId) {
                            starList.add(star);
                        }
                    }
                }

                builder.addAsterism(new Asterism(starList));
            }
        }
    }

}

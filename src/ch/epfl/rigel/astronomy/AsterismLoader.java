package ch.epfl.rigel.astronomy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
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

                // the number of stars in the line
                int lineLength = s.split(",").length;

                // the list that will contain the stars
                List<Star> starList = new ArrayList<>(lineLength);

                // add the stars
                for (int i = 0; i < lineLength; i++) {
                    int hipparcosId = Integer.parseInt(s.split(",")[i]);
                    for (Star star : builder.stars()) {
                        if (star.hipparcosId() == hipparcosId) {
                            starList.add(star);
                        }
                    }
                }

                // add the asterism to the builder
                builder.addAsterism(new Asterism(starList));
            }
        }
    }

}

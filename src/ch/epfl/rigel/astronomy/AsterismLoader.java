package ch.epfl.rigel.astronomy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Mohamed Kamil MELLOUK
 * 25.03.20
 */
public enum AsterismLoader implements StarCatalogue.Loader {
    INSTANCE();

    @Override
    public void load(InputStream inputStream, StarCatalogue.Builder builder) throws IOException {

        InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.US_ASCII);

        try (BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {
            String s;
            while ((s = bufferedReader.readLine()) != null) {

                Map<Integer, Star> starHipMap = new HashMap<>();
                List<Integer> starHipList = new ArrayList<>(s.split(",").length);
                List<Star> starList = new ArrayList<>(s.split(",").length);

                for (int hip : starHipList) {
                    for (Star star : builder.stars()) {
                        if (star.hipparcosId() == hip) {
                            starHipMap.put(hip, star);
                        }
                    }
                }

                builder.addAsterism(new Asterism((List<Star>) starHipMap.values()));
            }
        }
    }

}

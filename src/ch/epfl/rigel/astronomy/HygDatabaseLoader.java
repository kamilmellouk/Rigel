package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EquatorialCoordinates;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.StandardCharsets;

/**
 * @author Bastien Faivre (310929)
 * @author Kamil Mellouk (312327)
 */
public enum HygDatabaseLoader implements StarCatalogue.Loader{
    INSTANCE();

    @Override
    public void load(InputStream inputStream, StarCatalogue.Builder builder) throws IOException {
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.US_ASCII);
        try (BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {
            String s = bufferedReader.readLine();
            while ((s = bufferedReader.readLine()) != null) {


                Star star = new Star(
                        !s.isEmpty() ? Integer.parseInt(s.split(",")[HygDatabaseColumnIndex.HIP.ordinal()]) : 0,
                        !s.isEmpty() ?
                                s.split(",")[HygDatabaseColumnIndex.PROPER.ordinal()]
                                : !s.split(",")[HygDatabaseColumnIndex.BAYER.ordinal()].isEmpty() ?
                                s.split(",")[HygDatabaseColumnIndex.BAYER.ordinal()] + " " + s.split(",")[HygDatabaseColumnIndex.CON.ordinal()]
                                : "? " + s.split(",")[HygDatabaseColumnIndex.CON.ordinal()],
                        EquatorialCoordinates.of((Double.parseDouble(s.split(",")[HygDatabaseColumnIndex.RARAD.ordinal()])),
                                Double.parseDouble(s.split(",")[HygDatabaseColumnIndex.DECRAD.ordinal()])),
                        !s.isEmpty() ? Float.parseFloat(s.split(",")[HygDatabaseColumnIndex.MAG.ordinal()]) : 0f,
                        !s.isEmpty() ? Float.parseFloat(s.split(",")[HygDatabaseColumnIndex.CI.ordinal()]) : 0f
                );
                // TODO why NumberFormatException ?
                System.out.printf("%s, %d, %f, %d, %f \n", star.name(), star.hipparcosId(), star.magnitude(), star.colorTemperature(), star.equatorialPos().ra());
                builder.addStar(star);
            }
        }
    }

    private enum HygDatabaseColumnIndex {
        ID, HIP, HD, HR, GL, BF, PROPER, RA, DEC, DIST, PMRA, PMDEC,
        RV, MAG, ABSMAG, SPECT, CI, X, Y, Z, VX, VY, VZ,
        RARAD, DECRAD, PMRARAD, PMDECRAD, BAYER, FLAM, CON,
        COMP, COMP_PRIMARY, BASE, LUM, VAR, VAR_MIN, VAR_MAX;
    }
}

package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EquatorialCoordinates;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * @author Bastien Faivre (310929)
 * @author Kamil Mellouk (312327)
 */
public enum HygDatabaseLoader implements StarCatalogue.Loader {
    INSTANCE();

    @Override
    public void load(InputStream inputStream, StarCatalogue.Builder builder) throws IOException {
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.US_ASCII);
        try (BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {
            String s = bufferedReader.readLine(); // skipping the first line (column headers)
            while ((s = bufferedReader.readLine()) != null) {
                builder.addStar(new Star(
                        readHip(s),
                        readProper(s),
                        readEquatorialPos(s),
                        readMag(s),
                        readCi(s)
                        )
                );
            }
        }
    }

    /**
     * Getting the Hipparcos number of the Star associated with one given line of the CSV file
     * @param s given line of the CSV file
     * @return Hipparcos number of the Star
     */
    private static int readHip(String s) {
        return !s.split(",")[ColumnIndex.HIP.ordinal()].isEmpty() ? Integer.parseInt(s.split(",")[ColumnIndex.HIP.ordinal()]) : 0;
    }

    /**
     * Getting the proper name (if it exists) of the Star associated with one given line of the CSV file
     * @param s given line of the CSV file
     * @return If it exists, proper name of the star, or the concatenation of the Bayer naming (if it exists, "?" by default) with the Constellation name
     */
    private static String readProper(String s) {
        if(!s.split(",")[ColumnIndex.PROPER.ordinal()].isEmpty()) {
            return s.split(",")[ColumnIndex.PROPER.ordinal()];
        } else if (!s.split(",")[ColumnIndex.BAYER.ordinal()].isEmpty()) {
            return s.split(",")[ColumnIndex.BAYER.ordinal()] + " " + s.split(",")[ColumnIndex.CON.ordinal()];
        } else {
            return "? " + s.split(",")[ColumnIndex.CON.ordinal()];
        }
    }

    /**
     * Getting the equatorialPosition of the Star associated with one given line of the CSV file
     * @param s given line of the CSV file
     * @return EquatorialCoordinates of the Star
     */
    private static EquatorialCoordinates readEquatorialPos(String s) {
        return EquatorialCoordinates.of((Double.parseDouble(s.split(",")[ColumnIndex.RARAD.ordinal()])),
                                         Double.parseDouble(s.split(",")[ColumnIndex.DECRAD.ordinal()]));
    }

    /**
     * Getting the magnitude of the Star associated with one given line of the CSV file
     * @param s given line of the CSV file
     * @return magnitude of the Star
     */
    private static float readMag(String s) {
        return !s.split(",")[ColumnIndex.MAG.ordinal()].isEmpty() ? Float.parseFloat(s.split(",")[ColumnIndex.MAG.ordinal()]) : 0f;
    }

    /**
     * Getting the colorIndex of the Star associated with one given line of the CSV file
     * @param s given line of the CSV file
     * @return colorIndex of the Star
     */
    private static float readCi(String s) {
        return !s.split(",")[ColumnIndex.CI.ordinal()].isEmpty() ? Float.parseFloat(s.split(",")[ColumnIndex.CI.ordinal()]) : 0f;
    }





    private enum ColumnIndex {
        ID, HIP, HD, HR, GL, BF, PROPER, RA, DEC, DIST, PMRA, PMDEC,
        RV, MAG, ABSMAG, SPECT, CI, X, Y, Z, VX, VY, VZ,
        RARAD, DECRAD, PMRARAD, PMDECRAD, BAYER, FLAM, CON,
        COMP, COMP_PRIMARY, BASE, LUM, VAR, VAR_MIN, VAR_MAX;
    }
}

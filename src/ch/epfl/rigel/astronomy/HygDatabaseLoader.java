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
            // skipping the first line (column headers)
            String s = bufferedReader.readLine();

            while ((s = bufferedReader.readLine()) != null) {

                String proper;
                if (!s.split(",")[ColumnIndex.PROPER.ordinal()].isEmpty()) {
                    proper = s.split(",")[ColumnIndex.PROPER.ordinal()];
                } else if (!s.split(",")[ColumnIndex.BAYER.ordinal()].isEmpty()) {
                    proper = s.split(",")[ColumnIndex.BAYER.ordinal()] + " " + s.split(",")[ColumnIndex.CON.ordinal()];
                } else {
                    proper = "? " + s.split(",")[ColumnIndex.CON.ordinal()];
                }


                builder.addStar(new Star(
                                !s.split(",")[ColumnIndex.HIP.ordinal()].isEmpty() ? Integer.parseInt(s.split(",")[ColumnIndex.HIP.ordinal()]) : 0,
                                proper,
                                EquatorialCoordinates.of((Double.parseDouble(s.split(",")[ColumnIndex.RARAD.ordinal()])),
                                                          Double.parseDouble(s.split(",")[ColumnIndex.DECRAD.ordinal()])),
                                !s.split(",")[ColumnIndex.MAG.ordinal()].isEmpty() ? Float.parseFloat(s.split(",")[ColumnIndex.MAG.ordinal()]) : 0f,
                                !s.split(",")[ColumnIndex.CI.ordinal()].isEmpty() ? Float.parseFloat(s.split(",")[ColumnIndex.CI.ordinal()]) : 0f
                                )
                );
            }
        }
    }


    /**
     * enum composed of all column members of the HYG data base
     */
    private enum ColumnIndex {
        ID, HIP, HD, HR, GL, BF, PROPER, RA, DEC, DIST, PMRA, PMDEC,
        RV, MAG, ABSMAG, SPECT, CI, X, Y, Z, VX, VY, VZ,
        RARAD, DECRAD, PMRARAD, PMDECRAD, BAYER, FLAM, CON,
        COMP, COMP_PRIMARY, BASE, LUM, VAR, VAR_MIN, VAR_MAX;
    }
}

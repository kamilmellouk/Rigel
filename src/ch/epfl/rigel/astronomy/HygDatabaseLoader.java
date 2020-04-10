package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EquatorialCoordinates;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * Enum designed to load stars onto a StarCatalogue
 *
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
            bufferedReader.readLine();
            String s;
            while ((s = bufferedReader.readLine()) != null) {

                // Splitting the line into an array containing its different columns
                String[] col = s.split(",");

                // get the name of the star, or a substitute if it is not specified
                String proper;
                if (!col[ColumnIndex.PROPER.ordinal()].isBlank()) {
                    proper = col[ColumnIndex.PROPER.ordinal()];
                } else if (!col[ColumnIndex.BAYER.ordinal()].isBlank()) {
                    proper = col[ColumnIndex.BAYER.ordinal()] + " " + col[ColumnIndex.CON.ordinal()];
                } else {
                    proper = "? " + col[ColumnIndex.CON.ordinal()];
                }

                builder.addStar(new Star(
                                !col[ColumnIndex.HIP.ordinal()].isBlank() ? Integer.parseInt(col[ColumnIndex.HIP.ordinal()]) : 0,
                                proper,
                                EquatorialCoordinates.of((Double.parseDouble(col[ColumnIndex.RARAD.ordinal()])),
                                        Double.parseDouble(col[ColumnIndex.DECRAD.ordinal()])),
                                !col[ColumnIndex.MAG.ordinal()].isBlank() ? (float) Double.parseDouble(col[ColumnIndex.MAG.ordinal()]) : 0f,  // parseDouble instead of parseFloat for precision purposes
                                !col[ColumnIndex.CI.ordinal()].isBlank() ? (float) Double.parseDouble(col[ColumnIndex.CI.ordinal()]) : 0f // parseDouble instead of parseFloat for precision purposes
                        )
                );
            }
        }
    }


    /**
     * Enum designed to reference the 37 columns of HygDatabase
     *
     * @author Bastien Faivre (310929)
     * @author Kamil Mellouk (312327)
     */
    private enum ColumnIndex {
        ID, HIP, HD, HR, GL, BF, PROPER, RA, DEC, DIST, PMRA, PMDEC,
        RV, MAG, ABSMAG, SPECT, CI, X, Y, Z, VX, VY, VZ,
        RARAD, DECRAD, PMRARAD, PMDECRAD, BAYER, FLAM, CON,
        COMP, COMP_PRIMARY, BASE, LUM, VAR, VAR_MIN, VAR_MAX
    }
}

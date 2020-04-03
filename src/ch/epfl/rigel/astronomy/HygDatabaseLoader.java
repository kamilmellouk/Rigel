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
            bufferedReader.readLine();
            String s;
            while ((s = bufferedReader.readLine()) != null) {

                // array containing each element of the line
                String[] tabLine = s.split(",");

                // compute the name of the star depending on given info
                String proper;
                if (!tabLine[ColumnIndex.PROPER.ordinal()].isBlank()) {
                    proper = tabLine[ColumnIndex.PROPER.ordinal()];
                } else if (!tabLine[ColumnIndex.BAYER.ordinal()].isBlank()) {
                    proper = tabLine[ColumnIndex.BAYER.ordinal()] + " " + tabLine[ColumnIndex.CON.ordinal()];
                } else {
                    proper = "? " + tabLine[ColumnIndex.CON.ordinal()];
                }

                builder.addStar(new Star(
                                !tabLine[ColumnIndex.HIP.ordinal()].isBlank() ? Integer.parseInt(tabLine[ColumnIndex.HIP.ordinal()]) : 0,   // hipparcos number
                                proper, // name
                                EquatorialCoordinates.of((Double.parseDouble(tabLine[ColumnIndex.RARAD.ordinal()])),    // equatorial coordinates
                                        Double.parseDouble(tabLine[ColumnIndex.DECRAD.ordinal()])),
                                !tabLine[ColumnIndex.MAG.ordinal()].isBlank() ? (float)Double.parseDouble(tabLine[ColumnIndex.MAG.ordinal()]) : 0f,  // magnitude
                                !tabLine[ColumnIndex.CI.ordinal()].isBlank() ? (float)Double.parseDouble(tabLine[ColumnIndex.CI.ordinal()]) : 0f // color index
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
        COMP, COMP_PRIMARY, BASE, LUM, VAR, VAR_MIN, VAR_MAX
    }
}

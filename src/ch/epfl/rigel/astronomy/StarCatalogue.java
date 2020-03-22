package ch.epfl.rigel.astronomy;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Bastien Faivre (310929)
 * @author Kamil Mellouk (312327)
 */
public final class StarCatalogue {

    private final List<Star> stars;
    private final List<Asterism> asterisms;

    private  Map<Asterism, List<Integer>> asterismIndicesMap;

    /**
     * Constructing a new StarCatalogue
     * @param stars List of all the stars in the catalogue
     * @param asterisms Remarkable groups of stars, which have to be all contained in the list
     */
    public StarCatalogue(List<Star> stars, List<Asterism> asterisms) {

        for(Asterism ast : asterisms) {
            // Constructing a list of integers dedicated to stock the indices of stars forming an asterism
            List<Integer> indices = new ArrayList<Integer>();

            for(Star star : ast.stars()) {
                // Checking if all the stars in the asterisms are references in the list of stars
                if(!stars.contains(star)) throw new IllegalArgumentException();

                // Adding the index of each star in the asterisms to the dedicated list of indices, so we can map it to asterismIndicesMap
                indices.add(stars.indexOf(star));
            }
                // Mapping the asterism to its list of indices
                asterismIndicesMap.put(ast, indices);
        }

        this.stars = List.copyOf(stars);
        this.asterisms = List.copyOf(asterisms);
    }

    /**
     * Getter for stars
     * @return immutable copy of stars
     */
    public List<Star> stars() {
        return List.copyOf(stars);
    }

    /**
     * Getter for asterisms
     * @return immutable copy of asterisms
     */
    public List<Asterism> asterisms() {
        return List.copyOf(asterisms);
    }

    /**
     * Getting the indices of the stars contained in a given asterism
     * @param asterism of which we want to find the star indices
     * @return immutable copy of the list of indices
     */
    public List<Integer> asterismsIndices(Asterism asterism) {
        return List.copyOf(asterismIndicesMap.get(asterism));
    }

    public final class Builder {
        private List<Star> stars;
        private List<Asterism> asterisms;

        private Map<Asterism, List<Integer>> asterismIndicesMap;

        public Builder() {
            this.stars = new ArrayList<>();
            this.asterisms = new ArrayList<>();
            this.asterismIndicesMap = new HashMap<>();
        }

        public Builder addStar(Star star) {
            stars.add(star);
            // TODO implement
            return this;
        }

        public List<Star> stars() {
            // TODO is this really unmodifiable but non immutable ?
            return List.copyOf(stars);
        }

        public Builder addAsterism(Asterism asterism) {
            // TODO check
            asterisms.add(asterism);
            List<Integer> indices = new ArrayList<>();
            for(Star star : asterism.stars()) {
                stars.add(star);
                indices.add(stars.indexOf(star));
            }
            asterismIndicesMap.put(asterism, indices);

            return this;
        }

        public List<Asterism> asterisms() {
            // TODO is this really unmodifiable but non immutable ?
            return List.copyOf(asterisms);
        }

        public Builder loadFrom(InputStream inputStream, Loader loader) throws IOException {
            // TODO implement
            return this;
        }

        public StarCatalogue build() {
            // TODO implement
            return null;
        }

    }

    public interface Loader {

        public default void load(InputStream inputStream, Builder builder) throws IOException {
            // TODO implement
        }
    }
}

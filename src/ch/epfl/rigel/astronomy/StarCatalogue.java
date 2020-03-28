package ch.epfl.rigel.astronomy;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * @author Bastien Faivre (310929)
 * @author Kamil Mellouk (312327)
 */

public final class StarCatalogue {

    // the list of all stars containing in the catalogue
    private final List<Star> stars;
    // the list of all asterisms containing in the catalogue
    private final List<Asterism> asterisms;

    // map an asterism with its stars
    private Map<Asterism, List<Integer>> asterismIndicesMap = new HashMap<>();

    /**
     * Constructor of a StarCatalogue
     *
     * @param stars     List of all the stars in the catalogue
     * @param asterisms Remarkable groups of stars, which have to be all contained in the list
     */
    public StarCatalogue(List<Star> stars, List<Asterism> asterisms) {
        this.stars = List.copyOf(stars);
        this.asterisms = List.copyOf(asterisms);

        for (Asterism asterism : asterisms) {
            // Construct a list of integers dedicated to stock the indices of stars forming the asterism
            List<Integer> indices = new ArrayList<>();

            for (Star star : asterism.stars()) {
                // Check if all the stars in the asterisms are references in the list of stars
                if (!stars.contains(star)) {
                    throw new IllegalArgumentException();
                }
                // Add the index of each star in the asterism to the dedicated list of indices, so we can map it to asterismIndicesMap
                indices.add(stars.indexOf(star));
            }
            // Map the asterism to its list of indices
            asterismIndicesMap.put(asterism, indices);
        }
    }

    /**
     * Getter for stars
     *
     * @return immutable copy of stars
     */
    public List<Star> stars() {
        // TODO: 28/03/2020 check the copyOf()
        return List.copyOf(stars);
    }

    /**
     * Getter for asterisms
     *
     * @return immutable copy of asterisms
     */
    public Set<Asterism> asterisms() {
        // TODO: 28/03/2020 check the copyOf()
        return Set.copyOf(asterisms);
    }

    /**
     * Getter for the indices of the stars contained in a given asterism
     *
     * @param asterism of which we want to find the star indices
     * @return immutable copy of the list of indices
     */
    public List<Integer> asterismIndices(Asterism asterism) {
        // TODO: 28/03/2020 check the copyOf()
        return List.copyOf(asterismIndicesMap.get(asterism));
    }


    public static final class Builder {

        private List<Star> stars;
        private List<Asterism> asterisms;

        /**
         * Default constructor for the StarCatalogue.Builder
         */
        public Builder() {
            this.stars = new ArrayList<>();
            this.asterisms = new ArrayList<>();
        }

        /**
         * Adding a Star to the Builder
         *
         * @param star to add
         * @return this
         */
        public Builder addStar(Star star) {
            stars.add(star);
            return this;
        }

        /**
         * Getter for stars
         *
         * @return unmodifiable but mutable view of stars
         */
        public List<Star> stars() {
            return Collections.unmodifiableList(stars);
        }

        /**
         * Adding an Asterism (and its stars) to the Builder
         *
         * @param asterism to add
         * @return this
         */
        public Builder addAsterism(Asterism asterism) {
            asterisms.add(asterism);
            return this;
        }

        /**
         * Getter for asterisms
         *
         * @return unmodifiable but mutable view of asterisms
         */
        public List<Asterism> asterisms() {
            return Collections.unmodifiableList(asterisms);
        }

        /**
         * @param inputStream
         * @param loader
         * @return
         * @throws IOException
         */
        public Builder loadFrom(InputStream inputStream, Loader loader) throws IOException {
            loader.load(inputStream, this);
            return this;
        }

        /**
         * Building method for the StarCatalogue
         *
         * @return new StarCatalogue with stars and asterisms
         */
        public StarCatalogue build() {
            return new StarCatalogue(stars, asterisms);
        }

    }

    public interface Loader {

        public void load(InputStream inputStream, Builder builder) throws IOException;
    }
}

package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.Preconditions;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * @author Bastien Faivre (310929)
 * @author Kamil Mellouk (312327)
 */

public final class StarCatalogue {

    // The list of all stars containing in the catalogue
    private final List<Star> stars;

    // Map of asterisms with their own stars
    private final Map<Asterism, List<Integer>> asterismMap = new HashMap<>();

    /**
     * Constructor of a StarCatalogue
     *
     * @param stars     List of all the stars in the catalogue
     * @param asterisms Remarkable groups of stars, which have to be all contained in the list
     */
    public StarCatalogue(List<Star> stars, List<Asterism> asterisms) {
        this.stars = List.copyOf(stars);

        // fill in the map with asterisms and their stars (especially their index)
        for (Asterism a : asterisms) {
            List<Integer> indices = new ArrayList<>();
            for (Star s : a.stars()) {
                Preconditions.checkArgument(stars.contains(s));
                indices.add(stars.indexOf(s));
            }
            asterismMap.put(a, indices);
        }

    }

    /**
     * Getter for the stars
     *
     * @return immutable copy of stars
     */
    public List<Star> stars() {
        return stars;
    }

    /**
     * Getter for the asterisms
     *
     * @return immutable copy of asterisms
     */
    public Set<Asterism> asterisms() {
        return Set.copyOf(asterismMap.keySet());
    }

    /**
     * Getter for the indices of the stars contained in a given asterism
     *
     * @param ast of which we want to find the star indices
     * @return immutable copy of the list of indices
     */
    public List<Integer> asterismIndices(Asterism ast) {
        Preconditions.checkArgument(this.asterisms().contains(ast));
        return List.copyOf(asterismMap.get(ast));
    }

    /**
     * Builder of a star catalogue
     */
    public static final class Builder {

        // The list of all stars containing in the catalogue
        private final List<Star> stars;
        // The list of all asterisms containing in the catalogue
        private final List<Asterism> asterisms;

        /**
         * Default constructor for the StarCatalogue.Builder
         */
        public Builder() {
            this.stars = new ArrayList<>();
            this.asterisms = new ArrayList<>();
        }

        /**
         * Add a Star to the Builder
         *
         * @param star to add
         * @return this
         */
        public Builder addStar(Star star) {
            stars.add(star);
            return this;
        }

        /**
         * Getter for the stars
         *
         * @return unmodifiable but mutable view of stars
         */
        public List<Star> stars() {
            return Collections.unmodifiableList(stars);
        }

        /**
         * Add an asterism to the Builder
         *
         * @param asterism to add
         * @return this
         */
        public Builder addAsterism(Asterism asterism) {
            asterisms.add(asterism);
            return this;
        }

        /**
         * Getter for the asterisms
         *
         * @return unmodifiable but mutable view of asterisms
         */
        public List<Asterism> asterisms() {
            return Collections.unmodifiableList(asterisms);
        }

        /**
         * @param inputStream the input stream
         * @param loader      the loader
         * @return this
         * @throws IOException in case of input/output error
         */
        public Builder loadFrom(InputStream inputStream, Loader loader) throws IOException {
            loader.load(inputStream, this);
            return this;
        }

        /**
         * Build method for the StarCatalogue
         *
         * @return new StarCatalogue with stars and asterisms
         */
        public StarCatalogue build() {
            return new StarCatalogue(stars, asterisms);
        }

    }

    /**
     * Loader of a star catalogue
     */
    public interface Loader {

        /**
         * Loading the stars from a text file (to be redefined)
         *
         * @param inputStream input of the file
         * @param builder     where to add the stars
         * @throws IOException in case of any input/output error
         * @see HygDatabaseLoader
         * @see AsterismLoader
         */
        void load(InputStream inputStream, Builder builder) throws IOException;
    }

}

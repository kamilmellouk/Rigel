package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.Preconditions;

import java.util.List;

/**
 * Representation of an Asterism
 *
 * @author Bastien Faivre (310929)
 * @author Kamil Mellouk (312327)
 */

public final class Asterism {

    // List of the stars contained in an asterism (we consider it as final, as id shouldn't change in the next years)
    private final List<Star> stars;

    /**
     * Constructor of an asterism containing the given list of stars
     *
     * @param stars List of the stars contained in the Asterism, not empty
     * @throws IllegalArgumentException if stars is empty
     */
    public Asterism(List<Star> stars) {
        Preconditions.checkArgument(!stars.isEmpty());
        this.stars = List.copyOf(stars);
    }

    /**
     * Getter for the list of stars
     *
     * @return a copy of the list of stars
     */
    public List<Star> stars() {
        return stars;
    }

}

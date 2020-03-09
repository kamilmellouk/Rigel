package ch.epfl.rigel.astronomy;

import java.util.List;

/**
 * @author Bastien Faivre (310929)
 * @author Kamil Mellouk (312327)
 */
public final class Asterism {

    // List of the stars contained in an asterism (we consider it as final, as id shouldn't change in the next years
    private final List<Star> stars;

    /**
     * Creating a new Asterism containing the given list of stars
     * @param stars List of the stars contained in the Asterism, not empty
     */
    Asterism(List<Star> stars) {
        if(stars.isEmpty()) throw new IllegalArgumentException();
        this.stars = List.copyOf(stars);
    }

    /**
     * Getter for the list of stars
     * @return a copy of the list of stars
     */
    public List<Star> stars() {
        return List.copyOf(stars);
    }
}

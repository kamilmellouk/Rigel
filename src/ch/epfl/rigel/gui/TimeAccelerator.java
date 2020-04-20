package ch.epfl.rigel.gui;

import java.time.Duration;
import java.time.ZonedDateTime;

/**
 * @author Bastien Faivre (310929)
 * @author Kamil Mellouk (312327)
 */

@FunctionalInterface
public interface TimeAccelerator {

    // TODO: 20/04/2020 express these two methods with lambdas 
    /*
    static TimeAccelerator continuous(int acceleratingFactor) {
        
    }

    static TimeAccelerator discrete(int advancementFrequency, Duration step) {

    }*/

    /**
     * Compute the simulated time
     *
     * @param initialSimulatedTime the initial simulated time
     * @param elapsedRealTime      the elapsed real time since the beginning of the animation
     *                             expressed in nanoseconds !
     * @return the simulated time
     */
    ZonedDateTime adjust(ZonedDateTime initialSimulatedTime, long elapsedRealTime);

}

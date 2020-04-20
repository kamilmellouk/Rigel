package ch.epfl.rigel.gui;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

/**
 * @author Bastien Faivre (310929)
 * @author Kamil Mellouk (312327)
 */

@FunctionalInterface
public interface TimeAccelerator {

    static TimeAccelerator continuous(int acceleratingFactor) {
        return (initialSimulatedTime, elapsedRealTime) -> initialSimulatedTime.plus(elapsedRealTime * acceleratingFactor, ChronoUnit.NANOS);
    }

    static TimeAccelerator discrete(int advancementFrequency, Duration step) {
        return (initialSimulatedTime, elapsedRealTime) -> initialSimulatedTime.plus((long) Math.floor(advancementFrequency * elapsedRealTime) * step.get(ChronoUnit.NANOS), ChronoUnit.NANOS);
    }

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

package ch.epfl.rigel.gui;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

/**
 * Time Accelerator
 *
 * @author Bastien Faivre (310929)
 * @author Kamil Mellouk (312327)
 */
@FunctionalInterface
public interface TimeAccelerator {

    double SEC_PER_NANOS = 1e-9;

    /**
     * Return a continuous time accelerator
     *
     * @param acceleratingFactor the given accelerating factor
     * @return a continuous time accelerator
     */
    static TimeAccelerator continuous(int acceleratingFactor) {
        return (initialSimulatedTime, elapsedRealTime) ->
                initialSimulatedTime.plus(elapsedRealTime * acceleratingFactor, ChronoUnit.NANOS);
    }

    /**
     * Return a discrete time accelerator
     *
     * @param advancementFrequency the advancement frequency
     * @param step                 the discrete step
     * @return a discrete time accelerator
     */
    static TimeAccelerator discrete(int advancementFrequency, Duration step) {
        return (initialSimulatedTime, elapsedRealTime) ->
                initialSimulatedTime.plus(
                        step.multipliedBy(
                                (long) (advancementFrequency * elapsedRealTime * SEC_PER_NANOS)));
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

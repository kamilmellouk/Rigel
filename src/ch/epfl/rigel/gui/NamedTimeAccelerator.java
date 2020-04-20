package ch.epfl.rigel.gui;

import java.time.Duration;

/**
 * @author Bastien Faivre (310929)
 * @author Kamil Mellouk (312327)
 */

public enum NamedTimeAccelerator {
    TIMES_1("1x", TimeAccelerator.continuous(1)),
    TIMES_30("30x", TimeAccelerator.continuous(30)),
    TIMES_300("300x", TimeAccelerator.continuous(300)),
    TIMES_3000("3000x", TimeAccelerator.continuous(3000)),
    Day("jour", TimeAccelerator.discrete(60, Duration.ofHours(24))),
    SIDEREAL_DAY("jour sidéral", TimeAccelerator.discrete(60, Duration.ofHours(23).plus(Duration.ofMinutes(56).plus(Duration.ofSeconds(4)))));

    private final String name;
    private final TimeAccelerator accelerator;

    /**
     * Constructor of the named time accelerator
     *
     * @param name        the name
     * @param accelerator the type of accelerator
     */
    NamedTimeAccelerator(String name, TimeAccelerator accelerator) {
        this.name = name;
        this.accelerator = accelerator;
    }

    /**
     * Getter for the name
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Getter for the accelerator
     *
     * @return the accelerator
     */
    public TimeAccelerator getAccelerator() {
        return accelerator;
    }

    @Override
    public String toString() {
        return name;
    }
}

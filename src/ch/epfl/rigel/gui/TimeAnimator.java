package ch.epfl.rigel.gui;

import javafx.animation.AnimationTimer;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 * @author Bastien Faivre (310929)
 * @author Kamil Mellouk (312327)
 */

public final class TimeAnimator extends AnimationTimer {

    private final DateTimeBean observationTime;
    private final ObjectProperty<TimeAccelerator> accelerator = new SimpleObjectProperty<>(null);
    private final SimpleBooleanProperty running = new SimpleBooleanProperty(false);
    private long lastNanos;
    private boolean firstHandle;

    /**
     * Constructor of a time animator
     *
     * @param observationTime the observation time
     */
    public TimeAnimator(DateTimeBean observationTime) {
        this.observationTime = observationTime;
        firstHandle = false;
    }

    /**
     * Getter for the property time accelerator
     *
     * @return the property time accelerator
     */
    public ObjectProperty<TimeAccelerator> acceleratorProperty() {
        return accelerator;
    }

    /**
     * Getter for the accelerator
     *
     * @return the accelerator
     */
    public TimeAccelerator getAccelerator() {
        return accelerator.get();
    }

    /**
     * Setter for the property accelerator
     *
     * @param accelerator the new accelerator
     */
    public void setAccelerator(TimeAccelerator accelerator) {
        this.accelerator.setValue(accelerator);
    }

    @Override
    public void start() {
        super.start();
        running.set(true);
    }

    @Override
    public void stop() {
        super.stop();
        running.set(false);
        firstHandle = false;
    }

    @Override
    public void handle(long now) {
        if (!firstHandle) {
            lastNanos = now;
            firstHandle = true;
        }
        observationTime.setZonedDateTime(accelerator.get().adjust(observationTime.getZonedDateTime(), now - lastNanos));
        lastNanos = now;
    }
}

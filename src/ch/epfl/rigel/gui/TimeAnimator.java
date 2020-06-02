package ch.epfl.rigel.gui;

import javafx.animation.AnimationTimer;
import javafx.beans.property.*;

import java.time.ZonedDateTime;

/**
 * Time Animator
 *
 * @author Bastien Faivre (310929)
 * @author Kamil Mellouk (312327)
 */
public final class TimeAnimator extends AnimationTimer {

    private final DateTimeBean dateTimeBean;
    private final ObjectProperty<TimeAccelerator> accelerator = new SimpleObjectProperty<>(null);
    private final BooleanProperty running = new SimpleBooleanProperty(false);
    private ZonedDateTime initialTime;
    // represent the first argument passed in the method handle for the first time
    private long firstNanos;
    private boolean firstHandle;

    /**
     * Constructor of a time animator
     *
     * @param dateTimeBean the observation time
     */
    public TimeAnimator(DateTimeBean dateTimeBean) {
        this.dateTimeBean = dateTimeBean;
        firstHandle = true;
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

    /**
     * Getter for the running property
     *
     * @return running property
     */
    public ReadOnlyBooleanProperty runningProperty() {
        return running;
    }

    /**
     * Starting the time Animation if it is not already started, and stopping it if it is
     */
    public void startStop() {
        if(running.getValue()) stop();
        else start();
    }

    @Override
    public void start() {
        super.start();
        initialTime = dateTimeBean.getZonedDateTime();
        running.set(true);

    }

    @Override
    public void stop() {
        super.stop();
        running.set(false);
        firstHandle = true;
    }

    @Override
    public void handle(long now) {
        if (firstHandle) {
            firstNanos = now;
            firstHandle = false;
        } else {
            dateTimeBean.setZonedDateTime(
                    getAccelerator().adjust(initialTime, now - firstNanos));
        }
    }
}
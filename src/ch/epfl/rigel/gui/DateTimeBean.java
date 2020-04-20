package ch.epfl.rigel.gui;

import javafx.beans.property.ObjectProperty;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * @author Bastien Faivre (310929)
 * @author Kamil Mellouk (312327)
 */

public final class DateTimeBean {

    private ObjectProperty<LocalDate> date;
    private ObjectProperty<LocalTime> time;
    private ObjectProperty<ZoneId> zone;

    /**
     * Getter for the property local date
     *
     * @return the property local date
     */
    public ObjectProperty<LocalDate> dateProperty() {
        return date;
    }

    /**
     * Getter for the property local date
     *
     * @return the property local date
     */
    public LocalDate getDate() {
        return date.get();
    }

    /**
     * Setter for the property local date
     *
     * @param date the new local date
     */
    public void setDate(LocalDate date) {
        this.date.setValue(date);
    }

    /**
     * Getter for the property local time
     *
     * @return the property local time
     */
    public ObjectProperty<LocalTime> timeProperty() {
        return time;
    }

    /**
     * Getter for the property local time
     *
     * @return the property local time
     */
    public LocalTime getTime() {
        return time.get();
    }

    /**
     * Setter for the property local time
     *
     * @param time the new local time
     */
    public void setTime(LocalTime time) {
        this.time.setValue(time);
    }

    /**
     * Getter for the property zone
     *
     * @return the property zone
     */
    public ObjectProperty<ZoneId> zoneProperty() {
        return zone;
    }

    /**
     * Getter for the property zone
     *
     * @return the property zone
     */
    public ZoneId getZone() {
        return zone.get();
    }

    /**
     * Setter for the property zone
     *
     * @param zone the new zone
     */
    public void setZone(ZoneId zone) {
        this.zone.setValue(zone);
    }

    /**
     * Getter for the whole zoned date time
     *
     * @return the whole zoned date time
     */
    public ZonedDateTime getZonedDateTime() {
        return ZonedDateTime.of(getDate(), getTime(), getZone());
    }

    /**
     * Setter for the whole values of the zoned date time
     *
     * @param zonedDateTime the new zoned date time
     */
    public void setZonedDateTime(ZonedDateTime zonedDateTime) {
        setDate(zonedDateTime.toLocalDate());
        setTime(zonedDateTime.toLocalTime());
        setZone(zonedDateTime.getZone());
    }

}

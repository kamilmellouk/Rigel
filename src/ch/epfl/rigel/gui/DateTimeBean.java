package ch.epfl.rigel.gui;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * Bean for the date and time
 *
 * @author Bastien Faivre (310929)
 * @author Kamil Mellouk (312327)
 */
public final class DateTimeBean {

    private final ObjectProperty<LocalDate> date = new SimpleObjectProperty<>(LocalDate.now());
    private final ObjectProperty<LocalTime> time = new SimpleObjectProperty<>(LocalTime.now());
    private final ObjectProperty<ZoneId> zone = new SimpleObjectProperty<>(ZoneId.systemDefault());

    /**
     * Getter for the property local date
     *
     * @return the property local date
     */
    public ObjectProperty<LocalDate> dateProperty() {
        return date;
    }

    /**
     * Getter for the local date
     *
     * @return the local date
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
     * Getter for the local time
     *
     * @return the local time
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
     * Getter for the zone
     *
     * @return the zone
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

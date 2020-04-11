package ch.epfl.rigel.gui;

import javafx.scene.paint.Color;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Bastien Faivre (310929)
 * @author Kamil Mellouk (312327)
 */

public class MyBlackBodyColorTest {

    @Test
    public void staticMapWorks() {
        // to test, create a getter for the map
        /*
        for (Double temp : BlackBodyColor.getMap().keySet()) {
            System.out.println(temp + " " + BlackBodyColor.getMap().get(temp));
        }*/
    }

    @Test
    public void colorForTemperatureWorks() {
        assertEquals(Color.valueOf("#dde6ff"), BlackBodyColor.colorForTemperature(8400));
        assertEquals(Color.valueOf("#9fbeff"), BlackBodyColor.colorForTemperature(30500));
    }

    @Test
    public void colorForTemperatureWorksOnEdgeCases() {
        assertEquals(Color.valueOf("#ff3800"), BlackBodyColor.colorForTemperature(1000));
        assertEquals(Color.valueOf("#9bbcff"), BlackBodyColor.colorForTemperature(40000));
    }

    @Test
    public void colorForTemperatureWorksOnSpecialCases() {
        assertEquals(Color.valueOf("#b9d0ff"), BlackBodyColor.colorForTemperature(13167));
        assertEquals(Color.valueOf("#9dbdff"), BlackBodyColor.colorForTemperature(33824));
        assertEquals(Color.valueOf("#ffd7b1"), BlackBodyColor.colorForTemperature(4250));
    }

    @Test
    public void colorForTemperatureThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> BlackBodyColor.colorForTemperature(100));
        assertThrows(IllegalArgumentException.class, () -> BlackBodyColor.colorForTemperature(46000));
    }

}
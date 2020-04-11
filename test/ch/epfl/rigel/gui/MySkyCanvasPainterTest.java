package ch.epfl.rigel.gui;


import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Bastien Faivre (310929)
 * @author Kamil Mellouk (312327)
 */
public class MySkyCanvasPainterTest {

    @Test
    void magBasedSizeWorksOnRigel() {
        Canvas canvas = new Canvas(800, 300);
        SkyCanvasPainter s = new SkyCanvasPainter(canvas);

        assertEquals(0.00299, s.magBasedSize(0.18), 1e-6);
    }
}

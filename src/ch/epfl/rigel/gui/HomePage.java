package ch.epfl.rigel.gui;

import javafx.geometry.Pos;
import javafx.scene.control.Separator;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

/**
 * @author Bastien Faivre (310929)
 * @author Kamil Mellouk (312327)
 */

public class HomePage {

    // the pane containing the title and the welcome message
    private final VBox pane = new VBox(title(), welcomeMessage(), shortcutsList());

    /**
     * Constructor of a home page
     */
    public HomePage() {
        pane.setId("homePage");
    }

    /**
     * Getter for the border pane
     *
     * @return the border pane
     */
    public VBox getPane() {
        return pane;
    }

    /**
     * Create the title of the home page
     *
     * @return the title
     */
    private GridPane title() {
        Text title = new Text("Rigel");
        title.setId("title");
        GridPane pane = new GridPane();
        pane.getChildren().add(title);
        pane.setAlignment(Pos.CENTER);
        pane.setStyle("-fx-padding: 20pt;");
        return pane;
    }

    /**
     * Creates the welcome message of the home page
     *
     * @return the welcome message
     */
    private GridPane welcomeMessage() {
        Text welcomeMessage = new Text("Welcome ! Please press any key to launch the program.");
        welcomeMessage.setId("welcomeMessage");
        welcomeMessage.setTextAlignment(TextAlignment.CENTER);
        GridPane pane = new GridPane();
        pane.getChildren().add(welcomeMessage);
        pane.setAlignment(Pos.CENTER);
        return pane;
    }

    /**
     * Creates a message listing all the shortcuts
     *
     * @return the shortcuts list
     */
    private GridPane shortcutsList() {
        Text shortcuts = new Text(
                "TAB         : show/hide settings bar" + "\n" +
                        "F           : enable/disable fullscreen mode" + "\n" +
                        "SPACE/ENTER : start/stop animation" + "\n" +
                        "R           : reset animation" + "\n" +
                        "1           : show stars" + "\n" +
                        "2           : show asterisms" + "\n" +
                        "3           : show planets" + "\n" +
                        "4           : show the sun" + "\n" +
                        "5           : show the moon" + "\n" +
                        "6           : show the horizon" + "\n" +
                        "7           : show the atmosphere" + "\n" +
                        "8           : show names"
        );
        shortcuts.setId("shortcutsList");
        shortcuts.setTextAlignment(TextAlignment.LEFT);
        GridPane pane = new GridPane();
        pane.getChildren().add(shortcuts);
        pane.setAlignment(Pos.CENTER);
        return pane;
    }

}

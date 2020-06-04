package ch.epfl.rigel.gui;

import javafx.geometry.Pos;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

/**
 * @author Bastien Faivre (310929)
 * @author Kamil Mellouk (312327)
 */

public class HomePage {

    // the pane containing the title, the welcome message and the shortcuts list
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
        pane.setStyle("-fx-padding: 10pt;");
        return pane;
    }

    /**
     * Creates a message listing all the shortcuts
     *
     * @return the shortcuts list
     */
    private GridPane shortcutsList() {
        // column 1
        Text shortcuts1 = new Text(
                "TAB       : show/hide settings bar" + "\n" +
                        "F           : enable/disable fullscreen mode" + "\n" +
                        "SPACE/ENTER : start/stop animation" + "\n" +
                        "R           : reset animation" + "\n" +
                        "S           : take a screenshot of the sky" + "\n" +
                        "LEFT CLICK  : wikipedia page of the element" + "\n" +
                        "RIGHT CLICK : information window" + "\n" +
                        "MIDDLE CLICK : reset field of view"
        );
        shortcuts1.setId("shortcutsList1");
        shortcuts1.setTextAlignment(TextAlignment.LEFT);
        // column 2
        Text shortcuts2 = new Text(
                "1           : show stars" + "\n" +
                        "2           : show asterisms" + "\n" +
                        "3           : show planets" + "\n" +
                        "4           : show the sun" + "\n" +
                        "5           : show the moon" + "\n" +
                        "6           : show the horizon" + "\n" +
                        "7           : show cardinal points" + "\n" +
                        "8           : show the atmosphere" + "\n" +
                        "9           : show names"
        );
        shortcuts2.setId("shortcutsList2");
        shortcuts2.setTextAlignment(TextAlignment.LEFT);

        HBox shortcuts = new HBox(shortcuts1, shortcuts2);
        shortcuts.setSpacing(50);
        GridPane pane = new GridPane();
        pane.getChildren().addAll(shortcuts);
        pane.setAlignment(Pos.CENTER);
        pane.setStyle("-fx-padding: 10pt;");
        return pane;
    }

}

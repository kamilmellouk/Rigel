package ch.epfl.rigel.gui;

import javafx.geometry.Pos;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

/**
 * @author Bastien Faivre (310929)
 * @author Kamil Mellouk (312327)
 */

public class HomePage {

    private final VBox pane = new VBox(title(), welcomeMessage());

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
     * Create the welcome message of the home page
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
        pane.setStyle("-fx-padding: 20pt;");
        return pane;
    }

}

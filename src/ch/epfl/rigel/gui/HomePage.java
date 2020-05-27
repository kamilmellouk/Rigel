package ch.epfl.rigel.gui;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

/**
 * @author Bastien Faivre (310929)
 * @author Kamil Mellouk (312327)
 */

public class HomePage {

    private final Button startButton = new Button("Start");
    private final BorderPane borderPane = new BorderPane(welcomeMessage(), title(), null, startButton(), null);

    /**
     * Getter for the start button
     *
     * @return the start button
     */
    public Button getStartButton() {
        return startButton;
    }

    /**
     * Getter for the border pane
     *
     * @return the border pane
     */
    public BorderPane getBorderPane() {
        return borderPane;
    }

    /**
     * Create the title of the home page
     *
     * @return the title
     */
    private GridPane title() {
        Text title = new Text("Rigel");
        title.setStyle("-fx-font-size: 100pt;");
        GridPane pane = new GridPane();
        pane.getChildren().add(title);
        pane.setAlignment(Pos.CENTER);
        return pane;
    }

    /**
     * Create the welcome message of the home page
     *
     * @return the welcome message
     */
    private Text welcomeMessage() {
        Text welcomeMessage = new Text("Welcome ! Please press \"Start\" to launch the program.\n" +
                "Note : settings are available after starting the program.");
        welcomeMessage.setStyle("-fx-font-size: 20pt; -fx-padding: 100pt");
        welcomeMessage.setTextAlignment(TextAlignment.CENTER);
        return welcomeMessage;
    }

    private GridPane startButton() {
        startButton.setStyle("-fx-font-size: 20pt; -fx-pref-width: 100pt;");
        startButton.setAlignment(Pos.CENTER);
        GridPane pane = new GridPane();
        pane.getChildren().add(startButton);
        pane.setAlignment(Pos.CENTER);
        return pane;
    }

    /*
    private GridPane displayOptions() {
        Text text = new Text("Display options (can be changed later)");
        VBox options = new VBox(new CheckBox("Stars"),
                new CheckBox("Asterisms"),
                new CheckBox("Planets"),
                new CheckBox("Sun"),
                new CheckBox("Moon"));
        GridPane pane = new GridPane();
        pane.getChildren().add(new VBox(text, options));
        pane.setAlignment(Pos.CENTER);
        return pane;
    }*/

}

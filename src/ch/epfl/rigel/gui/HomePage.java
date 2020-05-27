package ch.epfl.rigel.gui;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

/**
 * @author Bastien Faivre (310929)
 * @author Kamil Mellouk (312327)
 */

public class HomePage {

    private final Button startButton = new Button("Start");
    private final BorderPane borderPane = new BorderPane(null, title(), null, startButton(), null);

    public HomePage() {
    }

    public Button getStartButton() {
        return startButton;
    }

    public BorderPane getBorderPane() {
        return borderPane;
    }

    private GridPane title() {
        Text title = new Text("Rigel");
        title.setStyle("-fx-font-size: 100pt;");
        GridPane pane = new GridPane();
        pane.getChildren().add(title);
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

    private GridPane startButton() {
        Text settings = new Text("Settings are avalaible after starting the program");
        //Button startButton = new Button("Start");
        GridPane pane = new GridPane();
        pane.getChildren().add(new VBox(settings, startButton));
        pane.setAlignment(Pos.CENTER);
        return pane;
    }

}

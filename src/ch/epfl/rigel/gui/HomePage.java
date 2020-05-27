package ch.epfl.rigel.gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Bastien Faivre (310929)
 * @author Kamil Mellouk (312327)
 */

public class HomePage {

    private final BorderPane borderPane = new BorderPane(null, title(), displayOptions(), startButton(), observationLocation());

    private static final ObservableList<String> typeList = FXCollections.observableList(List.of("Cities", "Observation centers", "Universities"));
    private static final ObservableList<String> citiesList = loadCities();
    private static final ObservableList<String> observationCentersList = loadObservationCenters();
    private static final ObservableList<String> universitiesList = loadUniversities();

    public HomePage() {
    }

    public BorderPane getBorderPane() {
        return borderPane;
    }

    private GridPane title() {
        GridPane pane = new GridPane();
        pane.getChildren().add(new Text("Rigel"));
        pane.setAlignment(Pos.CENTER);
        return pane;
    }

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
    }

    private VBox startButton() {
        Text timeSettings = new Text("Time settings are avalaible after starting the program");
        Button startButton = new Button("Start");
        return new VBox(timeSettings, startButton);
    }

    private VBox observationLocation() {
        Text text = new Text("Select the observation location");
        ChoiceBox<String> typeChoicer = new ChoiceBox<>();
        typeChoicer.setItems(typeList);
        ChoiceBox<String> locationChoicer = new ChoiceBox<>();
        locationChoicer.setItems(citiesList);
        return new VBox(text, typeChoicer, locationChoicer);
    }

    private static ObservableList<String> loadCities() throws UncheckedIOException {
        return FXCollections.observableList(new ArrayList<>());
    }

    private static ObservableList<String> loadObservationCenters() throws UncheckedIOException {
        return FXCollections.observableList(new ArrayList<>());
    }

    private static ObservableList<String> loadUniversities() throws UncheckedIOException {
        return FXCollections.observableList(new ArrayList<>());
    }

}

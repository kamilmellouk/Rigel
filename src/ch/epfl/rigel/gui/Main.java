package ch.epfl.rigel.gui;

import ch.epfl.rigel.astronomy.AsterismLoader;
import ch.epfl.rigel.astronomy.HygDatabaseLoader;
import ch.epfl.rigel.astronomy.StarCatalogue;
import ch.epfl.rigel.coordinates.GeographicCoordinates;
import ch.epfl.rigel.coordinates.HorizontalCoordinates;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;
import java.time.ZonedDateTime;

/**
 * @author Bastien Faivre (310929)
 * @author Kamil Mellouk (312327)
 */
public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    private InputStream resourceStream(String resourceName) {
        return getClass().getResourceAsStream(resourceName);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        Pane skyPane = createSkyPane();
        BorderPane mainPane = new BorderPane(skyPane, createControlBar(), null, null, null);

        primaryStage.setTitle("Rigel");
        primaryStage.setMinWidth(800);
        primaryStage.setMinHeight(600);

        primaryStage.show();
        skyPane.requestFocus();
    }

    private HBox createControlBar() {
        TextField lonTextField = new TextField();
        lonTextField.setStyle("-fx-pref-width: 60; -fx-alignment: baseline-right;");
        TextField latTextField = new TextField();
        latTextField.setStyle("-fx-pref-width: 60; -fx-alignment: baseline-right;");
        HBox whereControl = new HBox(
                new Label("Longitude (°) :"), lonTextField,
                new Label("Latitude (°) :"), latTextField
        );
        whereControl.setStyle("-fx-spacing: inherit; -fx-alignment: baseline-left;");

        DatePicker datePicker = new DatePicker();
        //datePicker.getValue() = Bindings.createObjectBinding();
        datePicker.setStyle("-fx-pref-width: 120;");
        HBox whenControl = new HBox(
                new Label("Date :"), datePicker,
                new Label("Heure")
        );
        whenControl.setStyle("-fx-spacing: inherit; -fx-alignment: baseline-left;");


        HBox controlBar = new HBox(whereControl, new Separator());
        controlBar.setStyle("-fx-spacing: 4; -fx-padding: 4;");

        return controlBar;
    }


    private Pane createSkyPane() throws IOException {
        try (InputStream hs = resourceStream("/hygdata_v3.csv");
             InputStream as = resourceStream("/asterisms.txt")) {
            StarCatalogue catalogue = new StarCatalogue.Builder()
                    .loadFrom(hs, HygDatabaseLoader.INSTANCE)
                    .loadFrom(as, AsterismLoader.INSTANCE)
                    .build();

            ZonedDateTime when =
                    ZonedDateTime.parse("2020-01-28T12:15:00+01:00");
            DateTimeBean dateTimeBean = new DateTimeBean();
            dateTimeBean.setZonedDateTime(when);

            ObserverLocationBean observerLocationBean =
                    new ObserverLocationBean();
            observerLocationBean.setCoordinates(
                    GeographicCoordinates.ofDeg(6.57, 46.52));

            ViewingParametersBean viewingParametersBean =
                    new ViewingParametersBean();
            viewingParametersBean.setCenter(
                    HorizontalCoordinates.ofDeg(180.000000000001, 42));
            viewingParametersBean.setFieldOfViewDeg(70);

            SkyCanvasManager canvasManager = new SkyCanvasManager(
                    catalogue,
                    dateTimeBean,
                    observerLocationBean,
                    viewingParametersBean);

            canvasManager.objUnderMouseProperty().addListener(
                    (p, o, n) -> {
                        if (n != null) System.out.println(n);
                    });

            Canvas sky = canvasManager.canvas();
            Pane root = new Pane(sky);

            sky.widthProperty().bind(root.widthProperty());
            sky.heightProperty().bind(root.heightProperty());
            return root;
        }
    }

    private BorderPane createInfoBar() {
        Text fovText = new Text();
        Text objText = new Text();
        Text mousePosText = new Text();
        BorderPane infoBar = new BorderPane(objText, null, mousePosText, null, fovText);
        return infoBar;
    }
}

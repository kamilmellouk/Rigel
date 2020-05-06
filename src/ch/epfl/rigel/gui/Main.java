package ch.epfl.rigel.gui;

import ch.epfl.rigel.astronomy.AsterismLoader;
import ch.epfl.rigel.astronomy.HygDatabaseLoader;
import ch.epfl.rigel.astronomy.StarCatalogue;
import ch.epfl.rigel.coordinates.GeographicCoordinates;
import ch.epfl.rigel.coordinates.HorizontalCoordinates;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.converter.LocalTimeStringConverter;
import javafx.util.converter.NumberStringConverter;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.function.UnaryOperator;

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

    /**
     * Create the control bar
     *
     * @return the control bar
     */
    private HBox createControlBar() {

        // observation position

        TextField lonTextField = new TextField();
        lonTextField.setStyle("-fx-pref-width: 60; -fx-alignment: baseline-right;");
        lonTextField.setTextFormatter(getFormatter(true));

        TextField latTextField = new TextField();
        latTextField.setStyle("-fx-pref-width: 60; -fx-alignment: baseline-right;");
        latTextField.setTextFormatter(getFormatter(false));

        HBox whereControl = new HBox(new Label("Longitude (°) :"), lonTextField, new Label("Latitude (°) :"), latTextField);
        whereControl.setStyle("-fx-spacing: inherit; -fx-alignment: baseline-left;");

        // observation time

        DatePicker datePicker = new DatePicker();

        TextField timeField = new TextField();
        timeField.setStyle("-fx-pref-width: 75; -fx-alignment: baseline-right;");
        DateTimeFormatter hmsFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        LocalTimeStringConverter stringConverter = new LocalTimeStringConverter(hmsFormatter, hmsFormatter);
        TextFormatter<LocalTime> timeFormatter = new TextFormatter<>(stringConverter);
        timeField.setTextFormatter(timeFormatter);

        ComboBox<String> timeZone = new ComboBox<>();
        List<String> zoneIds = new ArrayList<>(ZoneId.getAvailableZoneIds());
        timeZone.setItems(FXCollections.observableList(zoneIds));
        timeZone.setStyle("-fx-pref-width: 180");

        HBox whenControl = new HBox(new Label("Date :"), datePicker, new Label("Heure :"), timeField, timeZone);
        whenControl.setStyle("-fx-spacing: inherit; -fx-alignment: baseline-left;");

        // time flow

        ChoiceBox<NamedTimeAccelerator> acceleratorChoicer = new ChoiceBox<>();
        acceleratorChoicer.setItems(FXCollections.observableList(List.of(NamedTimeAccelerator.values())));

        HBox timeFlowControl = new HBox(acceleratorChoicer);
        timeFlowControl.setStyle("-fx-spacing: inherit");

        // controle bar
        HBox controlBar = new HBox(whereControl, whenControl, timeFlowControl);
        controlBar.setStyle("-fx-spacing: 4; -fx-padding: 4;");

        return controlBar;
    }

    /**
     * Return the formatter
     *
     * @param isLon {@code true} iff this is the formatter for the longitude,
     *              {@code false} iff this is the formatter for the latitude
     * @return the formatter
     */
    private TextFormatter<Number> getFormatter(boolean isLon) {
        NumberStringConverter stringConverter = new NumberStringConverter("#0.00");

        UnaryOperator<TextFormatter.Change> filter = (change -> {
            try {
                String newText = change.getControlNewText();
                double newValue = stringConverter.fromString(newText).doubleValue();
                if (isLon) {
                    return GeographicCoordinates.isValidLonDeg(newValue) ? change : null;
                } else {
                    return GeographicCoordinates.isValidLatDeg(newValue) ? change : null;
                }
            } catch (Exception e) {
                return null;
            }
        });

        return new TextFormatter<>(stringConverter, 0, filter);
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
        //Text fovText = new Text(Bindings.format("Champ de vue : %s°", viewingParametersBean.getFieldOfViewDeg()));
        Text objText = new Text();
        Text mousePosText = new Text();
        BorderPane infoBar = new BorderPane();
        return infoBar;
    }
}

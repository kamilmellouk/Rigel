package ch.epfl.rigel.gui;

import ch.epfl.rigel.astronomy.AsterismLoader;
import ch.epfl.rigel.astronomy.HygDatabaseLoader;
import ch.epfl.rigel.astronomy.StarCatalogue;
import ch.epfl.rigel.coordinates.GeographicCoordinates;
import ch.epfl.rigel.coordinates.HorizontalCoordinates;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.converter.LocalTimeStringConverter;
import javafx.util.converter.NumberStringConverter;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.function.UnaryOperator;

/**
 * @author Bastien Faivre (310929)
 * @author Kamil Mellouk (312327)
 */

public class Main extends Application {

    // TODO : canvas too small to display the full info bar ?
    // TODO : my draw sky, may use skycanvas manager, draw sky, epfl logo.... to keep in main package no
    // TODO : lon and lat textfields left digit doesnt delete
    // TODO : reset button doesnt resets datepicker, but not

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {

        ObserverLocationBean observerLocationBean = new ObserverLocationBean();
        DateTimeBean dateTimeBean = new DateTimeBean();

        ViewingParametersBean viewingParametersBean = new ViewingParametersBean();
        viewingParametersBean.setCenter(HorizontalCoordinates.ofDeg(180.000000000001, 42));
        viewingParametersBean.setFieldOfViewDeg(100);

        SkyCanvasManager canvasManager = createManager(dateTimeBean, observerLocationBean, viewingParametersBean);

        BorderPane mainPane = new BorderPane(
                new Pane(canvasManager.canvas()),
                controlBar(observerLocationBean, dateTimeBean),
                null,
                infoBar(viewingParametersBean, canvasManager),
                null
        );

        TimeAnimator timeAnimator = new TimeAnimator(dateTimeBean);

        canvasManager.canvas().widthProperty().bind(mainPane.widthProperty());
        canvasManager.canvas().heightProperty().bind(mainPane.heightProperty());

        primaryStage.setScene(new Scene(mainPane));

        primaryStage.setTitle("Rigel");
        primaryStage.setMinWidth(800);
        primaryStage.setMinHeight(600);

        primaryStage.show();
        canvasManager.canvas().requestFocus();

    }

    private TextField createTextField(boolean isLon, ObserverLocationBean olb, double defaultValue) {
        TextField tf = new TextField();
        tf.setStyle("-fx-pref-width: 60; -fx-alignment: baseline-right;");
        TextFormatter<Number> textFormatter = getFormatter(isLon);
        tf.setTextFormatter(textFormatter);
        if (isLon)
            olb.lonDegProperty().bind(textFormatter.valueProperty());
        else
            olb.latDegProperty().bind(textFormatter.valueProperty());
        textFormatter.setValue(defaultValue);
        return tf;
    }

    private HBox controlBar(ObserverLocationBean olb, DateTimeBean dtb) throws IOException {
        try (InputStream fs = getClass().getResourceAsStream("/Font Awesome 5 Free-Solid-900.otf")) {

            // Observer Location
            HBox whereControl = new HBox(
                    new Label("Longitude (°) :"), createTextField(true, olb, 6.57),
                    new Label("Latitude (°) :"), createTextField(false, olb, 46.52)
            );
            whereControl.setStyle("-fx-spacing: inherit; -fx-alignment: baseline-left;");

            // observation time


            DatePicker datePicker = new DatePicker();
            dtb.dateProperty().bind(datePicker.valueProperty());
            datePicker.setValue(LocalDate.now());


            TextField timeField = new TextField();
            timeField.setStyle("-fx-pref-width: 75; -fx-alignment: baseline-right;");
            DateTimeFormatter hmsFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
            LocalTimeStringConverter stringConverter = new LocalTimeStringConverter(hmsFormatter, hmsFormatter);
            TextFormatter<LocalTime> timeFormatter = new TextFormatter<>(stringConverter);
            timeField.setTextFormatter(timeFormatter);
            dtb.timeProperty().bind(timeFormatter.valueProperty());
            timeFormatter.setValue(LocalTime.now());


            ComboBox<ZoneId> timeZone = new ComboBox<>();
            List<String> availableZoneIds = new ArrayList<>(ZoneId.getAvailableZoneIds());
            Collections.sort(availableZoneIds);
            List<ZoneId> zoneIdList = new ArrayList<>();
            availableZoneIds.forEach(e -> zoneIdList.add(ZoneId.of(e)));
            timeZone.setItems(FXCollections.observableList(zoneIdList));
            timeZone.setStyle("-fx-pref-width: 180");
            dtb.zoneProperty().bind(timeZone.valueProperty());
            timeZone.setValue(ZoneId.systemDefault());


            HBox whenControl = new HBox(
                    new Label("Date :"), datePicker,
                    new Label("Heure :"), timeField, timeZone
            );
            whenControl.setStyle("-fx-spacing: inherit; -fx-alignment: baseline-left;");

            // time flow

            ChoiceBox<NamedTimeAccelerator> acceleratorChoicer = new ChoiceBox<>();
            acceleratorChoicer.setItems(FXCollections.observableList(List.of(NamedTimeAccelerator.values())));

            Font fontAwesome = Font.loadFont(fs, 15);

            Button startStopButton = new Button();
            startStopButton.setFont(fontAwesome);

            Button resetButton = new Button("\uf0e2");
            resetButton.setFont(fontAwesome);
            resetButton.setOnAction(e -> {
                datePicker.setValue(LocalDate.now());
                timeFormatter.setValue(LocalTime.now());
                timeZone.setValue(ZoneId.systemDefault());
            });

            HBox timeFlowControl = new HBox(acceleratorChoicer, resetButton, startStopButton);
            timeFlowControl.setStyle("-fx-spacing: inherit");

            // control bar

            HBox controlBar = new HBox(
                    whereControl, verticalSeparator(),
                    whenControl, verticalSeparator(),
                    timeFlowControl
            );

            controlBar.setStyle("-fx-spacing: 4; -fx-padding: 4;");
            return controlBar;
        }

    }

    private SkyCanvasManager createManager(DateTimeBean dtb, ObserverLocationBean olb, ViewingParametersBean vpb) throws IOException {
        try (InputStream hs = getClass().getResourceAsStream("/hygdata_v3.csv");
             InputStream as = getClass().getResourceAsStream("/asterisms.txt")) {
            StarCatalogue catalogue = new StarCatalogue.Builder()
                    .loadFrom(hs, HygDatabaseLoader.INSTANCE)
                    .loadFrom(as, AsterismLoader.INSTANCE)
                    .build();

            return new SkyCanvasManager(catalogue, dtb, olb, vpb);

        }
    }

    private BorderPane infoBar(ViewingParametersBean vpb, SkyCanvasManager canvasManager) {
        Text fovDisplay = new Text();
        vpb.fieldOfViewDegProperty().addListener(
                (p, o, n) -> fovDisplay.setText(Bindings.format(Locale.ROOT,
                        "Champ de vue : %.1f°", n).get())
        );

        Text objectInfo = new Text();
        canvasManager.objUnderMouseProperty().addListener(
                (p, o, n) -> {
                    if (n != null) {
                        objectInfo.setText(n.info());
                    } else {
                        objectInfo.setText("");
                    }
                }
        );

        Text mousePos = new Text();
        mousePos.setText(Bindings.format(Locale.ROOT,
                "Azimut : %.2f°, hauteur : %.2f°",
                canvasManager.getMouseAzDeg(),
                canvasManager.getMouseAltDeg()).get());

        BorderPane infoBar = new BorderPane(objectInfo, null, mousePos, null, fovDisplay);
        infoBar.setStyle("-fx-padding: 4; -fx-background-color: white;");
        return infoBar;
    }

    /**
     * @return a vertical separator
     */
    private Separator verticalSeparator() {
        Separator verticalSeparator = new Separator();
        verticalSeparator.setOrientation(Orientation.VERTICAL);
        return verticalSeparator;
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

}

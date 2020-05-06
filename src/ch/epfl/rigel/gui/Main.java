package ch.epfl.rigel.gui;

import ch.epfl.rigel.astronomy.HygDatabaseLoader;
import ch.epfl.rigel.astronomy.StarCatalogue;
import ch.epfl.rigel.coordinates.GeographicCoordinates;
import ch.epfl.rigel.coordinates.HorizontalCoordinates;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.converter.LocalTimeStringConverter;
import javafx.util.converter.NumberStringConverter;
import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
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

    private InputStream resourceStream() {
        return getClass().getResourceAsStream("/hygdata_v3.csv");
    }

    @Override
    public void start(Stage primaryStage) throws IOException {

        try (InputStream hs = resourceStream()) {

            // observation position

            TextField lonTextField = new TextField();
            lonTextField.setStyle("-fx-pref-width: 60; -fx-alignment: baseline-right;");
            TextFormatter<Number> textFormatterLon = getFormatter(true);
            lonTextField.setTextFormatter(textFormatterLon);
            textFormatterLon.setValue(6.57);

            TextField latTextField = new TextField();
            latTextField.setStyle("-fx-pref-width: 60; -fx-alignment: baseline-right;");
            TextFormatter<Number> textFormatterLat = getFormatter(false);
            latTextField.setTextFormatter(textFormatterLat);
            textFormatterLat.setValue(46.52);

            ObservableValue<ObserverLocationBean> observerLocationBean = Bindings.createObjectBinding(
                    () -> {
                        ObserverLocationBean olb = new ObserverLocationBean();
                        olb.setCoordinates(GeographicCoordinates.ofDeg(
                                textFormatterLon.getValue().doubleValue(),
                                textFormatterLat.getValue().doubleValue()
                        ));
                        return olb;
                    },
                    textFormatterLon.valueProperty(), textFormatterLat.valueProperty()
            );

            HBox whereControl = new HBox(
                    new Label("Longitude (°) :"), lonTextField,
                    new Label("Latitude (°) :"), latTextField
            );
            whereControl.setStyle("-fx-spacing: inherit; -fx-alignment: baseline-left;");

            // observation time

            DatePicker datePicker = new DatePicker();
            datePicker.setValue(LocalDate.now());

            TextField timeField = new TextField();
            timeField.setStyle("-fx-pref-width: 75; -fx-alignment: baseline-right;");
            DateTimeFormatter hmsFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
            LocalTimeStringConverter stringConverter = new LocalTimeStringConverter(hmsFormatter, hmsFormatter);
            TextFormatter<LocalTime> timeFormatter = new TextFormatter<>(stringConverter);
            timeField.setTextFormatter(timeFormatter);
            timeFormatter.setValue(LocalTime.now());

            ComboBox<String> timeZone = new ComboBox<>();
            List<String> zoneIds = new ArrayList<>(ZoneId.getAvailableZoneIds());
            Collections.sort(zoneIds);
            timeZone.setItems(FXCollections.observableList(zoneIds));
            timeZone.setStyle("-fx-pref-width: 180");
            timeZone.setValue(ZoneId.systemDefault().toString());

            ObservableValue<DateTimeBean> dateTimeBean = Bindings.createObjectBinding(
                    () -> {
                        DateTimeBean dtb = new DateTimeBean();
                        dtb.setDate(datePicker.getValue());
                        dtb.setTime(timeFormatter.getValue());
                        dtb.setZone(ZoneId.of(timeZone.getValue()));
                        return dtb;
                    },
                    datePicker.valueProperty(), timeFormatter.valueProperty(), timeZone.valueProperty()
            );

            HBox whenControl = new HBox(
                    new Label("Date :"), datePicker,
                    new Label("Heure :"), timeField, timeZone
            );
            whenControl.setStyle("-fx-spacing: inherit; -fx-alignment: baseline-left;");

            // time flow

            ChoiceBox<NamedTimeAccelerator> acceleratorChoicer = new ChoiceBox<>();
            acceleratorChoicer.setItems(FXCollections.observableList(List.of(NamedTimeAccelerator.values())));

            HBox timeFlowControl = new HBox(acceleratorChoicer);
            timeFlowControl.setStyle("-fx-spacing: inherit");

            // control bar

            HBox controlBar = new HBox(whereControl, verticalSeparator(), whenControl, verticalSeparator(), timeFlowControl);
            controlBar.setStyle("-fx-spacing: 4; -fx-padding: 4;");

            ViewingParametersBean viewingParametersBean =
                    new ViewingParametersBean();
            viewingParametersBean.setCenter(
                    HorizontalCoordinates.ofDeg(180.000000000001, 42));
            viewingParametersBean.setFieldOfViewDeg(70);

            // sky

            StarCatalogue catalogue = new StarCatalogue.Builder()
                    .loadFrom(hs, HygDatabaseLoader.INSTANCE)
                    .build();
            ObservableValue<SkyCanvasManager> canvasManager = Bindings.createObjectBinding(
                    () -> new SkyCanvasManager(
                            catalogue,
                            dateTimeBean.getValue(),
                            observerLocationBean.getValue(),
                            viewingParametersBean
                    ),
                    dateTimeBean, observerLocationBean
            );

            BorderPane skyPane = new BorderPane(canvasManager.getValue().canvas());

            BorderPane mainPane = new BorderPane(skyPane, controlBar, null, null, null);

            primaryStage.setTitle("Rigel");
            primaryStage.setMinWidth(800);
            primaryStage.setMinHeight(600);

            primaryStage.setScene(new Scene(mainPane));
            primaryStage.show();
            skyPane.requestFocus();
        }

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

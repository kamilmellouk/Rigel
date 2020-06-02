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
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.layout.*;
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

import static javafx.beans.binding.Bindings.when;

/**
 * @author Bastien Faivre (310929)
 * @author Kamil Mellouk (312327)
 */
public class Main extends Application {

    // Strings used for button icons
    private static final String RESET_ICON = "\uf0e2";
    private static final String PLAY_ICON = "\uf04b";
    private static final String PAUSE_ICON = "\uf04c";
    private static final String FORWARD_ICON = "\uf04e";
    private static final String SKIP_ICON = "\uf051";

    private final ObserverLocationBean observerLocationBean = new ObserverLocationBean();
    private final DateTimeBean dateTimeBean = new DateTimeBean();
    private final ViewingParametersBean viewingParametersBean = new ViewingParametersBean();
    private final TimeAnimator timeAnimator = new TimeAnimator(dateTimeBean);
    private SkyCanvasManager skyCanvasManager;
    private CityCatalogue cityCatalogue;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        viewingParametersBean.setCenter(HorizontalCoordinates.ofDeg(180.000000000001, 15));
        viewingParametersBean.setFieldOfViewDeg(100);

        skyCanvasManager = createManager();
        Canvas canvas = skyCanvasManager.canvas();
        cityCatalogue = createCityCatalogue();

        BorderPane mainPane = new BorderPane(
                new Pane(canvas),
                controlBar(),
                null,
                infoBar(),
                settingsBar()
        );

        skyCanvasManager.canvas().widthProperty().bind(mainPane.widthProperty());
        skyCanvasManager.canvas().heightProperty().bind(mainPane.heightProperty());

        HomePage homePage = new HomePage();
        VBox homePane = homePage.getPane();

        Scene scene = new Scene(homePane);
        scene.getStylesheets().add("/style.css");

        // launch the program by clicking any key
        homePane.setOnKeyPressed(
                e -> scene.setRoot(mainPane)
        );
        homePane.requestFocus();

        primaryStage.setScene(scene);
        primaryStage.setTitle("Rigel");
        primaryStage.setMinWidth(1200);
        primaryStage.setMinHeight(700);
        primaryStage.show();
    }

    /**
     * Creating te top bar dedicated to parameters control (location, time, timeflow)
     *
     * @return HBox dedicated to parameters control
     * @throws IOException if IO exception when loading AwesomeFont
     */
    private HBox controlBar() throws IOException {
        //-----------------------------------------------------------------------------
        // Observation location
        //-----------------------------------------------------------------------------
        TextField lonTextField = createLonLatTextField(true, 6.57);
        TextField latTextField = createLonLatTextField(false, 46.52);

        ComboBox<City> cityComboBox = new ComboBox<>();
        cityComboBox.setId("cityComboBox");
        cityComboBox.setItems(FXCollections.observableList(cityCatalogue.cities()));
        cityComboBox.setValue(cityCatalogue.cities().get(0));
        cityComboBox.setOnAction(e -> {
            GeographicCoordinates coordinates = cityComboBox.getValue().coordinates();
            lonTextField.setText(String.format("%.2f", coordinates.lonDeg()));
            latTextField.setText(String.format("%.2f", coordinates.latDeg()));
        });
        HBox whereControl = new HBox(
                new Label("Longitude (°) :"), lonTextField,
                new Label("Latitude (°) :"), latTextField,
                cityComboBox
        );
        whereControl.setId("whereControl");

        //-----------------------------------------------------------------------------
        // Observation instant
        //-----------------------------------------------------------------------------
        // Date selection
        DatePicker datePicker = new DatePicker();
        datePicker.setId("datePicker");
        datePicker.setValue(LocalDate.now());
        dateTimeBean.dateProperty().bindBidirectional(datePicker.valueProperty());

        // Time selection
        TextField timeField = new TextField();
        timeField.setId("timeField");
        DateTimeFormatter hmsFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        LocalTimeStringConverter stringConverter = new LocalTimeStringConverter(hmsFormatter, hmsFormatter);
        TextFormatter<LocalTime> timeFormatter = new TextFormatter<>(stringConverter);
        timeField.setTextFormatter(timeFormatter);
        timeFormatter.setValue(LocalTime.now());
        dateTimeBean.timeProperty().bindBidirectional(timeFormatter.valueProperty());

        // Timezone selection
        ComboBox<ZoneId> zoneIdComboBox = new ComboBox<>();
        zoneIdComboBox.setId("zoneIdComboBox");
        List<String> availableZoneIds = new ArrayList<>(ZoneId.getAvailableZoneIds());
        Collections.sort(availableZoneIds);
        List<ZoneId> zoneIdList = new ArrayList<>();
        availableZoneIds.forEach(e -> zoneIdList.add(ZoneId.of(e)));
        zoneIdComboBox.setItems(FXCollections.observableList(zoneIdList));
        zoneIdComboBox.setValue(ZoneId.systemDefault());
        zoneIdComboBox.setTooltip(new Tooltip("Time zone"));
        dateTimeBean.zoneProperty().bindBidirectional(zoneIdComboBox.valueProperty());

        HBox whenControl = new HBox(
                new Label("Date :"), datePicker,
                new Label("Time :"), timeField, zoneIdComboBox
        );
        whenControl.setId("whenControl");
        whenControl.disableProperty().bind(timeAnimator.runningProperty());

        //-----------------------------------------------------------------------------
        // Time flow control
        //-----------------------------------------------------------------------------
        // Time accelerator selection
        ChoiceBox<NamedTimeAccelerator> acceleratorChoiceBox = new ChoiceBox<>();
        acceleratorChoiceBox.setItems(FXCollections.observableList(List.of(NamedTimeAccelerator.values())));
        acceleratorChoiceBox.setValue(NamedTimeAccelerator.TIMES_300);
        timeAnimator.acceleratorProperty().bind(Bindings.select(acceleratorChoiceBox.valueProperty(),
                "accelerator"));
        acceleratorChoiceBox.setTooltip(new Tooltip("Time acceleration"));
        acceleratorChoiceBox.disableProperty().bind(timeAnimator.runningProperty());

        // Time reset
        Font fontAwesome = loadFontAwesome();
        Button resetButton = new Button(RESET_ICON);
        resetButton.setFont(fontAwesome);
        resetButton.setOnAction(e -> {
            datePicker.setValue(LocalDate.now());
            timeFormatter.setValue(LocalTime.now());
            zoneIdComboBox.setValue(ZoneId.systemDefault());
        });
        resetButton.setTooltip(new Tooltip("Reset"));
        resetButton.disableProperty().bind(timeAnimator.runningProperty());

        // Time play/pause
        Button playPauseButton = new Button(PLAY_ICON);
        playPauseButton.setFont(fontAwesome);
        playPauseButton.textProperty().bind(when(timeAnimator.runningProperty()).then(PAUSE_ICON).otherwise(PLAY_ICON));
        playPauseButton.setOnAction(e -> timeAnimator.startStop());
        playPauseButton.setTooltip(new Tooltip("Start/Stop"));

        Button forwardButton = new Button(FORWARD_ICON);
        forwardButton.setFont(fontAwesome);
        forwardButton.setOnAction(e ->
                acceleratorChoiceBox.setValue(NamedTimeAccelerator.values()[
                        acceleratorChoiceBox.getValue().ordinal() + 1 != NamedTimeAccelerator.values().length ?
                                acceleratorChoiceBox.getValue().ordinal() + 1 : 0])
        );
        forwardButton.setTooltip(new Tooltip("Fast forward"));

        Button skipButton = new Button(SKIP_ICON);
        skipButton.setFont(fontAwesome);
        skipButton.setOnAction(e -> dateTimeBean.setDate(dateTimeBean.getDate().plusYears(1)));
        skipButton.setTooltip(new Tooltip("Skip forward"));

        HBox timeFlowControl = new HBox(acceleratorChoiceBox, resetButton, playPauseButton, forwardButton, skipButton);
        timeFlowControl.setId("timeFlowControl");

        //-----------------------------------------------------------------------------
        // Control bar
        //-----------------------------------------------------------------------------
        HBox controlBar = new HBox(
                whereControl, verticalSeparator(),
                whenControl, verticalSeparator(),
                timeFlowControl
        );
        controlBar.setId("controlBar");
        return controlBar;
    }

    /**
     * Creating the bottom bar containing information on the sky
     *
     * @return HBox containing information on the sky
     */
    private BorderPane infoBar() {
        Text fovDisplay = new Text();
        fovDisplay.setId("fovDisplay");
        fovDisplay.textProperty().bind(Bindings.format(Locale.ROOT, "Field of view : %.1f°",
                viewingParametersBean.fieldOfViewDegProperty()));

        Text objectInfo = new Text();
        objectInfo.setId("objectInfo");
        objectInfo.textProperty().bind(Bindings.createStringBinding(
                () -> skyCanvasManager.getObjUnderMouse() != null ? skyCanvasManager.getObjUnderMouse().info() : "",
                skyCanvasManager.objUnderMouseProperty()));

        Text mousePos = new Text();
        mousePos.setId("mousePos");
        mousePos.textProperty().bind(Bindings.format(Locale.ROOT, "Azimut : %.2f°, Altitude : %.2f°",
                skyCanvasManager.mouseAzDegProperty(),
                skyCanvasManager.mouseAltDegProperty()));

        BorderPane infoBar = new BorderPane(objectInfo, null, mousePos, null, fovDisplay);
        infoBar.setId("infoBar");
        return infoBar;
    }

    private VBox settingsBar() {
        Text displaySettingText = new Text("Display settings:");
        displaySettingText.setId("settingsText");
        CheckBox starsCheckBox = new CheckBox("Stars");
        starsCheckBox.selectedProperty().bindBidirectional(skyCanvasManager.drawStarsProperty());
        CheckBox asterismsCheckBox = new CheckBox("Asterisms");
        asterismsCheckBox.selectedProperty().bindBidirectional(skyCanvasManager.drawAsterismsProperty());
        CheckBox planetsCheckBox = new CheckBox("Planets");
        planetsCheckBox.selectedProperty().bindBidirectional(skyCanvasManager.drawPlanetsProperty());
        CheckBox sunCheckBox = new CheckBox("Sun");
        sunCheckBox.selectedProperty().bindBidirectional(skyCanvasManager.drawSunProperty());
        CheckBox moonCheckBox = new CheckBox("Moon");
        moonCheckBox.selectedProperty().bindBidirectional(skyCanvasManager.drawMoonProperty());
        CheckBox horizonCheckBox = new CheckBox("Horizon");
        horizonCheckBox.selectedProperty().bindBidirectional(skyCanvasManager.drawHorizonProperty());
        CheckBox cardinalPointsCheckBox = new CheckBox("Cardinal points");
        cardinalPointsCheckBox.selectedProperty().bindBidirectional(skyCanvasManager.drawCardinalPointsProperty());
        CheckBox atmosphereCheckBox = new CheckBox("Atmosphere");
        atmosphereCheckBox.selectedProperty().bindBidirectional(skyCanvasManager.drawAtmosphereProperty());

        Text fovSliderText = new Text("Field of view (°):");
        fovSliderText.setId("fovSliderText");
        Slider fovSlider = new Slider(30, 150, 100);
        fovSlider.setShowTickMarks(true);
        fovSlider.setShowTickLabels(true);
        fovSlider.valueProperty().bindBidirectional(viewingParametersBean.fieldOfViewDegProperty());

        VBox vBox = new VBox(displaySettingText, starsCheckBox, asterismsCheckBox, planetsCheckBox,
                sunCheckBox, moonCheckBox, horizonCheckBox, cardinalPointsCheckBox, atmosphereCheckBox, new Separator(),
                fovSliderText, fovSlider);
        vBox.setId("settingsBar");
        return vBox;
    }

    /**
     * Creating the SkyCanvasManager used to manage the sky
     *
     * @return SkyCanvasManager used to manage the sky
     * @throws IOException if there is an input exception
     */
    private SkyCanvasManager createManager() throws IOException {
        try (InputStream hs = getClass().getResourceAsStream("/hygdata_v3.csv");
             InputStream as = getClass().getResourceAsStream("/asterisms.txt")) {
            StarCatalogue catalogue = new StarCatalogue.Builder()
                    .loadFrom(hs, HygDatabaseLoader.INSTANCE)
                    .loadFrom(as, AsterismLoader.INSTANCE)
                    .build();
            return new SkyCanvasManager(catalogue, dateTimeBean, observerLocationBean, viewingParametersBean);
        }
    }

    /**
     * Creating the CityCatalogue used in the control bar
     *
     * @return CityCatalogue
     * @throws IOException if there is an input exception
     */
    private CityCatalogue createCityCatalogue() throws IOException {
        try (InputStream cs = getClass().getResourceAsStream("/worldcities.csv")) {
            return new CityCatalogue.Builder()
                    .loadFrom(cs, CityCatalogue.CityLoader.INSTANCE)
                    .build();
        }
    }

    /**
     * Creating a text field for lon or lat with the right formatting
     *
     * @param isLon        boolean indicating whether the TextField is for lon or lat
     * @param defaultValue to assign to the TextField
     * @return TextField with the right formatting
     */
    private TextField createLonLatTextField(boolean isLon, double defaultValue) {
        TextField tf = new TextField();
        tf.setId("tf");
        // Creating the right text formatter
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
        TextFormatter<Number> textFormatter = new TextFormatter<>(stringConverter, 0, filter);
        tf.setTextFormatter(textFormatter);
        // Binding the TextField to the correct ObserverLocationBean property
        if (isLon) {
            observerLocationBean.lonDegProperty().bind(textFormatter.valueProperty());
        } else {
            observerLocationBean.latDegProperty().bind(textFormatter.valueProperty());
        }
        textFormatter.setValue(defaultValue);
        return tf;
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
     * Loading FontAwesome (used for buttons)
     *
     * @return the font FontAwesome
     * @throws IOException if loading exception
     */
    private Font loadFontAwesome() throws IOException {
        try (InputStream fs = getClass().getResourceAsStream("/Font Awesome 5 Free-Solid-900.otf")) {
            return Font.loadFont(fs, 15);
        }
    }
}

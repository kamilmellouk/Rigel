package ch.epfl.rigel.gui;

import ch.epfl.rigel.astronomy.AsterismLoader;
import ch.epfl.rigel.astronomy.CelestialObject;
import ch.epfl.rigel.astronomy.HygDatabaseLoader;
import ch.epfl.rigel.astronomy.StarCatalogue;
import ch.epfl.rigel.coordinates.GeographicCoordinates;
import ch.epfl.rigel.coordinates.HorizontalCoordinates;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.converter.LocalTimeStringConverter;
import javafx.util.converter.NumberStringConverter;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.function.UnaryOperator;

import static javafx.beans.binding.Bindings.when;

/**
 * @author Bastien Faivre (310929)
 * @author Kamil Mellouk (312327)
 */
public class Main extends Application {

    private static final String FAST_FWD_ICON = "\uf04e";
    private static final String SKIP_FWD_ICON = "\uf051";

    private final ObserverLocationBean observerLocationBean = new ObserverLocationBean();
    private final DateTimeBean dateTimeBean = new DateTimeBean();
    private final ViewingParametersBean viewingParametersBean = new ViewingParametersBean();
    private final TimeAnimator timeAnimator = new TimeAnimator(dateTimeBean);
    private SkyCanvasManager skyCanvasManager;
    private Canvas canvas;

    private final Map<String, BooleanProperty> checkBoxesData = new TreeMap<>();

    private Button resetButton;
    private static final String RESET_ICON = "\uf0e2";

    private Button startStopButton;
    private static final String START_ICON = "\uf04b";
    private static final String STOP_ICON = "\uf04c";

    private final Button settingsButton = new Button();
    private static final String RIGHT_ARROW_ICON = "\uf0a9";
    private static final String LEFT_ARROW_ICON = "\uf0a8";
    private final BooleanProperty showSettings = new SimpleBooleanProperty(false);

    private final Button fullScreenButton = new Button();
    private static final String INCREASE_ICON = "\uF065";
    private static final String DECREASE_ICON = "\uF066";
    private final BooleanProperty fullScreen = new SimpleBooleanProperty(false);

    private TextField lonTextField;
    private TextField latTextField;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        viewingParametersBean.setCenter(HorizontalCoordinates.ofDeg(180.000000000001, 15));
        viewingParametersBean.setFieldOfViewDeg(100);

        skyCanvasManager = createManager();
        canvas = skyCanvasManager.canvas();

        // put the data for check boxes
        checkBoxesData.put("Stars", skyCanvasManager.drawStarsProperty());
        checkBoxesData.put("Asterisms", skyCanvasManager.drawAsterismsProperty());
        checkBoxesData.put("Planets", skyCanvasManager.drawPlanetsProperty());
        checkBoxesData.put("Sun", skyCanvasManager.drawSunProperty());
        checkBoxesData.put("Moon", skyCanvasManager.drawMoonProperty());
        checkBoxesData.put("Horizon", skyCanvasManager.drawHorizonProperty());
        checkBoxesData.put("Cardinal points", skyCanvasManager.drawCardinalPointsProperty());
        checkBoxesData.put("Atmosphere", skyCanvasManager.drawAtmosphereProperty());
        checkBoxesData.put("Names", skyCanvasManager.drawNamesProperty());

        initialiseMouseControls();
        initialiseKeyboardControls();
        initialiseButtons();

        Pane sky = new Pane(canvas, settingsButton, fullScreenButton);

        BorderPane mainPane = new BorderPane(
                sky,
                controlBar(),
                null,
                infoBar(),
                null
        );

        // display depending on state
        mainPane.leftProperty().bind(when(showSettings).then(settingsBar()).otherwise(new VBox()));
        mainPane.topProperty().bind(when(fullScreen).then(new HBox()).otherwise(controlBar()));
        mainPane.bottomProperty().bind(when(fullScreen).then(new BorderPane()).otherwise(infoBar()));

        skyCanvasManager.canvas().widthProperty().bind(mainPane.widthProperty());
        skyCanvasManager.canvas().heightProperty().bind(mainPane.heightProperty());

        HomePage homePage = new HomePage();
        VBox homePane = homePage.getPane();

        Scene scene = new Scene(homePane);
        // add the reference for the style
        scene.getStylesheets().add("/style.css");

        // launch the program by clicking any key
        homePane.setOnKeyPressed(k -> {
                    scene.setRoot(mainPane);
                    canvas.requestFocus();
                }
        );
        homePane.requestFocus();

        primaryStage.setScene(scene);
        primaryStage.setTitle("Rigel");
        primaryStage.setMinWidth(1025);
        primaryStage.setMinHeight(700);
        primaryStage.show();
    }

    /**
     * Initialising mouse controls
     */
    private void initialiseMouseControls() {
        canvas.setOnMouseClicked(m -> {
            if (!canvas.isFocused()) canvas.requestFocus();
            skyCanvasManager.updateSky();
            switch (m.getButton()) {
                case PRIMARY:
                    // Open the specific Wikipedia webpage depending on the object
                    if (skyCanvasManager.getObjUnderMouse() != null) {
                        switch (skyCanvasManager.getObjUnderMouse().name()) {
                            case "Rigel":
                            case "Sun":
                            case "Moon":
                            case "Neptune":
                            case "Mars":
                            case "Jupiter":
                            case "Saturn":
                            case "Venus":
                            case "Uranus":
                                this.getHostServices().showDocument("https://en.wikipedia.org/wiki/" +
                                        skyCanvasManager.getObjUnderMouse().name());
                                break;
                            case "Mercury":
                                this.getHostServices().showDocument("https://en.wikipedia.org/wiki/Mercury_(planet)");
                                break;
                        }
                    }
                    break;
                case MIDDLE:
                    // reset the field of view
                    viewingParametersBean.setFieldOfViewDeg(100);
                    break;
                case SECONDARY:
                    // display information of the object under mouse
                    CelestialObject objUnderMouse = skyCanvasManager.getObjUnderMouse();
                    if (objUnderMouse != null) {
                        GraphicsContext ctx = canvas.getGraphicsContext2D();
                        ctx.setFill(Color.valueOf("#373e43"));
                        ctx.fillRect(m.getX(), m.getY(), 230, 65);
                        ctx.setStroke(Color.WHITE);
                        ctx.setLineWidth(1);
                        ctx.strokeText(" Name : " + objUnderMouse.info() + "\n" +
                                        " Position : " + objUnderMouse.equatorialPos() + "\n" +
                                        " Angular Size : " + objUnderMouse.angularSize() + "\n" +
                                        " Magnitude : " + objUnderMouse.magnitude(),
                                m.getX(), m.getY());
                    }
                    break;
            }
        });
    }

    /**
     * Initialising keyboard controls
     */
    private void initialiseKeyboardControls() {
        canvas.setOnKeyPressed(k -> {
                    HorizontalCoordinates center = viewingParametersBean.getCenter();
                    switch (k.getCode()) {
                        case LEFT:
                            viewingParametersBean.setCenter(skyCanvasManager.centerWithAzDiff(center, -10));
                            break;
                        case RIGHT:
                            viewingParametersBean.setCenter(skyCanvasManager.centerWithAzDiff(center, 10));
                            break;
                        case UP:
                            viewingParametersBean.setCenter(skyCanvasManager.centerWithAltDiff(center, 5));
                            break;
                        case DOWN:
                            viewingParametersBean.setCenter(skyCanvasManager.centerWithAltDiff(center, -5));
                            break;
                        case SPACE:
                        case ENTER:
                            startStopButton.fire();
                            break;
                        case F:
                            fullScreenButton.fire();
                            break;
                        case R:
                            resetButton.fire();
                            break;
                        case TAB:
                            settingsButton.fire();
                            break;
                        case DIGIT1:
                            skyCanvasManager.drawStarsProperty().set(!skyCanvasManager.drawStarsProperty().get());
                            break;
                        case DIGIT2:
                            skyCanvasManager.drawAsterismsProperty().set(!skyCanvasManager.drawAsterismsProperty().get());
                            break;
                        case DIGIT3:
                            skyCanvasManager.drawPlanetsProperty().set(!skyCanvasManager.drawPlanetsProperty().get());
                            break;
                        case DIGIT4:
                            skyCanvasManager.drawSunProperty().set(!skyCanvasManager.drawSunProperty().get());
                            break;
                        case DIGIT5:
                            skyCanvasManager.drawMoonProperty().set(!skyCanvasManager.drawMoonProperty().get());
                            break;
                        case DIGIT6:
                            skyCanvasManager.drawHorizonProperty().set(!skyCanvasManager.drawHorizonProperty().get());
                            break;
                        case DIGIT7:
                            skyCanvasManager.drawCardinalPointsProperty().set(!skyCanvasManager.drawCardinalPointsProperty().get());
                            break;
                        case DIGIT8:
                            skyCanvasManager.drawAtmosphereProperty().set(!skyCanvasManager.drawAtmosphereProperty().get());
                            break;
                        case DIGIT9:
                            skyCanvasManager.drawNamesProperty().set(!skyCanvasManager.drawNamesProperty().get());
                            break;
                        case S:
                            saveScreenshot();
                            break;
                    }
                    skyCanvasManager.updateSky();
                    k.consume();
                }
        );
    }

    /**
     * Initialising buttons
     */
    private void initialiseButtons() {
        fullScreenButton.textProperty().bind(when(fullScreen).then(DECREASE_ICON).otherwise(INCREASE_ICON));
        fullScreenButton.setOnAction(e -> fullScreen.set(!fullScreen.get()));
        fullScreenButton.setTooltip(new Tooltip("Full screen - F"));
        // the full screen closes the settings pane
        fullScreen.addListener((p, o, n) -> {
            if (n) showSettings.set(false);
        });
        // place the full screen button just next to the settings button
        fullScreenButton.layoutXProperty().bind(settingsButton.widthProperty());

        settingsButton.textProperty().bind(when(showSettings).then(LEFT_ARROW_ICON).otherwise(RIGHT_ARROW_ICON));
        settingsButton.setTooltip(new Tooltip("Settings - TAB"));
        settingsButton.setOnAction(e -> showSettings.set(!showSettings.get()));
        // the settings pane is disabled in full screen
        settingsButton.disableProperty().bind(fullScreen);
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
        // Coordinates fields
        lonTextField = createLonLatTextField(true, 6.57);
        latTextField = createLonLatTextField(false, 46.52);

        HBox whereControl = new HBox(
                new Label("Longitude (°) :"), lonTextField,
                new Label("Latitude (°) :"), latTextField
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
        settingsButton.setFont(fontAwesome);
        fullScreenButton.setFont(fontAwesome);

        resetButton = new Button(RESET_ICON);
        resetButton.setFont(fontAwesome);
        resetButton.setOnAction(e -> {
            datePicker.setValue(LocalDate.now());
            timeFormatter.setValue(LocalTime.now());
            zoneIdComboBox.setValue(ZoneId.systemDefault());
        });
        resetButton.setTooltip(new Tooltip("Reset - R"));
        resetButton.disableProperty().bind(timeAnimator.runningProperty());

        // Time play/pause
        startStopButton = new Button(START_ICON);
        startStopButton.setFont(fontAwesome);
        startStopButton.textProperty().bind(when(timeAnimator.runningProperty()).then(STOP_ICON).otherwise(START_ICON));
        startStopButton.setOnAction(e -> timeAnimator.startStop());
        startStopButton.setTooltip(new Tooltip("Start/Stop - SPACE or ENTER"));

        // Select the next accelerator
        Button forwardButton = new Button(FAST_FWD_ICON);
        forwardButton.setFont(fontAwesome);
        forwardButton.setOnAction(e -> {
                    timeAnimator.startStop();
                    acceleratorChoiceBox.setValue(NamedTimeAccelerator.values()[
                            acceleratorChoiceBox.getValue().ordinal() + 1 != NamedTimeAccelerator.values().length ?
                                    acceleratorChoiceBox.getValue().ordinal() + 1 : 0]);
                    timeAnimator.startStop();
                }
        );
        forwardButton.setTooltip(new Tooltip("Fast forward"));

        // Add one year to the current date
        Button skipButton = new Button(SKIP_FWD_ICON);
        skipButton.setFont(fontAwesome);
        skipButton.setOnAction(e -> dateTimeBean.setDate(dateTimeBean.getDate().plusYears(1)));
        skipButton.disableProperty().bind(timeAnimator.runningProperty());
        skipButton.setTooltip(new Tooltip("Skip forward"));

        HBox timeFlowControl = new HBox(acceleratorChoiceBox, resetButton, startStopButton, forwardButton, skipButton);
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

    private VBox settingsBar() throws IOException {
        Text displaySettingText = new Text("Display settings:");
        displaySettingText.setId("settingsText");
        // checkboxes for each elements of the canvas
        VBox checkBoxes = new VBox();
        int numberKey = 1;
        for (String element : checkBoxesData.keySet()) {
            CheckBox checkBox = new CheckBox(element);
            checkBox.selectedProperty().bindBidirectional(checkBoxesData.get(element));
            checkBox.setTooltip(new Tooltip("Show " + element.toLowerCase() + " - " + numberKey));
            numberKey++;
            checkBoxes.getChildren().add(checkBox);
        }

        // slider for the field of view
        Text fovSliderText = new Text("Field of view (°):");
        fovSliderText.setId("fovSliderText");
        Slider fovSlider = new Slider(30, 150, 100);
        fovSlider.setShowTickMarks(true);
        fovSlider.setShowTickLabels(true);
        fovSlider.valueProperty().bindBidirectional(viewingParametersBean.fieldOfViewDegProperty());

        // city selection
        Text citySelectionText = new Text("Search or select a city:");
        citySelectionText.setId("citySelectionText");

        CityCatalogue cityCatalogue = createCityCatalogue();
        FilteredList<City> filteredCities = new FilteredList<>(FXCollections.observableList(cityCatalogue.cities()), c -> true);

        TextField searchBar = new TextField();
        // modify the list of the cities depending on the research
        searchBar.textProperty().addListener((p, o, n) -> filteredCities.setPredicate(city -> {
            String searchText = n.toLowerCase();
            return n.isEmpty() || city.getName().toLowerCase().contains(searchText) || city.getCountry().toLowerCase().contains(searchText);
        }));

        // list of the cities matching the research
        TableView<City> cityTableView = new TableView<>();
        cityTableView.setItems(filteredCities);
        TableColumn<City, String> cityTableColumn = new TableColumn<>("City");
        cityTableColumn.setCellValueFactory(city -> city.getValue().nameProperty());
        TableColumn<City, String> countryTableColumn = new TableColumn<>("Country");
        countryTableColumn.setCellValueFactory(city -> city.getValue().countryProperty());
        cityTableView.getColumns().setAll(cityTableColumn, countryTableColumn);
        cityTableView.getSelectionModel().selectedItemProperty().addListener((p, o, n) -> {
            if (n != null) {
                GeographicCoordinates coordinates = n.coordinates();
                lonTextField.setText(String.format("%.2f", coordinates.lonDeg()));
                latTextField.setText(String.format("%.2f", coordinates.latDeg()));
            }
        });

        VBox vBox = new VBox(displaySettingText, checkBoxes, new Separator(),
                fovSliderText, fovSlider, new Separator(), citySelectionText, searchBar, cityTableView);
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

    /**
     * Saving the current sky as a png file, with the name containing the positon and time of observation
     */
    private void saveScreenshot() {
        WritableImage image = skyCanvasManager.canvas().snapshot(new SnapshotParameters(), null);

        File file = new File("sky_" +
                observerLocationBean.getCoordinates() + "_" +
                dateTimeBean.getDate() + "_" +
                dateTimeBean.getTime().truncatedTo(ChronoUnit.SECONDS) +
                ".png");

        try {
            ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
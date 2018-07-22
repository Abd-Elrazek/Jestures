/*******************************************************************************
 * Copyright (c) 2018 Giulianini Luca
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/

package recorder.view;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXScrollPane;
import com.jfoenix.controls.JFXTabPane;
import com.sun.javafx.application.PlatformImpl;

import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import jestures.core.codification.FrameLength;
import jestures.core.file.FileManager;
import jestures.core.recognition.gesture.DefaultGesture;
import jestures.core.view.enums.DialogsType.DimDialogs;
import jestures.core.view.enums.NotificationType;
import jestures.core.view.enums.NotificationType.Duration;
import jestures.core.view.utils.ListViewFactory;
import jestures.core.view.utils.ViewUtilities;
import recorder.controller.Recording;

/**
 *
 *
 */
@SuppressWarnings("restriction")
public class RecorderScreenView extends AbstractRecorderScreenView implements RecView {
    // private static final Logger LOG = Logger.getLogger(RecorderScreenView.class);
    private final Recording recorder;
    private FrameLength frameLength;
    // VIEW
    private Stage stage; // NOPMD
    private Scene scene; // NOPMD

    // ########### ALL TABS #############
    @FXML
    private JFXTabPane tabPane;
    @FXML
    private BorderPane recorderPane; // NOPMD
    @FXML
    private JFXButton startButton;
    @FXML
    private StackPane tabStackPane;
    @FXML
    private JFXComboBox<String> gestureComboBox;
    @FXML
    private VBox vbox;
    @FXML
    private ComboBox<FrameLength> frameLengthCombo;
    @FXML
    private JFXButton addGestureButton;
    // ########### TAB 1 #############
    @FXML
    private BorderPane userBorderPane; // NOPMD
    @FXML
    private JFXButton createUserButton;
    @FXML
    private JFXComboBox<String> selectUserCombo;

    // ########### TAB 2 #############

    // ########### TAB 3 #############

    // ########### TAB 4 #############
    @FXML
    private JFXScrollPane scrollPane;
    @FXML
    private JFXListView<BorderPane> listView;

    /**
     * @param recorder
     *            the {@link RecorderScreenView}
     */
    public RecorderScreenView(final Recording recorder) {
        super(recorder);
        this.recorder = recorder;
        // CREATE AND SET THE CONTROLLER. INIT THE BORDER PANE
        Platform.runLater(() -> {
            final FXMLLoader loader = new FXMLLoader();
            loader.setController(this);
            loader.setLocation(this.getClass().getResource(FXMLScreens.HOME.getPath()));
            try {
                this.recorderPane = (BorderPane) loader.load();
            } catch (final IOException e1) {
                e1.printStackTrace();
            }
        });
    }

    @Override
    @FXML
    public void initialize() { // NOPMD
        // INIT FIRST ABSTRACT CLASS
        super.initialize();
        // CREATION OF STAGE SCENE AND PANE
        this.stage = new Stage();
        this.scene = new Scene(this.recorderPane);
        this.stage.setScene(this.scene);
        // SETTING EXIT ACTIONS
        this.stage.setOnCloseRequest(e -> {
            this.stopSensor();
            Platform.exit();
            System.exit(0);
        });
        // CHARGE THE CSS
        this.chargeSceneSheets(FXMLScreens.HOME);
        this.stage.show();

    }

    private void chargeSceneSheets(final FXMLScreens screen) {
        this.scene.getStylesheets().add(RecorderScreenView.class.getResource(screen.getCssPath()).toString());
    }

    // ############################################## FROM TRACKER (RECORDER) ###################################
    // INTERFACE METHODS FROM VIEW
    @Override
    public void notifyOnFrameChange(final int frame, final Vector2D derivative, final Vector2D path) {
        Platform.runLater(() -> {
            if (frame == 0) {
                this.clearCanvasAndChart();
            }
            this.getxSeries().getData().add(new XYChart.Data<Number, Number>(frame, (int) derivative.getX()));
            this.getySeries().getData().add(new XYChart.Data<Number, Number>(frame, (int) derivative.getY()));

            this.getContext().fillOval(-path.getX() + this.getCanvas().getWidth() / 2,
                    path.getY() + this.getCanvas().getHeight() / 2, 10, 10);
        });

    }

    @Override
    public void notifyOnFeatureVectorEvent() {
        Platform.runLater(() -> {
            this.addFeatureVectorToListView(this.listView.getItems().size(),
                    this.getCanvas().snapshot(new SnapshotParameters(), null));
        });
    }

    // ############################################## FROM RECORDER ###################################

    @Override
    public void loadUsers() {
        Platform.runLater(() -> {
            try {
                this.selectUserCombo.getItems().clear();
                FileManager.getAllUserFolder().stream().forEachOrdered(t -> this.selectUserCombo.getItems().add(t));
            } catch (final IOException e) {
                ViewUtilities.showNotificationPopup("Exception", "Cannot read user data", Duration.MEDIUM,
                        NotificationType.WARNING, null);
            }
        });
    }

    @Override
    public void setRecording(final boolean isRecording) {
        if (isRecording) {
            Platform.runLater(() -> {
                ViewUtilities.showSnackBar((Pane) this.recorderPane.getCenter(), "Record is started", Duration.MEDIUM,
                        DimDialogs.SMALL, null);
            });
        } else {
            Platform.runLater(() -> {
                ViewUtilities.showSnackBar((Pane) this.recorderPane.getCenter(), "Record is stopped", Duration.MEDIUM,
                        DimDialogs.SMALL, null);
            });
        }
    }

    // ############################################## TO RECORDER ###################################

    // #################################### ALL TABS #############################################
    @Override
    public void selectGesture(final String gesture) {
        this.startButton.setDisable(false);
        System.out.println(gesture);
    }

    @Override
    public void setFrameLength(final FrameLength length) {
        this.frameLength = length;
        this.recorder.setFrameLength(length);
    }

    @Override
    public void startSensor() {
        this.selectUserCombo.setDisable(false);
        this.recorder.startSensor();
    }

    @Override
    public void stopSensor() {
        this.clearCanvasAndChart();
        this.recorder.stopSensor();
        this.selectUserCombo.setDisable(true);
    }

    @Override
    public Recording getTracker() {
        return this.recorder;
    }

    @Override
    public void startFxThread() {
        PlatformImpl.startup(() -> {
        });
    }

    @Override
    public FrameLength getFrameLength() {
        return this.frameLength;
    }

    // ###### TAB 1 ######
    @Override
    public void createUserProfile(final String username) {
        try {
            if (!this.recorder.createUserProfile(username)) {
                ViewUtilities.showNotificationPopup("Cannot create User", username + " already exists", Duration.MEDIUM,
                        NotificationType.WARNING, null);
            } else {
                ViewUtilities.showNotificationPopup("User Created", username + " created!", Duration.MEDIUM,
                        NotificationType.SUCCESS, null);
                // IF USER IS LOADED CORRECLY ENABLE BUTTONS
                this.gestureComboBox.setDisable(false);
                this.addGestureButton.setDisable(false);
                this.loadUsers();
            }
        } catch (final IOException e) {
            ViewUtilities.showNotificationPopup("Io Exception", "Cannot create user file. \nClick for info",
                    Duration.LONG, NotificationType.ERROR, t -> e.printStackTrace());
        }
    }

    @Override
    public void loadUserProfile(final String name) {
        try {
            this.recorder.loadUserProfile(name);
        } catch (final IOException e1) {
            ViewUtilities.showNotificationPopup("User Dataset not found", "Regenerating it", Duration.MEDIUM, // NOPMD
                    NotificationType.WARNING, t -> e1.printStackTrace());
        }
        // IF USER IS LOADED CORRECLY ENABLE BUTTONS
        this.gestureComboBox.setDisable(false);
        this.addGestureButton.setDisable(false);
        // SAVE COPY OF GESTURES LOAD USER WITH GESTURE AND UPDATE THE GESTURES
        final Set<String> set = new HashSet<>();
        // DELETE GESTURES
        this.gestureComboBox.getItems().clear();
        System.out.println(this.recorder.getAllUserGesture());
        // LOAD USER GESTURES
        set.addAll(this.recorder.getAllUserGesture());
        set.addAll(DefaultGesture.getAllDefaultGestures());
        this.gestureComboBox.getItems().addAll(set);
        Collections.sort(this.gestureComboBox.getItems());
        ViewUtilities.showSnackBar((Pane) this.recorderPane.getCenter(), "Database loaded and Gesture updated!",
                Duration.MEDIUM, DimDialogs.SMALL, null);
    }

    // ###### TAB 4 ######
    @Override
    public void deleteFeatureVectorInLIstView(final int indexClicked) {
        this.listView.getItems().remove(indexClicked);
        this.recorder.deleteFeatureVector(indexClicked);
        this.scrollPane.setContent(this.listView);

    }

    @Override
    public void clearListView() {
        this.listView.getItems().clear();
        this.recorder.clearFeatureVectors();
        this.scrollPane.setContent(this.listView);
    }

    @Override
    public void addFeatureVector(final String gesture, final int indexClicked) {
        try {
            this.recorder.addFeatureVector(this.getGesture(), indexClicked);
        } catch (final IOException e2) {
            ViewUtilities.showNotificationPopup("Io Exception", "Cannot serialize file", Duration.MEDIUM,
                    NotificationType.ERROR, t -> e2.printStackTrace());
        }
        this.deleteFeatureVectorInLIstView(indexClicked);
    }

    @Override
    public void addAllElemInListView() {
        try {
            this.recorder.addAllFeatureVectors(this.getGesture());
        } catch (final IOException e2) {
            ViewUtilities.showNotificationPopup("Io Exception", "Cannot serialize file", Duration.MEDIUM,
                    NotificationType.ERROR, t -> e2.printStackTrace());
        }
        this.clearListView();
    }

    // ############################################## INSTANCE METHODS ###################################
    private void addFeatureVectorToListView(final int index, final Image image) {
        ListViewFactory.addVectorToListView(this.listView, image, index);

        // ON CLICK ACTION
        this.listView.setOnMouseClicked(t -> {
            final int indexClicked = this.listView.getSelectionModel().getSelectedIndex();
            if (t.getButton().equals(MouseButton.PRIMARY) && indexClicked != -1) {

                ViewUtilities.showConfirmDialog(this.scrollPane, "Save",
                        "Save the feature vector N: " + indexClicked + "?", DimDialogs.MEDIUM, (final Event event) -> {
                            if (((JFXButton) event.getSource()).getText().equals("YES")) {
                                try {
                                    this.addFeatureVector(this.getGesture(), indexClicked);
                                } catch (final Exception e) {
                                    ViewUtilities.showNotificationPopup("Gesture Error", "Select Gesture!",
                                            Duration.MEDIUM, NotificationType.ERROR, k -> e.printStackTrace());
                                }
                            }
                        });

            } else if (indexClicked != -1) {
                this.deleteFeatureVectorInLIstView(indexClicked);
            }

        });
    }

    // 2 THREAD, UI AND KINECT
    private synchronized void clearCanvasAndChart() {
        this.getContext().clearRect(0, 0, this.getCanvas().getWidth(), this.getCanvas().getHeight());
        this.getxSeries().getData().clear();
        this.getySeries().getData().clear();
    }

    private String getGesture() {
        final int i = this.gestureComboBox.getSelectionModel().getSelectedIndex();
        if (i != -1) {
            return this.gestureComboBox.getSelectionModel().getSelectedItem();
        } else {
            throw new IllegalStateException();
        }
    }

}

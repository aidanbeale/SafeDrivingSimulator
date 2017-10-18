/**
 * Copyright 2017 John Humphrys, Aidan Beale
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package controller;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXSlider;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import simulation.Car;

import java.io.IOException;
import java.util.ArrayList;

/**
 * The choose car controller class manages all controlling aspects when a user
 * is selecting the car and hazards
 *
 * @author John
 */
public class ChooseCarController {

    @FXML
    private Group chooseCarGroup;
    @FXML
    private JFXCheckBox checkSpeeding;
    @FXML
    private JFXCheckBox checkCrash;
    @FXML
    private JFXCheckBox checkGiveway;
    @FXML
    private JFXRadioButton firstPersonRadio;
    @FXML
    private JFXRadioButton thirdPersonRadio;
    @FXML
    private JFXSlider simulationTime;
    @FXML
    private JFXCheckBox aiRed;
    @FXML
    private JFXCheckBox aiGreen;
    @FXML
    private JFXCheckBox aiBlue;
    @FXML
    private JFXCheckBox aiOrange;
    @FXML
    private JFXSlider aiCount;
    @FXML
    private JFXCheckBox schoolCrossing;


    PerspectiveCamera camera;
    Group rootGroup = new Group();
    ArrayList<Group> carGroupList = new ArrayList<>();
    ArrayList<Car> carList = new ArrayList<>();
    private int rotateAmount;
    private int currCarGroup = 0;

    ArrayList<String> carColourList;
    ArrayList<String> hazardsList = new ArrayList<>();
    ArrayList<String> aiColours = new ArrayList<>();
    String userView;

    /**
     * Initialises the controller
     */
    @FXML
    private void initialize() {

        // Create cars
        createCars();

        // Setup camera
        camera = setupCamera();

        // Add ambient light to the group
        // rootGroup.getChildren().add(ambient);

        // Create subscene
        SubScene subScene = new SubScene(rootGroup, 300, 300, true, SceneAntialiasing.BALANCED);
        subScene.setCamera(camera);

        // Add subscene to main window
        chooseCarGroup.getChildren().add(subScene);

        chooseForward(null);
        // Rotate the cars
        rotateCarChoice();
    }

    /**
     * This method is called when the user presses the button to start the
     * applicaion
     *
     * @param event The mouse click event on the button
     * @throws IOException Thrown if fxml file not found
     */
    @FXML
    private void start(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/Simulation.fxml"));
        Parent root = (Parent) loader.load();

        SimulationController simControl = loader.<SimulationController>getController();

        // Get values chosen by the user
        String chosenCar = carColourList.get(currCarGroup);
        addView();
        addHazards();
        addAiColours();


        // Pass all values to the next controller
        simControl.setUserChosenCarString(chosenCar);
        simControl.setEvents(hazardsList);
        simControl.setUserView(userView);
        simControl.setAiColours(aiColours);
        simControl.setAiCount((int) aiCount.getValue());
        simControl.setSimTime((int) simulationTime.getValue());

        Parent p = loader.getRoot();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(p));
        stage.show();
    }

    /**
     * Checks to see which view is selected
     */
    private void addView() {
        if (firstPersonRadio.isSelected()) {
            userView = "first";
        } else if (thirdPersonRadio.isSelected()) {
            userView = "third";
        }
    }

    /**
     * Checks to see which hazards are selected
     */
    private void addHazards() {
        if (checkSpeeding.isSelected()) {
            hazardsList.add("speedingEvent");
        }
        if (checkCrash.isSelected()) {
            hazardsList.add("crashEvent");
        }
        if (schoolCrossing.isSelected()) {
            hazardsList.add("schoolCrossingEvent");
        }

        // Removed due to request
        /*
         * if (checkGiveway.isSelected()) { hazardsList.add("givewayEvent"); }
		 */
    }

    private void addAiColours() {
        if (aiRed.isSelected()) {
            aiColours.add("mini-red.3DS");
        }
        if (aiGreen.isSelected()) {
            aiColours.add("mini-green.3DS");
        }
        if (aiBlue.isSelected()) {
            aiColours.add("mini-blue.3DS");
        }
        if (aiOrange.isSelected()) {
            aiColours.add("mini-aws.3DS");
        }

        if(aiColours.isEmpty()) {
            aiColours.add("mini-red.3DS");
            aiColours.add("mini-green.3DS");
            aiColours.add("mini-blue.3DS");
            aiColours.add("mini-aws.3DS");
        }
    }


    /**
     * Used to create the car objects
     */
    private void createCars() {
        carColourList = new ArrayList<>();

        carColourList.add("mini-red.3DS");
        carColourList.add("mini-green.3DS");
        carColourList.add("mini-blue.3DS");
        carColourList.add("mini-aws.3DS");

        for (String s : carColourList) {
            Car c = new Car(s);
            carGroupList.add(c.getCarGroup());
        }

    }

    /**
     * Used to rotate the car selection
     */
    private void rotateCarChoice() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(30); // 30
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    // Needs to utilise runLater to update the main thread from another thread
                    Platform.runLater(new Runnable() {

                        @Override
                        public void run() {
                            rootGroup.setRotationAxis(Rotate.Y_AXIS);

                            if (rotateAmount == 359) {
                                rotateAmount = 0;
                            }
                            rootGroup.setRotate(rotateAmount++);
                        }

                        ;
                    });
                }
            }
        }).start();
    }

    /**
     * Create the perspective camera to view the rotating car
     *
     * @return The created camera
     */
    private PerspectiveCamera setupCamera() {
        camera = new PerspectiveCamera();

        // Position the camera as required
        camera.setTranslateX(-50);
        camera.setTranslateY(-320);
        camera.setTranslateZ(-320);
        camera.setRotationAxis(Rotate.X_AXIS);
        camera.setRotate(-20.0);
        return camera;
    }

    /**
     * Moves the car selection to the previous one in the arraylist
     *
     * @param event The mouse click event on the button
     */
    @FXML
    private void chooseBack(ActionEvent event) {
        currCarGroup--;
        if (currCarGroup == carGroupList.size()) {
            currCarGroup = 0;
        } else if (currCarGroup == -1) {
            currCarGroup = 3;
        }

        if (!rootGroup.getChildren().isEmpty()) {
            rootGroup.getChildren().remove(0);
        }
        rootGroup.getChildren().add(carGroupList.get(currCarGroup));
    }

    /**
     * Moves the car selection to the next one in the arraylist
     *
     * @param event The mouse click event on the button
     */
    @FXML
    private void chooseForward(ActionEvent event) {
        currCarGroup++;
        if (currCarGroup == carGroupList.size()) {
            currCarGroup = 0;
        } else if (currCarGroup == -1) {
            currCarGroup = 3;
        }

        if (!rootGroup.getChildren().isEmpty()) {
            rootGroup.getChildren().remove(0);
        }
        rootGroup.getChildren().add(carGroupList.get(currCarGroup));
    }
}

/**
 * Copyright 2017 John Humphrys
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
import java.util.Random;

/**
 * The choose car controller class manages all controlling aspects when a user
 * is selecting the car and hazards
 *
 * @author John
 *
 */
public class ChooseMode {

    @FXML
    private void startDemo(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/SimulationDemo.fxml"));
        Parent root = (Parent) loader.load();

        DemoController demoControl = loader.<DemoController>getController();

        ArrayList<String> hazardsList = new ArrayList<>();
            hazardsList.add("speedingEvent");
            hazardsList.add("crashEvent");


        // Pass all values to the next controller
        demoControl.setUserChosenCarString("mini-aws.3DS");
        demoControl.setEvents(hazardsList);
        demoControl.setUserView("first");

        Parent p = loader.getRoot();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(p));
        stage.show();
    }


    @FXML
    private void startTest(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/ChooseCar.fxml"));
        Parent root = (Parent) loader.load();

        ChooseCarController simControl = loader.<ChooseCarController>getController();

        Parent p = loader.getRoot();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(p));
        stage.show();
    }

}

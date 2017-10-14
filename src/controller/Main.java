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

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    /**
     * Used to launch the inital JavaFX stage
     *
     * @param args
     */
    public static void main(String[] args) {
        launch();
    }

    /**
     * Starts the login view and controller
     */
    @Override
    public void start(Stage primaryStage) throws Exception {

        // Display initial login screen
        primaryStage.setTitle("Safe Driving Simulator");
        Parent root = FXMLLoader.load(getClass().getResource("../view/TermsConditions.fxml"));

        Scene scene = new Scene(root);

        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
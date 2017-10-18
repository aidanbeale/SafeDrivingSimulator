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

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import model.LoginModel;

import java.io.IOException;

/**
 * The login controller deals with a user trying to log in
 *
 * @author John Humphrys
 *
 */
public class LoginController {

    // Default user/pass combo
    private final String USERNAME = "username";
    private final String PASSWORD = "password";
    private LoginModel lm;

    @FXML
    private JFXTextField usernameField;
    @FXML
    private JFXTextField usernameField2;
    @FXML
    private JFXPasswordField passwordField;
    @FXML
    private JFXButton loginButton;
    @FXML
    private Pane popPane;
    @FXML
    private Label popHeading;
    @FXML
    private Label popMessage;
    @FXML
    private JFXButton errorButton;

    /**
     * Is called when a user attempts to login
     *
     * @param event
     * @throws IOException
     */
    @FXML
    private void handleLogin(ActionEvent event) throws IOException {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.equals(lm.getUsername()) && password.equals(lm.getPassword())) {
            System.out.println("Credentials are correct");

            // Change scene to display terms and conditions
            Parent root = FXMLLoader.load(getClass().getResource("../view/TermsConditions.fxml"));
            Scene scene = new Scene(root);

            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(scene);
            window.show();

        } else {
            System.out.println("Credentials are incorrect");

            // Display error message
            popPane.setDisable(false);
            popPane.setStyle("-fx-background-color: #dddddd;");
            popPane.setOpacity(1.00);
            popHeading.setText("Error");
            popMessage.setText("Login credentials are incorrect, please try again");
            errorButton.setText("Ok");
            errorButton.setStyle("-fx-background-color:  #34495e;");
            errorButton.setDisable(false);
            loginButton.setDisable(true);
        }
    }

    /**
     * Called to restart the scene when the user clicks ok on the popup
     *
     * @param event
     *            The mouse click event on the button
     * @throws IOException
     *             Thrown if fxml file not found
     */
    @FXML
    private void handleOk(ActionEvent event) throws IOException {

        // Change scene to display error
        Parent root = FXMLLoader.load(getClass().getResource("../view/Login.fxml"));
        Scene scene = new Scene(root);

        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();

    }

    /**
     * Disabled the popup button by default
     */
    @FXML
    private void initialize() {
        lm = new LoginModel(USERNAME, PASSWORD);
        errorButton.setDisable(true);
        popPane.setDisable(true);
    }

    /**
     * Display the about screen if pressed
     *
     * @param event
     *            The mouse click event on the button
     * @throws IOException
     *             Thrown if fxml file not found
     */
    @FXML
    private void aboutPressed(ActionEvent event) throws IOException {
        // Change scene to display error
        Parent root = FXMLLoader.load(getClass().getResource("../view/About.fxml"));
        Scene scene = new Scene(root);

        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }
}

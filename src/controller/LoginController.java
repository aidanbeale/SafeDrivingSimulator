package controller;

import java.io.IOException;

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

public class LoginController {

	private final String USERNAME = "username";
	private final String PASSWORD = "password";

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

	@FXML
	private void handleLogin(ActionEvent event) throws IOException {
		String username = usernameField.getText();
		String password = passwordField.getText();

		if (username.equals(USERNAME) && password.equals(PASSWORD)) {
			System.out.println("Credentials are correct");

			// Change scene to display error
			Parent root = FXMLLoader.load(getClass().getResource("../view/TermsConditions.fxml"));
			Scene scene = new Scene(root);

			Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
			window.setScene(scene);
			window.show();

		} else {
			System.out.println("Credentials are incorrect");
				
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
	
	@FXML
	private void handleOk(ActionEvent event) throws IOException {

		// Change scene to display error
		Parent root = FXMLLoader.load(getClass().getResource("../view/Login.fxml"));
		Scene scene = new Scene(root);

		Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
		window.setScene(scene);
		window.show();
		
	}
	
	@FXML
	private void initialize() {
		errorButton.setDisable(true);
	}
	
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

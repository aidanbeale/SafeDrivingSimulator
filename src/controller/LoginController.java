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
import javafx.stage.Stage;

public class LoginController {

	private final String USERNAME = "";
	private final String PASSWORD = "";

	@FXML private JFXTextField usernameField;
	@FXML private JFXTextField usernameField2;
	@FXML private JFXPasswordField passwordField;
	@FXML private JFXButton loginButton;
		
	@FXML
	private void handleLogin(ActionEvent event) throws IOException {
		String username = usernameField.getText();
		String password = passwordField.getText();

		if (username.equals(USERNAME) && password.equals(PASSWORD)) {
			System.out.println("Credentials are correct");
			
			// Change scene to display error
			Parent root = FXMLLoader.load(getClass().getResource("../view/TermsConditions.fxml"));
			Scene scene = new Scene(root);
			
			Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
			window.setScene(scene);
			window.show();

		} else {
			System.out.println("Credentials are incorrect");
			
			AlertBox box = new AlertBox();
			box.display("Error", "Credentials are incorrect, please try again", "Okay", "close");
			

			/*
			// Change scene to display error
			Parent root = FXMLLoader.load(getClass().getResource("../view/LoginError.fxml"));
			Scene scene = new Scene(root);
			
			Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
			window.setScene(scene);
			window.show();
			*/
	}
	}
}

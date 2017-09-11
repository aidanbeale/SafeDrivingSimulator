package application;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;

public class LoginController {
	@FXML
	private JFXTextField usernameField;
	@FXML
	private JFXTextField usernameField2;
	@FXML
	private JFXPasswordField passwordField;
	@FXML
	private JFXButton loginButton;
	
	@FXML
	private void handleAction() {
		String username = usernameField.getText();
		System.out.println("The username is " + username);
		usernameField2.setText(username);
	}
	
	@FXML void initialise() {
		
    
	}
}

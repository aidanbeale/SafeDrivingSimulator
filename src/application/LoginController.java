package application;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class LoginController {
	@FXML
	private JFXTextField usernameField;
	@FXML
	private JFXTextField usernameField2;
	@FXML
	private JFXPasswordField passwordField;
	
	@FXML
	private void handleAction(ActionEvent event) {
		String username = usernameField.getText();
		usernameField2.setText(username);
	}
	
	@FXML
	private void initialise() {
		
	}
}

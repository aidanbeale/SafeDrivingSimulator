package application.Controller;

import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class LoginController implements Initializable {
	
	private final String USERNAME = "John";
	private final String PASSWORD = "john";
	private Scene scene;
	
	@FXML
	private JFXTextField usernameField;
	@FXML
	private JFXTextField usernameField2;
	@FXML
	private JFXPasswordField passwordField;
	@FXML
	private JFXButton loginButton;
	
	@FXML
	private void handleLogin() {
		String username = usernameField.getText();
		String password = passwordField.getText();

		if (username.equals(USERNAME) && password.equals(PASSWORD)) {
			System.out.println("Credentials are correct");
			TermsConditionsController controller = new TermsConditionsController();
			controller.run();
		} else {
			System.out.println("Credentials are incorrect");
		}
	}
	
	public void initialise(Scene scene) {
		this.scene = scene;
    
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
	}
}

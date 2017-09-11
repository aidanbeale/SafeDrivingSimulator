package application;

import java.io.IOException;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GenericPopup extends Application {
	
	@FXML
	private JFXTextField headingTextField;
	@FXML
	private JFXTextArea mainTextArea;
	
	String heading;
	String message;
	Stage primaryStage;
	
	@Override
	public void start(Stage primaryStage) throws IOException {
	    primaryStage.setTitle("Safe Driving Simulator: " + heading);  
	    Parent root = FXMLLoader.load(getClass().getResource("GenericPopup.fxml"));
	    Scene scene = new Scene(root); 
	    primaryStage.setScene(scene);
	    primaryStage.show();
	}
	
	public GenericPopup(Stage primaryStage, String heading, String message) {
		this.primaryStage = primaryStage;
		this.heading = heading;
		this.message = message;
	}
	
}
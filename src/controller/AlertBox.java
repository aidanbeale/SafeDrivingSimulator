package controller;

import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;

import java.io.IOException;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.*;

/**
 * @see https://github.com/buckyroberts/Source-Code-from-Tutorials/blob/master/JavaFX/005_creatingAlertBoxes/AlertBox.java
 *
 */
public class AlertBox {

	@FXML
	private JFXButton button;
	@FXML
	private JFXTextArea messageArea;
	@FXML
	private Label heading;

	public void display(String title, String message, String buttonText, String action) throws IOException {
		Stage window = new Stage();
	
		Parent root = FXMLLoader.load(getClass().getResource("../view/Alert.fxml"));

		// Block events to other windows
		window.initModality(Modality.APPLICATION_MODAL);
		window.setTitle("Safe Driving Simulator");
		
		//heading.setText(title);
		//messageArea.setText(message);
		//button.setText(buttonText);
		//

		// Display window and wait for it to be closed before returning
		Scene scene = new Scene(root);
		window.setScene(scene);
		window.showAndWait();
	}

}
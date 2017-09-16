package controller;

import java.io.IOException;

import com.jfoenix.controls.JFXButton;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class LoginErrorController {

	@FXML private JFXButton errorButton;
	
	@FXML
	private void handleOk(ActionEvent event) throws IOException {
		
		// Change scene to display login screen again
			Parent root = FXMLLoader.load(getClass().getResource("../view/ChooseCar.fxml"));
			Scene scene = new Scene(root);
			
			Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
			window.setScene(scene);
			window.show();
	}
}

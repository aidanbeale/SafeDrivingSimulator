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

public class ChooseCarController {
	
	@FXML private JFXButton chooseCar1;
	@FXML private JFXButton chooseCar2;
	@FXML private JFXButton chooseCar3;
	
	@FXML
	private void chooseCar1(ActionEvent event) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("../view/Simulation.fxml"));
		Scene scene = new Scene(root);
		
		Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
		window.setScene(scene);
		window.show();
	}
	
	@FXML
	private void chooseCar2(ActionEvent event) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("../view/Simulation.fxml"));
		Scene scene = new Scene(root);
		
		Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
		window.setScene(scene);
		window.show();
	}
	
	@FXML
	private void chooseCar3(ActionEvent event) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("../view/Simulation.fxml"));
		Scene scene = new Scene(root);
		
		Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
		window.setScene(scene);
		window.show();
	}
}

package controller;

import java.io.IOException;
import java.util.ArrayList;

import com.jfoenix.controls.JFXButton;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import simulation.Car;

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
	
	private void createCars(String userChoice) {
		carColourList.add("mini-red");
		carColourList.add("mini-green");
		carColourList.add("mini-blue");
		carColourList.add("mini-aws");

		for (String c : carColourList) {
			if (c.equals(userChoice)) {
				carColourList.remove(c);
				break;
			}
		}

		userCar = new Car(0, 0, userChoice, true);
		rootGroup.getChildren().add(userCar.getCarGroup());

		int i = 1;
		ArrayList<Car> aiCarList = new ArrayList<>();
		// Create the other cars
		for (String c : carColourList) {
			Car newCar = new Car(0, -5200 * i, c, false);
			rootGroup.getChildren().add(newCar.getCarGroup());
			aiCarList.add(newCar);
			i++;
		}
		// Assign cars as global
		aiCar1 = aiCarList.get(0);
		aiCar2 = aiCarList.get(1);
		aiCar3 = aiCarList.get(2);
		aiCar3.setxPos(-30000);
		aiCar3.getCarGroup().setRotationAxis(Rotate.Y_AXIS);
		aiCar3.getCarGroup().setRotate(180.0);
		aiCar3.getCarGroup().setTranslateZ(550);
	}
	
}

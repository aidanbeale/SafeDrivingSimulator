package controller;

import java.io.IOException;
import java.util.ArrayList;

import com.jfoenix.controls.JFXButton;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.AmbientLight;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import simulation.Car;

public class ChooseCarController {

	@FXML
	private JFXButton chooseCar1;
	@FXML
	private JFXButton chooseCar2;
	@FXML
	private JFXButton chooseCar3;
	@FXML
	private Group chooseCarGroup;

	PerspectiveCamera camera;
	Group rootGroup = new Group();
	ArrayList<Car> carList = new ArrayList<>();
	private int rotateAmount;
	
	@FXML
	private void initialize() {
		
		// Create cars
		createCars();
		
		// Setup camera
		camera = setupCamera();
		
		// Create Ambient Light
		AmbientLight ambient = new AmbientLight();
		
		// Add ambient light to the group
		rootGroup.getChildren().add(ambient);
		
		// Create subscene
		SubScene subScene = new SubScene(rootGroup, 300, 300, true, SceneAntialiasing.BALANCED);
		subScene.setFill(Color.SKYBLUE);
		subScene.setCamera(camera);
		
		
		// Add subscene to main window
		chooseCarGroup.getChildren().add(subScene);
		
		rotateCarChoice();
	}

	@FXML
	private void chooseCar1(ActionEvent event) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("../view/Simulation.fxml"));
		Scene scene = new Scene(root);

		Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
		window.setScene(scene);
		window.show();
	}

	@FXML
	private void chooseCar2(ActionEvent event) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("../view/Simulation.fxml"));
		Scene scene = new Scene(root);

		Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
		window.setScene(scene);
		window.show();
	}

	@FXML
	private void chooseCar3(ActionEvent event) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("../view/Simulation.fxml"));
		Scene scene = new Scene(root);

		Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
		window.setScene(scene);
		window.show();
	}

	private void createCars() {
		ArrayList<String> carColourList = new ArrayList<>();

		carColourList.add("mini-red");
		carColourList.add("mini-green");
		carColourList.add("mini-blue");
		carColourList.add("mini-aws");

		for (String s : carColourList) {
			Car c = new Car(s);
			rootGroup.getChildren().add(c.getCarGroup());
		}

	}

	private void rotateCarChoice() {

		new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					try {
						Thread.sleep(30); // 30
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					Platform.runLater(new Runnable() {

						@Override
						public void run() {
							rootGroup.setRotationAxis(Rotate.Y_AXIS);
							
							if (rotateAmount == 359) {
								rotateAmount = 0;
							}
							rootGroup.setRotate(rotateAmount++);
						};
					});
				}
			}
		}).start();
	}

	private PerspectiveCamera setupCamera() {
		// Create view camera
		camera = new PerspectiveCamera();

		camera.setTranslateX(-50); // -1202
		camera.setTranslateY(-320); // -420
		camera.setTranslateZ(-320); // -520
		camera.setRotationAxis(Rotate.X_AXIS);
		camera.setRotate(-20.0); // 270
		return camera;
	}

}

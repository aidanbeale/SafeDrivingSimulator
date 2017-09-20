package controller;

import java.io.IOException;
import java.util.ArrayList;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXRadioButton;

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
	@FXML
	private JFXCheckBox checkSpeeding;
	@FXML
	private JFXCheckBox checkCrash;
	@FXML
	private JFXCheckBox checkGiveway;
	@FXML
	private JFXRadioButton firstPersonRadio;
	@FXML
	private JFXRadioButton thirdPersonRadio;

	PerspectiveCamera camera;
	Group rootGroup = new Group();
	ArrayList<Group> carGroupList = new ArrayList<>();
	ArrayList<Car> carList = new ArrayList<>();
	private int rotateAmount;
	private int currCarGroup = 0;

	ArrayList<String> carColourList;
	ArrayList<String> hazardsList = new ArrayList<>();
	String userView;

	@FXML
	private void initialize() {

		// Create cars
		createCars();

		// Setup camera
		camera = setupCamera();

		// Create Ambient Light
		AmbientLight ambient = new AmbientLight();

		// Add ambient light to the group
		// rootGroup.getChildren().add(ambient);

		// Create subscene
		SubScene subScene = new SubScene(rootGroup, 300, 300, true, SceneAntialiasing.BALANCED);
		subScene.setCamera(camera);

		// Add subscene to main window
		chooseCarGroup.getChildren().add(subScene);

		rotateCarChoice();
	}

	@FXML
	private void start(ActionEvent event) throws IOException {

		FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/Simulation.fxml"));
		Parent root = (Parent) loader.load();

		SimulationController simControl = loader.<SimulationController>getController();

		String chosenCar = carColourList.get(currCarGroup);
		addView();
		addHazards();

		simControl.setUserChosenCarString(chosenCar);
		simControl.setEvents(hazardsList);
		simControl.setUserView(userView);

		// sim.setEvents(hazardsList);

		// loader.load();

		Parent p = loader.getRoot();
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		stage.setScene(new Scene(p));
		stage.show();
	}

	private void addView() {
		if (firstPersonRadio.isSelected()) {
			userView = "first";
		} else if (thirdPersonRadio.isSelected()) {
			userView = "third";
		}
	}

	private void addHazards() {
		if (firstPersonRadio.isSelected()) {
			hazardsList.add("speedingEvent");
		}
		if (checkCrash.isSelected()) {
			hazardsList.add("crashEvent");
		}
		if (checkGiveway.isSelected()) {
			hazardsList.add("givewayEvent");
		}
	}

	private void createCars() {
		carColourList = new ArrayList<>();

		carColourList.add("mini-red.3DS");
		carColourList.add("mini-green.3DS");
		carColourList.add("mini-blue.3DS");
		carColourList.add("mini-aws.3DS");

		for (String s : carColourList) {
			Car c = new Car(s);
			carGroupList.add(c.getCarGroup());
		}

	}

	private void rotateCarChoice() {
		rootGroup.getChildren().add(carGroupList.get(currCarGroup));
		currCarGroup++;

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

	@FXML
	private void chooseBack(ActionEvent event) throws IOException {
		currCarGroup--;
		if (currCarGroup == carGroupList.size()) {
			currCarGroup = 0;
		} else if (currCarGroup == -1) {
			currCarGroup = 3;
		}

		rootGroup.getChildren().remove(0);
		rootGroup.getChildren().add(carGroupList.get(currCarGroup));
		
	}

	@FXML
	private void chooseForward(ActionEvent event) throws IOException {
		currCarGroup++;
		if (currCarGroup == carGroupList.size()) {
			currCarGroup = 0;
		} else if (currCarGroup == -1) {
			currCarGroup = 3;
		}

		rootGroup.getChildren().remove(0);
		rootGroup.getChildren().add(carGroupList.get(currCarGroup));
		

	}
}

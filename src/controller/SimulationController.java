package controller;

import java.util.ArrayList;
import java.util.Random;

import com.interactivemesh.jfx.importer.ImportException;
import com.interactivemesh.jfx.importer.tds.TdsModelImporter;
import com.jfoenix.controls.JFXButton;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.AmbientLight;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.PerspectiveCamera;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.transform.Rotate;

public class SimulationController {

	@FXML
	private JFXButton beginSimButton;
	@FXML
	private AnchorPane simAnchor;
	@FXML
	private Group simGroup;
	@FXML
	private Label failurescreen;

	private Group userCarGroup;
	private Group aiCarGroup1;
	private Group aiCarGroup2;
	private Group roadGroup;
	private PerspectiveCamera camera;

	private int transCam = -1200;
	private int userTransCar = 0;
	private int aiTransCar1 = -3200;
	private int aiTransCar2 = -6400;

	private boolean testFailed = false;

	private Random rand = new Random();

	@FXML
	private void handleSimBegin(ActionEvent event) {
		moveUserCar();
	}

	/**
	 * Calls the nessasary methods to initialise the simulation scene
	 */
	@FXML
	private void initialize() {

		// Import car and add to subscene
		Node[] userCarMesh = import3dModel("mini-aws"); // Users choice TODO remove hardcode
		Node[] aiCarMesh1 = import3dModel("mini-blue");
		Node[] aiCarMesh2 = import3dModel("mini-red");

		ArrayList<Group> allGroups = new ArrayList<>();

		userCarGroup = meshIntoGroup(userCarMesh);
		aiCarGroup1 = meshIntoGroup(aiCarMesh1);
		aiCarGroup2 = meshIntoGroup(aiCarMesh2);
		roadGroup = createRoad();

		allGroups.add(userCarGroup);
		allGroups.add(aiCarGroup1);
		allGroups.add(aiCarGroup2);
		allGroups.add(roadGroup);

		camera = setupUserCamera("first");

		// Creating Ambient Light
		AmbientLight ambient = new AmbientLight();
		ambient.setColor(Color.rgb(255, 255, 255, 0.6));

		Group rootGroup = new Group();

		SubScene subScene = addGroupsToSubScene(allGroups, rootGroup, camera, ambient);

		simGroup.getChildren().add(subScene);
	}

	private Group createRoad() {
		roadGroup = new Group();
		Double roadDistance = 0.0;
		Image roadMap = new Image("https://i.imgur.com/T41orEg.jpg");
		for (int i = 0; i < 1000; i++) {
			Box road = new Box(2000.0, 1.0, 2000.0);

			PhongMaterial tpLogo = new PhongMaterial();
			tpLogo.setDiffuseMap(roadMap);
			road.setMaterial(tpLogo);

			road.setTranslateY(95); // Fix road height
			road.setTranslateZ(140); // Centre the road to the car

			road.setTranslateX(roadDistance -= 2000);
			roadGroup.getChildren().add(road);
		}

		return roadGroup;
	}

	private void moveUserCar() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (!testFailed) {
					try {
						Thread.sleep(2);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					Platform.runLater(new Runnable() {
						@Override
						public void run() {

							userCarGroup.setTranslateX(userTransCar -= 2);
							System.out.println("trans car to " + userTransCar);

							camera.setTranslateX(transCam -= 2);
							System.out.println("trans cam to " + transCam);

							aiCarGroup1.setTranslateX(aiTransCar1);
							System.out.println("trans ai1 car to " + aiTransCar1);

							aiCarGroup2.setTranslateX(aiTransCar2);
							System.out.println("trans ai2 car to " + aiTransCar2);

							calculateNextStep();

							if (userTransCar < aiTransCar1 + 1500 || userTransCar < aiTransCar2 + 1500) {
								System.out.print("SIMULATION ENDED");
								failurescreen.setText("YOU FAIL");
								testFailed = true;

							}
						};
					});
				}
			}
		}).start();
	}

	private void calculateNextStep() {
		int randomNumber = rand.nextInt(100);
		System.out.println(randomNumber);
		if (randomNumber > 4) {
			aiTransCar1 -= 2;
		} else if (randomNumber > 15 && randomNumber < 95) {
			aiTransCar1 -= 1;
		} else if (randomNumber > 60 && randomNumber < 95) {
			aiTransCar1 += 1;
		}

		if (randomNumber > 4) {
			aiTransCar2 -= 2;
		} else if (randomNumber > 15 && randomNumber < 95) {
			aiTransCar2 -= 1;
		} else if (randomNumber > 60 && randomNumber < 95) {
			aiTransCar2 += 3;
		}

		/*
		 * if (randomNumber > 2) { aiTransCar2 -= 2; }
		 */

	}

	private PerspectiveCamera setupUserCamera(String camPlacement) {
		// Create view camera
		camera = new PerspectiveCamera();

		if (camPlacement.equals("first")) {
			// First person view
			camera.setTranslateX(-1202); // -1202
			camera.setTranslateY(-420); // -420
			camera.setTranslateZ(-520); // -520
			camera.setRotationAxis(Rotate.Y_AXIS);
			camera.setRotate(270.0); // 270
		} else if (camPlacement.equals("third")) {
			// TODO this
		} else {
			// TODO throws
			System.out.println("no cam");
			return null;
		}
		return camera;
	}

	/**
	 * Method adds a car mesh to a subscene
	 * 
	 * @param ambient
	 * 
	 * @param carMesh
	 *            The carmesh to add to the subscene
	 * @param road
	 * @return The subscene
	 */
	private SubScene addGroupsToSubScene(ArrayList<Group> groupsToAdd, Group rootGroup, PerspectiveCamera camera,
			AmbientLight ambient) {

		for (Group g : groupsToAdd) {
			rootGroup.getChildren().add(g);
		}

		rootGroup.getChildren().add(ambient);

		// Create sub scene
		SubScene subScene = new SubScene(rootGroup, 975, 740, true, SceneAntialiasing.BALANCED);
		subScene.setFill(Color.SKYBLUE);
		subScene.setCamera(camera);

		return subScene;
	}

	/**
	 * This method will import the carmodel requested
	 * 
	 * @param carName
	 *            The file name of the car
	 * @return A group containing the car mesh
	 */
	private Node[] import3dModel(String carName) {

		// Create model importer
		// @see http://www.interactivemesh.org/models/jfx3dimporter.html
		TdsModelImporter modelImporter = new TdsModelImporter();

		try {
			// Read car model from path
			String path = "C:\\Users\\John\\Documents\\UniEclipseplswork\\SafeDrivingSimulator\\src\\carModels\\"
					+ carName + ".3DS";
			modelImporter.read(path);
		} catch (ImportException e) {
			// TODO fix this
			System.out.println("Error importing 3ds model: " + e.getMessage());
			return null;
		}

		// Get car mesh
		Node[] carMesh = modelImporter.getImport();
		modelImporter.close();

		return carMesh;
	}

	private Group meshIntoGroup(Node[] carMesh) {

		// Add car mesh to group
		Group model3D = new Group(carMesh);

		// Set the basic layout of the car
		model3D.setLayoutX(100);
		model3D.setLayoutY(100);

		return model3D;
	}

	@FXML
	private void brakeButtonPressed() {

	}
}

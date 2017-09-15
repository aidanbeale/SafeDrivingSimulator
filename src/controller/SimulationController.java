package controller;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;

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
import javafx.scene.PointLight;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.CullFace;
import javafx.scene.transform.Rotate;
import simulation.Car;
import simulation.EventHandler;
import simulation.EventTimer;
import simulation.Score;

public class SimulationController {

	@FXML
	private JFXButton beginSimButton;
	@FXML
	private AnchorPane simAnchor;
	@FXML
	private Group simGroup;
	@FXML
	private Label failureScreen;
	@FXML
	private Label clockLabel;
	@FXML
	private Label speedLabel;

	private Car userCar;
	private Car aiCar1;
	private Car aiCar2;
	private Car aiCar3;

	private Group roadGroup;
	private PerspectiveCamera camera;

	private int transCam = -1200;

	private boolean testHalt = false;
	private boolean brakePressed = false;

	private String simButtonLabel = "begin";

	private Random rand = new Random();
	private Timer timeSinceOptimal;

	private ArrayList<String> carColourList = new ArrayList<>();
	Group rootGroup = new Group();

	private EventHandler crashEvent;
	private boolean brakesApplied;
	private int userUnitsps = 20;
	private int brakeCount = 0;
	private boolean braking = false;
	private boolean accelerating = false;
	private Score score = new Score();
	private int minute;

	@FXML
	private void handleSimBegin(ActionEvent event) {

		if (simButtonLabel.equals("begin")) {
			beginSimButton.setText("Cancel Simulation");
			simButtonLabel = "cancel";
			moveUserCar();
		} else if (simButtonLabel.equals("cancel")) {
			failureScreen.setText("TEST CANCELLED");
			testHalt = true;

			// Disable buttons
			beginSimButton.setDisable(true);
		}
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

	/**
	 * Calls the nessasary methods to initialise the simulation scene
	 */
	@FXML
	private void initialize() {

		// Create cars
		createCars("mini-aws");

		// Create road
		roadGroup = createRoad();
		rootGroup.getChildren().add(roadGroup);

		// Create camera
		camera = setupUserCamera("first");

		// Create Ambient Light
		AmbientLight ambient = new AmbientLight();
		rootGroup.getChildren().add(ambient);

		// Create subscene
		SubScene subScene = new SubScene(rootGroup, 975, 740, true, SceneAntialiasing.BALANCED);
		subScene.setFill(Color.SKYBLUE);
		subScene.setCamera(camera);

		// Add subscene to main window
		simGroup.getChildren().add(subScene);

		// Start car clock
		initClock();
	}

	private void initClock() {

		final String[] hourArr = { "8", "9", "2", "3" };
		String hour = hourArr[rand.nextInt(hourArr.length)];
		// Dont have to worry about changing hour

		if (hour.equals("2") || hour.equals("9")) {
			minute = rand.nextInt(25);
		} else if (hour.equals("8") || hour.equals("3")) {
			minute = rand.nextInt(54);
		}
		
		if (String.valueOf(minute).length() == 1) {
			clockLabel.setText(hour + ":0" + minute);
		} else {
			clockLabel.setText(hour + ":" + minute);
		}

		new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					try {
						Thread.sleep(60000); // every minute
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					Platform.runLater(new Runnable() {

						@Override
						public void run() {
							minute++;
							if (String.valueOf(minute).length() == 1) {
								clockLabel.setText(hour + ":0" + minute);
							} else {
								clockLabel.setText(hour + ":" + minute);
							}
							

						}
					});
				}

			};

		}).start();
	}

	private Group createRoad() {
		roadGroup = new Group();
		Double roadDistance = 0.0;

		PhongMaterial roadSurface = new PhongMaterial();
		roadSurface.setDiffuseMap(new Image("images\\asphalt.jpg"));
		roadSurface.setSpecularColor(Color.WHITE);

		for (int i = 0; i < 200; i++) {
			Box road = new Box(1624.0, 10.0, 6600.0);
			road.setMaterial(roadSurface);
			road.setTranslateY(95); // Fix road height
			road.setTranslateZ(140); // Centre the road to the car
			road.setTranslateX(roadDistance -= 1624);
			roadGroup.getChildren().add(road);
		}

		return roadGroup;
	}

	private void moveUserCar() {

		crashEvent = new EventHandler(false, true, false);

		new Thread(new Runnable() {
			@Override
			public void run() {
				while (!testHalt) {
					try {
						Thread.sleep(30); // 30
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					Platform.runLater(new Runnable() {

						@Override
						public void run() {

							if (braking) {

								if (userUnitsps > 3) {
									userUnitsps -= 1;
								} else {
									braking = false;
									accelerating = true;
								}

							} else if (accelerating) {
								if (userUnitsps < 20) {
									userUnitsps += 1;
								} else {
									accelerating = false;
								}
							}

							userCar.setxPos(userCar.getxPos() - userUnitsps);
							userCar.getCarGroup().setTranslateX(userCar.getxPos());
							// System.out.println("trans car to " + userCar.getxPos());

							camera.setTranslateX(transCam -= userUnitsps);
							// System.out.println("trans cam to " + transCam);

							aiCar1.setxPos(aiCar1.getxPos() - 18);
							aiCar1.getCarGroup().setTranslateX(aiCar1.getxPos());
							// System.out.println("trans ai1 car to " + aiCar1.getxPos());

							aiCar2.setxPos(aiCar2.getxPos() - 18);
							aiCar2.getCarGroup().setTranslateX(aiCar2.getxPos());
							// System.out.println("trans ai2 car to " + aiCar2.getxPos());

							aiCar3.setxPos(aiCar3.getxPos() + 20);
							aiCar3.getCarGroup().setTranslateX(aiCar3.getxPos());
							// System.out.println("trans ai3 car to " + aiCar3.getxPos());

							// calculateNextStep();

							// Should apply brake now
							if ((userCar.getxPos() < aiCar1.getxPos() + 2000
									|| userCar.getxPos() < aiCar2.getxPos() + 2000) && !crashEvent.getTimerStarted()) {

								crashEvent.startCrashEvent();

								//// System.out.print("SIMULATION ENDED");
							} else if (userCar.getxPos() < aiCar1.getxPos() + 480 // FAILURE
									|| userCar.getxPos() < aiCar2.getxPos() + 480) {

								if (!crashEvent.isTimerStopped()) {
									crashEvent.stopCrashEvent();
									crashEvent.setTimerStopped(true);
									//// System.out.print("SIMULATION ENDED");
									failureScreen.setText("YOU FAIL Score: " + crashEvent.calculateScore());
									testHalt = true;
								}

							}
						};
					});
				}
			}
		}).start();
	}

	/*
	 * private void calculateNextStep() { int randomNumber = rand.nextInt(100);
	 * System.out.println(randomNumber); if (randomNumber > 4) { aiCar1.setxPos() =
	 * aiCar1.getxPos() - 2; } else if (randomNumber > 15 && randomNumber < 95) {
	 * aiTransCar1 -= 1; } else if (randomNumber > 60 && randomNumber < 95) {
	 * aiTransCar1 += 1; }
	 * 
	 * if (randomNumber > 4) { aiTransCar2 -= 2; } else if (randomNumber > 15 &&
	 * randomNumber < 95) { aiTransCar2 -= 1; } else if (randomNumber > 60 &&
	 * randomNumber < 95) { aiTransCar2 += 3; }
	 * 
	 * 
	 * 
	 * }
	 */

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
			// Third person view
			camera.setTranslateX(-800); // -1202
			camera.setTranslateY(-320); // -420
			camera.setTranslateZ(500); // -520
			// camera.setRotationAxis(Rotate.Y_AXIS);
			// camera.setRotate(0.0); // 270
			camera.setRotationAxis(Rotate.X_AXIS);
			camera.setRotate(270.0);
			// camera.setRotationAxis(Rotate.Z_AXIS);
			// camera.setRotate(0.0);
		} else {
			// TODO throws
			System.out.println("no cam");
			return null;
		}
		return camera;
	}

	private PointLight testLightPoint() {

		PointLight pointLight = new PointLight(Color.WHITE);
		pointLight.setTranslateX(-2300);
		pointLight.setTranslateY(-820);
		pointLight.setTranslateZ(-800);
		// pointLight.setRotate(0);

		return pointLight;
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

		// If crash event occuring
		if (crashEvent.getTimerStarted()) {
			crashEvent.stopCrashEvent();
			crashEvent.setTimerStopped(true);

			braking = true;

			failureScreen.setText("YOU applied the brakes correctly  Score: " + crashEvent.calculateScore());

		} else if (!crashEvent.getTimerStarted()) { // if no events have started
			failureScreen.setText("brakes early   Score: -100 ");
			score.setLostScore(score.getLostScore() - 100);
			braking = true;
		}
	}
}

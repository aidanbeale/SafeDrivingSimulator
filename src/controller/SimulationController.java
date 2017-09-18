package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;

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
import javafx.scene.PointLight;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import simulation.Car;
import simulation.EventHandler;
import simulation.Score;
import simulation.SimObject;

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
	@FXML
	private Label timeRemainingLabel;
	@FXML
	private Button brakeButton;
	@FXML
	private JFXButton displayScore;

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
	private final int SPEED_LIMIT = 40;
	private int userUnitsps = SPEED_LIMIT;
	private int aiUnitsps = SPEED_LIMIT;
	private boolean braking = false;
	private boolean accelerating = false;
	private boolean acceleratingBreak = false;
	private int minute;
	private int simulationTime = 300;
	private boolean simulationRunning = false;
	private ArrayList<Score> scoringOps = new ArrayList<>();
	private ArrayList<String> events = new ArrayList<>();

	@FXML
	private void handleSimBegin(ActionEvent event) {
		simulationRunning = true;
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

		// Start countdown
		simulationCountdown();

		EventHandler crashEvent = new EventHandler();
		crashEvent.startCrashEvent(aiCar1, aiCar2);
	}

	private void createCars(String userChoice) {
		carColourList.add("mini-red.3DS");
		carColourList.add("mini-green.3DS");
		carColourList.add("mini-blue.3DS");
		carColourList.add("mini-aws.3DS");

		for (String c : carColourList) {
			if (c.equals(userChoice)) {
				carColourList.remove(c);
				break;
			}
		}

		userCar = new Car(40, 0, userChoice, true);
		rootGroup.getChildren().add(userCar.getCarGroup());

		int i = 1;
		ArrayList<Car> aiCarList = new ArrayList<>();
		// Create the other cars
		for (String c : carColourList) {
			Car newCar = new Car(40, -5200 * i, c, false);
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

	private Group createObjects(int startOfBox, int boxLength) {
		Group objGroup = new Group();
		Double distBetweenSign = 1000.0;
		Double distBetweenTree = 100.0;

		SimObject powerline = new SimObject("scSign.3DS", startOfBox, 0, -720);

		// SimObject tree = new SimObject("Tree 4.3ds",
		// rand.nextInt((startOfBox + boxLength) - startOfBox + 1) + startOfBox, 0,
		// rand.nextInt((startOfBox + boxLength) - startOfBox + 1) + startOfBox);

		objGroup.getChildren().add(powerline.getObjGroup());
		// objGroup.getChildren().add(tree.getObjGroup());
		return objGroup;
	}

	/**
	 * Calls the nessasary methods to initialise the simulation scene
	 */
	@FXML
	private void initialize() {

		// Create cars
		createCars("mini-aws.3DS");

		// Create road
		roadGroup = createRoad();
		rootGroup.getChildren().add(roadGroup);

		// Create camera
		camera = setupUserCamera("first");

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

	private void simulationCountdown() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (!testHalt) {
					try {
						Thread.sleep(1000); // every second
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					Platform.runLater(new Runnable() {

						@Override
						public void run() {
							timeRemainingLabel.setText(String.valueOf(simulationTime--));

							if (simulationTime == 0) {
								testHalt = true;
							}
						}
					});
				}

			};

		}).start();
	}

	private Group createRoad() {
		roadGroup = new Group();
		int roadDistance = 0;
		int boxSize = 1624;
		int numberOfBoxes = 200;

		PhongMaterial roadSurface = new PhongMaterial();
		roadSurface.setDiffuseMap(new Image("images\\asphalt.jpg"));
		roadSurface.setSpecularColor(Color.WHITE);

		for (int i = 0; i < numberOfBoxes; i++) {
			Box road = new Box(boxSize, 10.0, 6600.0);
			road.setMaterial(roadSurface);
			road.setTranslateY(95); // Fix road height
			road.setTranslateZ(140); // Centre the road to the car
			road.setTranslateX(roadDistance -= boxSize);
			roadGroup.getChildren().add(road);
			roadGroup.getChildren().add(createObjects(roadDistance, boxSize));
		}

		// Create Ambient Light
		AmbientLight ambient = new AmbientLight();
		roadGroup.getChildren().add(ambient);

		return roadGroup;
	}

	private void moveUserCar() {

		crashEvent = new EventHandler();

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

								if (userUnitsps > 15) {
									userUnitsps -= 1;
								} else {
									braking = false;
									accelerating = true;
								}

							} else if (accelerating) {
								if (userUnitsps < 40) {
									if (!acceleratingBreak) {
										userUnitsps += 1;
										acceleratingBreak = true;
									} else {
										acceleratingBreak = false;
									}
								} else {
									accelerating = false;
								}
							}

							userCar.setxPos(userCar.getxPos() - userUnitsps);
							speedLabel.setText(String.valueOf((userUnitsps) + "km/h"));
							userCar.getCarGroup().setTranslateX(userCar.getxPos());
							System.out.println(userCar.getxPos());

							camera.setTranslateX(transCam -= userUnitsps);
							// System.out.println("trans cam to " + transCam);

							aiCar1.setxPos(aiCar2.getxPos() - (aiCar1.getSpeed()));
							aiCar1.getCarGroup().setTranslateX(aiCar1.getxPos());
							// System.out.println("trans ai1 car to " + aiCar1.getxPos());

							aiCar2.setxPos(aiCar2.getxPos() - (aiCar2.getSpeed()));
							aiCar2.getCarGroup().setTranslateX(aiCar2.getxPos());
							// System.out.println("trans ai2 car to " + aiCar2.getxPos());

							aiCar3.setxPos(aiCar3.getxPos() + SPEED_LIMIT);
							aiCar3.getCarGroup().setTranslateX(aiCar3.getxPos());
							// System.out.println("trans ai3 car to " + aiCar3.getxPos());

							// Should apply brake now
							if ((userCar.getxPos() < aiCar1.getxPos() + 2000
									|| userCar.getxPos() < aiCar2.getxPos() + 2000) && !crashEvent.getTimerStarted()) {

								crashEvent.startCrashEventTimer();

								//// System.out.print("SIMULATION ENDED");
							} else if (userCar.getxPos() < aiCar1.getxPos() + 480 // FAILURE
									|| userCar.getxPos() < aiCar2.getxPos() + 480) {

								if (!crashEvent.isTimerStopped()) {
									crashEvent.stopCrashEventTimer();
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
			camera.setTranslateX(2000); // -1202
			camera.setTranslateY(-620); // -420
			camera.setTranslateZ(-500); // -520
			// camera.setRotationAxis(Rotate.Y_AXIS);
			// camera.setRotate(0.0); // 270
			// camera.setRotationAxis(Rotate.Y_AXIS);
			// camera.setRotate(270.0);
			camera.setRotationAxis(Rotate.X_AXIS);
			camera.setRotate(-45.0);

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

		braking = true;
		tempDisableBrakeButton();

		// If crash event occuring
		if (crashEvent.getTimerStarted()) {
			crashEvent.stopCrashEventTimer();
			crashEvent.setTimerStopped(true);

			// tempDisableBrakeButton();

			Score score = new Score("crashevent", crashEvent.getTimerStartedTime(), crashEvent.getTimerStoppedTime());
			scoringOps.add(score);

			if (score.getScore() != 0) {
				failureScreen.setText("Brakes applied correctly.  Score: " + score.getScore());
			} else {
				failureScreen.setText("Brakes applied correctly. Too slow to react.  Score: " + score.getScore());
			}

			crashEvent = new EventHandler();

			endOldEvent();
			assignRandomEventTime();

		} else if (!crashEvent.getTimerStarted()) { // if no events have started
			failureScreen.setText("Brakes applied incorrectly. Score: -300 ");

			// tempDisableBrakeButton();

			Score score = new Score("failedAttempt", -300);
			scoringOps.add(score);
			endOldEvent();
			assignRandomEventTime();
		}
	}

	private void tempDisableBrakeButton() {

		final Thread t = new Thread(new Runnable() {

			@Override
			public void run() {
				brakeButton.setDisable(true);

				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				brakeButton.setDisable(false);

			}
		});
		t.start();
	}

	private void assignRandomEventTime() {

		// TODO choose events depending on user choice

		// No random events in the first 10 seconds
		final int MAX_EVENTS = (simulationTime - 10) / 30;

		// Wait minimum of 10 seconds between events starting and max of 30
		int eventBreak = rand.nextInt(20) + 10;

		final Thread t = new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					Thread.sleep(eventBreak);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				while (simulationRunning) {
					int eventType = rand.nextInt(events.size());
					String eventName;

					if (events.get(eventType).equals("crashEvent")) {
						EventHandler crashEvent = new EventHandler();
						crashEvent.startCrashEvent(aiCar1, aiCar2);

					} else if (events.get(eventType).equals("speedingEvent")) {

					}

					// Until event ends
				}

			}
		});
		t.start();

	}

	/**
	 * Resets everything back to initial values ready for a new event
	 */
	private void endOldEvent() {

		final Thread t = new Thread(new Runnable() {

			@Override
			public void run() {

				// Move cars away from user (Crash event)
				aiCar1.setSpeed((int) (SPEED_LIMIT * 1.2));
				aiCar2.setSpeed((int) (SPEED_LIMIT * 1.2));
				
				try {
					// Sleep for 5 seconds
					Thread.sleep(5000);
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				// Restore cars (Crash event)
				aiCar1.setSpeed(SPEED_LIMIT);
				aiCar2.setSpeed(SPEED_LIMIT);
			}

		});
		t.start();
	}

	@FXML
	private void displayScores(ActionEvent event) throws IOException {

		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("../view/Results.fxml"));

		loader.load();

		ResultsController results = loader.getController();
		results.setScoringOps(scoringOps);

		Parent p = loader.getRoot();
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		stage.setScene(new Scene(p));
		stage.show();
	}

}

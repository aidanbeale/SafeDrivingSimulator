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
import javafx.scene.layout.Pane;
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
	private Pane pane;
	@FXML
	private JFXButton displayScore;
	@FXML
	private Label heading;

	private Car userCar;
	private Car aiCar1;
	private Car aiCar2;
	private Car aiCar3;

	private Group roadGroup;
	private PerspectiveCamera camera;
	private boolean userSpeeding = false;

	private boolean testHalt = false;
	private boolean brakePressed = false;

	private boolean eventRunning = true;

	private String simButtonLabel = "begin";

	private Random rand = new Random();
	private Timer timeSinceOptimal;

	private ArrayList<String> carColourList = new ArrayList<>();
	Group rootGroup = new Group();

	private EventHandler crashEvent;
	private EventHandler speedingEvent;
	private EventHandler givewayEvent;
	private final int SPEED_LIMIT = 40;
	private boolean braking = false;
	private boolean accelerating = false;
	private boolean acceleratingBreak = false;
	private int minute;
	private int simulationTime = 100;
	private ArrayList<Score> scoringOps = new ArrayList<>();

	private int randomiseCarSpeedCounter = 0;
	private Group givewayGroup;

	private String userChosenCarString;
	private ArrayList<String> events = new ArrayList<>();
	private String userView;

	@FXML
	private void handleSimBegin(ActionEvent event) {
		if (simButtonLabel.equals("begin")) {
			initialize();
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

		assignRandomEventTime();

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

		userCar = new Car(40, 0, userChoice, true, SPEED_LIMIT);
		rootGroup.getChildren().add(userCar.getCarGroup());

		int i = 1;
		ArrayList<Car> aiCarList = new ArrayList<>();
		// Create the other cars
		for (String c : carColourList) {
			Car newCar = new Car(40, -5200 * i, c, false, SPEED_LIMIT);
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

	private void createSign() {
		int boxSize = 1624 * 20;
		int numberOfBoxes = 50;
		int signDistance = 0;

		PhongMaterial schoolZone = new PhongMaterial();
		schoolZone.setDiffuseMap(new Image("images\\schoolzone.png"));

		for (int i = 0; i < numberOfBoxes; i++) {
			Box schoolZoneSign = new Box(300, 10, 300);
			schoolZoneSign.setMaterial(schoolZone);
			schoolZoneSign.setTranslateY(-400); // Fix road height
			schoolZoneSign.setTranslateZ(-700); // Centre the road to the car
			schoolZoneSign.setTranslateX(signDistance -= boxSize);
			schoolZoneSign.getTransforms().add(new Rotate(90, Rotate.Z_AXIS));
			schoolZoneSign.getTransforms().add(new Rotate(270, Rotate.Y_AXIS));
			roadGroup.getChildren().add(schoolZoneSign);

			PhongMaterial postMaterial = new PhongMaterial();
			postMaterial.setDiffuseColor(Color.GRAY);

			Box post = new Box(10, 10, 500);
			post.setMaterial(postMaterial);
			post.setTranslateY(-100); // Fix road height
			post.setTranslateZ(-700); // Centre the road to the car
			post.setTranslateX(schoolZoneSign.getTranslateX() - 10);
			post.getTransforms().add(new Rotate(90, Rotate.Z_AXIS));
			post.getTransforms().add(new Rotate(270, Rotate.Y_AXIS));
			roadGroup.getChildren().add(post);
		}

	}

	private Group createObjects(int startOfBox, int boxLength) {
		Group objGroup = new Group();
		Double distBetweenSign = 1000.0;
		Double distBetweenTree = 100.0;

		// SimObject tree = new SimObject("Tree 4.3ds",
		// rand.nextInt((startOfBox + boxLength) - startOfBox + 1) + startOfBox, 0,
		// rand.nextInt((startOfBox + boxLength) - startOfBox + 1) + startOfBox);

		// objGroup.getChildren().add(tree.getObjGroup());
		createSign();
		return objGroup;
	}

	/**
	 * Calls the nessasary methods to initialise the simulation scene
	 */
	private void initialize() {
		// Create cars
		createCars(userChosenCarString);

		// Create road
		roadGroup = createRoad();
		rootGroup.getChildren().add(roadGroup);

		// Create camera
		camera = setupUserCamera(userView);

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

								pane.setStyle("-fx-background-color: #F4F4F4;");
								heading.setText("Congratulations!");
								displayScore.setText("Display Scores");

							}
						}
					});
				}

			};

		}).start();
	}

	private Group createRoad() {
		roadGroup = new Group();
		int roadDistance = 1624;
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

		if (events.contains("givewayEvent")) {
			// TODO Create three give way events by default

			givewayEvent = new EventHandler();
			int loc1 = -(rand.nextInt(100000));
			int loc2 = -(rand.nextInt(100000) + 100000);
			int loc3 = -(rand.nextInt(100000) + 200000);

			System.out.println("locs:" + loc1 + " " + loc2 + " " + loc3);

			givewayEvent.addGivewayLocation(loc1);
			givewayEvent.addGivewayLocation(loc2);
			givewayEvent.addGivewayLocation(loc3);

			roadGroup.getChildren().add(createGivewayEvent(loc1));
			roadGroup.getChildren().add(createGivewayEvent(loc2));
			roadGroup.getChildren().add(createGivewayEvent(loc3));
		}

		return roadGroup;
	}

	/*
	 * private void cleanupOldRoad() { if(userCar.getxPos() >
	 * rootGroup.getChildren(). }
	 */

	private int randomiseCarSpeed(Car car) {
		final int minCarSpeed = car.getCarSpeedLimit() - 3;
		final int maxCarSpeed = car.getCarSpeedLimit();

		int randomSpeed = rand.nextInt(maxCarSpeed - minCarSpeed) + minCarSpeed;
		return randomSpeed;
	}

	private void moveUserCar() {

		crashEvent = new EventHandler();
		speedingEvent = new EventHandler();
		// givewayEvent = new EventHandler();

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
							boolean breakingBreak = false;
							int acceleratingBreakCounter = 0;

							if (braking) {

								if (!breakingBreak) {
									if (userCar.getCarSpeedLimit() > 15) {
										userCar.setCarSpeedLimit(userCar.getCarSpeedLimit() - 1);
										breakingBreak = true;
									} else {
										braking = false;
										accelerating = true;
									}
								} else if (breakingBreak) {
									breakingBreak = false;
								}

							} else if (accelerating) {
								if (userCar.getCarSpeedLimit() < SPEED_LIMIT) {
									if (!acceleratingBreak && acceleratingBreakCounter % 2 == 0) {
										userCar.setCarSpeedLimit(userCar.getCarSpeedLimit() + 1);
										acceleratingBreak = true;
									} else {
										acceleratingBreak = false;
										acceleratingBreakCounter++;
									}
								} else {
									accelerating = false;
								}
							}

							randomiseCarSpeedCounter++;

							if (randomiseCarSpeedCounter % 10 == 0) {

								userCar.setSpeed(randomiseCarSpeed(userCar));
								aiCar1.setSpeed(randomiseCarSpeed(aiCar1));
								aiCar2.setSpeed(randomiseCarSpeed(aiCar2));
								aiCar3.setSpeed(randomiseCarSpeed(aiCar3));
							}

							userCar.setxPos(userCar.getxPos() - userCar.getSpeed());
							speedLabel.setText(String.valueOf((userCar.getSpeed()) + "km/h"));
							userCar.getCarGroup().setTranslateX(userCar.getxPos());
							// System.out.println(userCar.getxPos());

							camera.setTranslateX(camera.getTranslateX() - userCar.getSpeed());
							// System.out.println("trans cam to " + transCam);

							aiCar1.setxPos(aiCar1.getxPos() - (aiCar1.getSpeed()));
							aiCar1.getCarGroup().setTranslateX(aiCar1.getxPos());
							// System.out.println("trans ai1 car to " + aiCar1.getxPos());

							aiCar2.setxPos(aiCar2.getxPos() - (aiCar2.getSpeed()));
							aiCar2.getCarGroup().setTranslateX(aiCar2.getxPos());
							// System.out.println("trans ai2 car to " + aiCar2.getxPos());

							aiCar3.setxPos(aiCar3.getxPos() + SPEED_LIMIT);
							aiCar3.getCarGroup().setTranslateX(aiCar3.getxPos());
							// System.out.println("trans ai3 car to " + aiCar3.getxPos());

							// Should apply brake now
							checkBrakeRequired();

						};
					});
				}
			}
		}).start();
	}

	private void checkBrakeRequired() {

		/*
		 * Crash event
		 */
		if (events.contains("crashEvent")) {
			if ((userCar.getxPos() < aiCar1.getxPos() + 3000 || userCar.getxPos() < aiCar2.getxPos() + 3000)
					&& !crashEvent.getTimerStarted()) {
				System.out.println("---------Crash event timer started---------");
				crashEvent.startEventTimer();

				//// System.out.print("SIMULATION ENDED");
			} else if (userCar.getxPos() < aiCar1.getxPos() + 480 // FAILURE
					|| userCar.getxPos() < aiCar2.getxPos() + 480) {

				if (!crashEvent.isTimerStopped()) {
					crashEvent.stopEventTimer();
					crashEvent.setTimerStopped(true);
					//// System.out.print("SIMULATION ENDED");
					failureScreen.setText("YOU FAIL");
					testHalt = true;
				}
			}
		}

		/*
		 * Speeding event
		 */
		if (events.contains("speedingEvent")) {
			if (!userSpeeding) {
				if (userCar.getSpeed() > SPEED_LIMIT) {
					userSpeeding = true;
					System.out.println("---------Speeding event timer started---------");
					speedingEvent.startEventTimer();
				} else if (userCar.getSpeed() >= SPEED_LIMIT + 20) {
					if (!speedingEvent.isTimerStopped()) {
						speedingEvent.stopEventTimer();
						speedingEvent.setTimerStopped(true);
						failureScreen.setText("YOU FAIL");
						testHalt = true;

					}
				}
			}

		}

		/*
		 * Giveway event
		 */
		if (events.contains("givewayEvent")) {
			if ((userCar.getxPos() < givewayEvent.getClosestGivewayLoc() + 10000) && !givewayEvent.getTimerStarted()) {
				System.out.println("Giveway timer started");
				System.out.println("---------Giveway event timer started---------");
				givewayEvent.startEventTimer();

				//// System.out.print("SIMULATION ENDED");
			} else if (userCar.getxPos() < givewayEvent.getClosestGivewayLoc()) {

				if (!givewayEvent.isTimerStopped()) {
					givewayEvent.stopEventTimer();
					givewayEvent.setTimerStopped(true);
					//// System.out.print("SIMULATION ENDED");
					failureScreen.setText("YOU FAIL");
					testHalt = true;
				}
			}
		}
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
			camera.setTranslateX(600); // -1202
			camera.setTranslateY(-720); // -420
			camera.setTranslateZ(-520); // -520
			camera.getTransforms().add((new Rotate(-17.0, Rotate.Z_AXIS)));
			camera.getTransforms().add((new Rotate(-90.0, Rotate.Y_AXIS)));

		} else {
			// TODO throws
			System.out.println("no cam");
			return null;
		}
		return camera;
	}

	@FXML
	private void brakeButtonPressed() {

		braking = true;
		eventRunning = false;

		tempDisableBrakeButton();

		if (!crashEvent.getTimerStarted() || crashEvent == null && !speedingEvent.getTimerStarted()
				|| speedingEvent == null && !givewayEvent.getTimerStarted() || givewayEvent == null) { // if
			// no
			// events
			// have
			// started
			failureScreen.setText("Brakes applied incorrectly. Score: -300 ");

			// tempDisableBrakeButton();

			Score score = new Score("failedAttempt", -300);
			scoringOps.add(score);

		}

		// If crash event occuring
		if (events.contains("crashEvent")) {
			if (crashEvent.getTimerStarted()) {
				crashEvent.stopEventTimer();
				crashEvent.setTimerStopped(true);

				Score score = new Score("crashevent", crashEvent.getTimerStartedTime(),
						crashEvent.getTimerStoppedTime());
				scoringOps.add(score);

				if (score.getScore() != 0) {
					failureScreen.setText("Brakes applied correctly.  Score: " + score.getScore());
				} else {
					failureScreen.setText("Brakes applied correctly but too slow to react.");
				}

				crashEvent = new EventHandler();
			}
		}

		// If speeding event occuring
		if (events.contains("speedingEvent")) {
			if (speedingEvent.getTimerStarted()) {
				speedingEvent.stopEventTimer();
				speedingEvent.setTimerStopped(true);

				Score score = new Score("speedingEvent", speedingEvent.getTimerStartedTime(),
						speedingEvent.getTimerStoppedTime());
				scoringOps.add(score);

				if (score.getScore() != 0) {
					failureScreen.setText("Brakes applied correctly.  Score: " + score.getScore());
				} else {
					failureScreen.setText("Brakes applied correctly but too slow to react.");
				}

				speedingEvent = new EventHandler();
			}
		}
		// if giveway event occuring
		if (events.contains("givewayEvent")) {
			if (givewayEvent.getTimerStarted())

			{
				givewayEvent.stopEventTimer();
				givewayEvent.setTimerStopped(true);

				Score score = new Score("givewayEvent", givewayEvent.getTimerStartedTime(),
						givewayEvent.getTimerStoppedTime());
				scoringOps.add(score);

				if (score.getScore() != 0) {
					failureScreen.setText("Brakes applied correctly.  Score: " + score.getScore());
				} else {
					failureScreen.setText("Brakes applied correctly but too slow to react.");
				}

				if (givewayEvent.getClosestGivewayLoc() == givewayEvent.getGivewayLocations().get(0)) {
					givewayEvent.getGivewayLocations().remove(0);
				}

			}
		}

		endOldEvent();
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
		t.setDaemon(true);
		t.start();
	}

	private void assignRandomEventTime() {

		// TODO choose events depending on user choice

		// No random events in the first 10 seconds
		final int MAX_EVENTS = (simulationTime - 10) / 30;

		// Wait minimum of 10 seconds between events starting and max of 30
		int eventBreak = rand.nextInt(10000) + 10000;

		final Thread t = new Thread(new Runnable() {

			@Override
			public void run() {

				try {
					Thread.sleep(eventBreak);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println("breaks done");

				int eventType;
				String eventName;

				// Choose event
				if (events.size() == 0) {
					eventType = 0;
				} else {
					eventType = rand.nextInt((events.size()));
				}

				while (eventRunning) {

					// Start Crash event
					if (events.get(eventType).equals("crashEvent")) {
						System.out.println("crashEvent started");
						
						EventHandler crashEvent = new EventHandler();
						crashEvent.startCrashEvent(aiCar1, aiCar2);

						// Sleep for one second to keep randomising speeds
						System.out.println("Sleeping..");
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						// Start Speeding event
					} else if (events.get(eventType).equals("speedingEvent")) {
						System.out.println("speedingEvent started");
						EventHandler speedingEvent = new EventHandler();
						speedingEvent.startSpeedingEvent(userCar);

						// Sleep for one second to keep randomising speeds
						System.out.println("Sleeping..");
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						// Start Giveway event
					} else if (events.get(eventType).equals("givewayEvent")) {
						System.out.println("givewayEvent started");
						EventHandler givewayEvent = new EventHandler();
						int posOfGiveway = givewayEvent.startGivewayEvent(userCar);
						// givewayGroup = createGivewayEvent(posOfGiveway);
					}

				}
			}
		});
		t.setDaemon(true);
		t.start();
	}

	private Group createGivewayEvent(int xPos) {
		Group givewayGroup = new Group();

		PhongMaterial givewayRoad = new PhongMaterial();
		givewayRoad.setDiffuseMap(new Image("images\\givewayRoad.jpg"));

		Box givewayRoadBox = new Box(1624, 11, 6600);
		givewayRoadBox.setMaterial(givewayRoad);
		givewayRoadBox.setTranslateY(95); // Fix road height
		givewayRoadBox.setTranslateZ(140); // Centre the road to the car
		givewayRoadBox.setTranslateX(xPos);
		givewayGroup.getChildren().add(givewayRoadBox);

		PhongMaterial giveway = new PhongMaterial();
		giveway.setDiffuseMap(new Image("images\\giveway.jpg"));

		Box givewaySign = new Box(200, 11, 200);
		givewaySign.setMaterial(giveway);
		givewaySign.setTranslateY(-200); // Fix road height
		givewaySign.setTranslateZ(-700); // Centre the road to the car
		givewaySign.setTranslateX(xPos + 1000);
		givewaySign.getTransforms().add(new Rotate(90, Rotate.Z_AXIS));
		givewaySign.getTransforms().add(new Rotate(270, Rotate.Y_AXIS));
		givewayGroup.getChildren().add(givewaySign);

		PhongMaterial postMaterial = new PhongMaterial();
		postMaterial.setDiffuseColor(Color.GRAY);

		Box post = new Box(10, 11, 500);
		post.setMaterial(postMaterial);
		post.setTranslateY(givewaySign.getTranslateY() + 100); // Fix road height
		post.setTranslateZ(givewaySign.getTranslateZ()); // Centre the road to the car
		post.setTranslateX(givewaySign.getTranslateX() - 10);
		post.getTransforms().add(new Rotate(90, Rotate.Z_AXIS));
		post.getTransforms().add(new Rotate(270, Rotate.Y_AXIS));
		givewayGroup.getChildren().add(post);

		return givewayGroup;

	}

	/**
	 * Resets everything back to initial values ready for a new event
	 */
	private void endOldEvent() {

		final Thread t = new Thread(new Runnable() {

			@Override
			public void run() {

				eventRunning = false;
				userSpeeding = false;

				// Move cars away from user (Crash event)
				aiCar1.setCarSpeedLimit((int) (SPEED_LIMIT * 1.2));
				aiCar2.setCarSpeedLimit((int) (SPEED_LIMIT * 1.2));

				try {
					// Sleep for 5 seconds
					Thread.sleep(5000);
				} catch (Exception e) {
					e.printStackTrace();
				}

				// Restore cars (Crash event)
				aiCar1.setSpeed(SPEED_LIMIT);
				aiCar2.setSpeed(SPEED_LIMIT);
				userCar.setSpeed(SPEED_LIMIT);
				eventRunning = true;
				assignRandomEventTime();
			}

		});
		t.setDaemon(true);
		t.start();
	}

	@FXML
	private void displayScores(ActionEvent event) throws IOException {

		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("../view/Results.fxml"));

		ResultsController results = loader.getController();
		results.setScoringOps(scoringOps);

		loader.load();

		Parent p = loader.getRoot();
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		stage.setScene(new Scene(p));
		stage.show();
	}

	public void setUserChosenCarString(String userChosenCarString) {
		this.userChosenCarString = userChosenCarString;
	}

	public void setEvents(ArrayList<String> events) {
		this.events = events;
	}

	public void setUserView(String userView) {
		this.userView = userView;
	}

}

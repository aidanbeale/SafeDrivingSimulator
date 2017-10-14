/**
 * Copyright 2017 John Humphrys
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package controller;

import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

/**
 * The simulation controller manages all aspects of the controller. Is made up
 * of several models and other classes located in the 'model' package.
 *
 * @author John Humphrys
 */
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

    private SimObject person;
    private SimObject person2;
    private int personIncrement = 13;

    private ArrayList<Car> extraCarList = new ArrayList<>();
    private ArrayList<String> aiColours;
    private int aiCount;
    private int simTime;

    private int schoolCrossingLocation = -25000;
    private boolean appliedBrakeAtSchoolCrossing = false;

    private Group roadGroup;
    private PerspectiveCamera camera;
    private boolean userSpeeding = false;
    private boolean eventNotRestarting = true;
    private boolean testHalt = false;
    private boolean brakePressed = false;

    private boolean eventRunning = true;

    private String simButtonLabel = "begin";

    private Random rand = new Random();

    private ArrayList<String> carColourList = new ArrayList<>();
    Group rootGroup = new Group();

    private EventHandler crashEvent;
    private EventHandler speedingEvent;
    private EventHandler givewayEvent;
    private EventHandler schoolCrossingEvent;

    private final int SPEED_LIMIT = 40;
    private boolean braking = false;
    private boolean accelerating = true;
    private boolean acceleratingBreak = false;
    private int minute;
    private int simulationTime;
    private ArrayList<Score> scoringOps = new ArrayList<>();

    private int randomiseCarSpeedCounter = 0;

    private String userChosenCarString;
    private ArrayList<String> events = new ArrayList<>();
    private String userView;

    /**
     * Starts the simulation and changes the button label
     *
     * @param event The mouse click event
     */
    @FXML
    private void handleSimBegin(ActionEvent event) {
        simulationTime = simTime;
        if (simButtonLabel.equals("begin")) {
            initialize();
            beginSimButton.setText("Cancel Simulation");
            simButtonLabel = "cancel";
            moveUserCar();
        } else if (simButtonLabel.equals("cancel")) {
            manageMessage("TEST CANCELLED");
            testHalt = true;
            displayResultsNotification("You have cancelled the test");

            // Disable buttons
            beginSimButton.setDisable(true);
        }
        // Start countdown
        simulationCountdown();

        // Begin random event manager
        assignRandomEventTime();
    }

    /**
     * Creates the required cars
     *
     * @param userChoice The car selected by the user
     */
    private void createCars(String userChoice) {
/*
        carColourList.add("mini-red.3DS");
        carColourList.add("mini-green.3DS");
        carColourList.add("mini-blue.3DS");
        carColourList.add("mini-aws.3DS");
*/
/*
        carColourList = aiColours;
        for (String c : carColourList) {
            if (c.equals(userChoice)) {
                carColourList.remove(c);
                break;
            }
        }
*/
        // Create user car and add to rootGroup
        userCar = new Car(40, 0, userChoice, true, SPEED_LIMIT);
        rootGroup.getChildren().add(userCar.getCarGroup());

        // Create ai cars
        ArrayList<Car> aiCarList = new ArrayList<>();
        // Create the other cars
        for (int i = 1; i < 4; i++) {
            Car newCar = new Car(40, -3700 * i, aiColours.get(rand.nextInt(aiColours.size())), false, SPEED_LIMIT);
            rootGroup.getChildren().add(newCar.getCarGroup());
            aiCarList.add(newCar);
        }
        // Assign cars as global
        aiCar1 = aiCarList.get(0);
        aiCar2 = aiCarList.get(1);

        // Create car coming opposite direction
        aiCar3 = aiCarList.get(2);
        aiCar3.setxPos(-40000);
        aiCar3.getCarGroup().setRotationAxis(Rotate.Y_AXIS);
        aiCar3.getCarGroup().setRotate(180.0);
        aiCar3.getCarGroup().setTranslateZ(550);

        for (int j = 1; j < aiCount + 1; j++) {
            Car c = new Car(40, aiCar3.getxPos() * j, aiColours.get(rand.nextInt(aiColours.size())), false, SPEED_LIMIT);
            rootGroup.getChildren().add(c.getCarGroup());

            c.getCarGroup().setRotationAxis(Rotate.Y_AXIS);
            c.getCarGroup().setRotate(180.0);
            c.getCarGroup().setTranslateZ(550);
            extraCarList.add(c);

        }

    }

    /**
     * Create the school zone signs
     */
    private void createSign() {
        int boxSize = 1624 * 20;
        int numberOfBoxes = 50;
        int signDistance = 0;

        // Load required material
        PhongMaterial schoolZone = new PhongMaterial();
        schoolZone.setDiffuseMap(new Image("schoolzone.png"));

        // Create signs
        for (int i = 0; i < numberOfBoxes; i++) {
            Box schoolZoneSign = new Box(300, 10, 300);
            schoolZoneSign.setMaterial(schoolZone);
            schoolZoneSign.setTranslateY(-400); // Fix road height
            schoolZoneSign.setTranslateZ(-700); // Centre the road to the car
            schoolZoneSign.setTranslateX(signDistance -= boxSize);
            schoolZoneSign.getTransforms().add(new Rotate(90, Rotate.Z_AXIS));
            schoolZoneSign.getTransforms().add(new Rotate(270, Rotate.Y_AXIS));
            roadGroup.getChildren().add(schoolZoneSign);

            // Create sign pole
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

    private void createSchool(int xCoord) {
        person = new SimObject("people/Boy N110512.3DS", xCoord, -40, -800);
        rootGroup.getChildren().add(person.getObjGroup());
        person.getObjGroup().getTransforms().add((new Rotate(90.0, Rotate.Y_AXIS)));
        person.getObjGroup().setScaleX(1.3);
        person.getObjGroup().setScaleY(1.3);
        person.getObjGroup().setScaleZ(1.3);


        person2 = new SimObject("people/Boy N311013.3DS", xCoord, -100, -1000);
        rootGroup.getChildren().add(person2.getObjGroup());
        person2.getObjGroup().getTransforms().add((new Rotate(90.0, Rotate.Y_AXIS)));
        person2.getObjGroup().setScaleX(60.0);
        person2.getObjGroup().setScaleY(60.0);
        person2.getObjGroup().setScaleZ(60.0);


        PhongMaterial crossingMat = new PhongMaterial();
        crossingMat.setDiffuseMap(new Image("crossing.png"));
        Box crossing = new Box(1624, 1, 1000);
        crossing.setMaterial(crossingMat);
        crossing.setTranslateY(90);
        crossing.setTranslateZ(360);
        crossing.setTranslateX(xCoord);
        crossing.getTransforms().add((new Rotate(90.0, Rotate.Y_AXIS)));
        rootGroup.getChildren().add(crossing);
    }

    private Group createObjects(int startOfBox, int boxLength) {
        Group objGroup = new Group();

        // Trees removed on request
        /*
         * SimObject tree = new SimObject("Tree 4.3ds", rand.nextInt((startOfBox +
		 * boxLength) - startOfBox + 1) + startOfBox, 0, rand.nextInt((startOfBox +
		 * boxLength) - startOfBox + 1) + startOfBox);
		 * 
		 * objGroup.getChildren().add(tree.getObjGroup());
		 */
        createSign();
        return objGroup;
    }

    /**
     * Calls the necessary methods to initialise the simulation scene
     */
    private void initialize() {
        // Create cars
        createCars(userChosenCarString);

        if (events.contains("schoolCrossingEvent")) {
            createSchool(schoolCrossingLocation);
        }
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

    /**
     * Create the clock displayed on the screen. Creates a thread to update it every
     * minute to a maximum of 5 minutes.
     */
    private void initClock() {
        // Dont have to worry about changing hour
        final String[] hourArr = {"8", "9", "2", "3"};
        String hour = hourArr[rand.nextInt(hourArr.length)];

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

            }

            ;

        }).start();
    }

    /**
     * Starts a thread to display the remaining time of the simulation
     */
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
                                displayResultsNotification("Congratulations!, you have passed the test");
                            }
                        }
                    });
                }

            }

            ;

        }).start();
    }

    /**
     * Display notification when simulation ends or is failed
     *
     * @param result The result of the notification appearing
     */
    private void displayResultsNotification(String result) {
        pane.setStyle("-fx-background-color: #F4F4F4;");
        displayScore.setStyle("-fx-background-color:  #34495e;");
        heading.setText(result);
        displayScore.setText("Display Scores");
    }

    /**
     * Creates the road objects
     * <p>
     * // TODO Only draw required road to prevent lagging on low spec machines
     *
     * @return The group of road objects
     */
    private Group createRoad() {
        roadGroup = new Group();
        int roadDistance = 1624;
        int boxSize = 1624;
        int numberOfBoxes = 85;

        // Read in required material
        PhongMaterial roadSurface = new PhongMaterial();
        roadSurface.setDiffuseMap(new Image("asphalt.jpg"));
        roadSurface.setSpecularColor(Color.WHITE);

        // Create road boxes
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

        // Event disabled on request
        /*
         * if (events.contains("givewayEvent")) { // TODO Create three give way events
		 * by default
		 * 
		 * givewayEvent = new EventHandler(); int loc1 = -(rand.nextInt(100000)); int
		 * loc2 = -(rand.nextInt(100000) + 100000); int loc3 = -(rand.nextInt(100000) +
		 * 200000);
		 * 
		 * System.out.println("locs:" + loc1 + " " + loc2 + " " + loc3);
		 * 
		 * givewayEvent.addGivewayLocation(loc1); givewayEvent.addGivewayLocation(loc2);
		 * givewayEvent.addGivewayLocation(loc3);
		 * 
		 * roadGroup.getChildren().add(createGivewayEvent(loc1));
		 * roadGroup.getChildren().add(createGivewayEvent(loc2));
		 * roadGroup.getChildren().add(createGivewayEvent(loc3)); }
		 */

        return roadGroup;
    }

    /**
     * Used to randomise the car speed
     *
     * @param car The car to update its speed
     * @return The random speed to assign to the car
     */
    private int randomiseCarSpeed(Car car) {
        final int minCarSpeed = car.getCarSpeedLimit() - 3;
        final int maxCarSpeed = car.getCarSpeedLimit();

        int randomSpeed = rand.nextInt(maxCarSpeed - minCarSpeed) + minCarSpeed;
        return randomSpeed;
    }

    /**
     * The moveUserCar creates the main thread to move the cars
     */
    private void moveUserCar() {

        crashEvent = new EventHandler();
        speedingEvent = new EventHandler();
        schoolCrossingEvent = new EventHandler();

        // Event removed on request
        // givewayEvent = new EventHandler();

        new Thread(new Runnable() {
            @Override
            public void run() {

                // While test is running
                while (!testHalt) {
                    try {
                        // Update every 30ms (33.3fps)
                        Thread.sleep(30);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    // Used to create a thread that updates the UI
                    Platform.runLater(new Runnable() {

                        @Override
                        public void run() {
                            boolean breakingBreak = false;
                            int acceleratingBreakCounter = 0;

                            // If braking event occuring slow the car down quickly
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

                                // If accelerating event occurring speed the car down slowly
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

                            // Continue randomising the speeds of the cars to provide more realism
                            randomiseCarSpeedCounter++;

                            // To slow down the updating to 300ms
                            if (randomiseCarSpeedCounter % 10 == 0) {
                                userCar.setSpeed(randomiseCarSpeed(userCar));
                                aiCar1.setSpeed(randomiseCarSpeed(aiCar1));
                                aiCar2.setSpeed(randomiseCarSpeed(aiCar2));
                                aiCar3.setSpeed(randomiseCarSpeed(aiCar3));


                                for (Car c : extraCarList) {
                                    c.setSpeed(randomiseCarSpeed(c));
                                }
                            }

                            // Move userCar
                            userCar.setxPos(userCar.getxPos() - userCar.getSpeed());
                            speedLabel.setText(String.valueOf((userCar.getSpeed()) + "km/h"));
                            userCar.getCarGroup().setTranslateX(userCar.getxPos());
                            // System.out.println(userCar.getxPos());

                            // Move camera with userCar
                            camera.setTranslateX(camera.getTranslateX() - userCar.getSpeed());
                            // System.out.println("trans cam to " + transCam);

                            // Move aiCar1
                            aiCar1.setxPos(aiCar1.getxPos() - (aiCar1.getSpeed()));
                            aiCar1.getCarGroup().setTranslateX(aiCar1.getxPos());
                            // System.out.println("trans ai1 car to " + aiCar1.getxPos());

                            // Move aiCar2
                            aiCar2.setxPos(aiCar2.getxPos() - (aiCar2.getSpeed()));
                            aiCar2.getCarGroup().setTranslateX(aiCar2.getxPos());
                            // System.out.println("trans ai2 car to " + aiCar2.getxPos());

                            // Move aiCar3
                            aiCar3.setxPos(aiCar3.getxPos() + SPEED_LIMIT);
                            aiCar3.getCarGroup().setTranslateX(aiCar3.getxPos());
                            // System.out.println("trans ai3 car to " + aiCar3.getxPos());

                            for (Car c : extraCarList) {
                                c.setxPos(c.getxPos() + SPEED_LIMIT);
                                c.getCarGroup().setTranslateX(c.getxPos());
                            }

                            if (events.contains("schoolCrossingEvent")) {
                                if (userCar.getxPos() < schoolCrossingLocation + 5000) {
                                    person.setzPos(person.getzPos() + personIncrement);
                                    person.getObjGroup().setTranslateZ(person.getzPos());

                                    person2.setzPos(person2.getzPos() + personIncrement);
                                    person2.getObjGroup().setTranslateZ(person2.getzPos());
                                }
                            }

                            // check if should apply brake now

                            if (userCar.getxPos() % 2 == 0) {
                                checkBrakeRequired();
                            }
                        }


                    });
                }
            }
        }).start();
    }

    /**
     * Creates a message to display to the user
     *
     * @param message The message to display
     */
    private void manageMessage(String message) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Platform.runLater(new Runnable() {

                    @Override
                    public void run() {
                        failureScreen.setText(message);
                    }
                });

                try {
                    // Display for 3.2 seconds
                    Thread.sleep(3200);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                Platform.runLater(new Runnable() {

                    @Override
                    public void run() {
                        failureScreen.setText("");
                    }
                });
            }
        }).start();
    }

    /**
     * Creates a message to display to the user
     *
     * @param message The message to display
     */
    private void manageMessage(String message, Color color) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Platform.runLater(new Runnable() {

                    @Override
                    public void run() {
                        failureScreen.setText(message);
                        failureScreen.setTextFill(color);
                    }
                });

                try {
                    // Display for 3.2 seconds
                    Thread.sleep(3200);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                Platform.runLater(new Runnable() {

                    @Override
                    public void run() {
                        failureScreen.setText("");
                    }
                });
            }
        }).start();
    }

    /**
     * Used to check if braking is required
     */
    private void checkBrakeRequired() {

        if (!brakeButton.isDisabled()) {
            /*
             * Crash event
			 */
            //if (events.contains("crashEvent")) {
            if ((userCar.getxPos() < aiCar1.getxPos() + 3000 || userCar.getxPos() < aiCar2.getxPos() + 3000)
                    && !crashEvent.getTimerStarted() && failureScreen.getText() == "") {
                System.out.println("---------Crash event timer started---------");
                crashEvent.startEventTimer();
                manageMessage("Apply the brakes! You are close to the car in front!", Color.HOTPINK);

            } else if (userCar.getxPos() < aiCar1.getxPos() + 480 // FAILURE
                    || userCar.getxPos() < aiCar2.getxPos() + 480) {

                if (!crashEvent.isTimerStopped()) {
                    crashEvent.stopEventTimer();
                    System.out.println("---------Crash event timer stopped---------");
                    crashEvent.setTimerStopped(true);
                    manageMessage("You crashed!");
                    displayResultsNotification("You have failed the test...");
                    testHalt = true;
                }
            }

            // if school crossing event
            if (events.contains("schoolCrossingEvent")) {
                if ((userCar.getxPos() < schoolCrossingLocation + 5000) && (!schoolCrossingEvent.getTimerStarted()) && (failureScreen.getText() == "") && (userCar.getxPos() > schoolCrossingLocation) && !appliedBrakeAtSchoolCrossing) {
                    System.out.println("---------School crossing event timer started---------");
                    schoolCrossingEvent.startEventTimer();
                    manageMessage("Apply the brakes! You are close to the crossing!", Color.GREEN);

                    // Failure
                } else if ((userCar.getxPos() < schoolCrossingLocation + 100) && !appliedBrakeAtSchoolCrossing) {

                    if (!schoolCrossingEvent.isTimerStopped()) {
                        schoolCrossingEvent.stopEventTimer();
                        System.out.println("---------School crossing event timer stopped---------");
                        schoolCrossingEvent.setTimerStopped(true);
                        manageMessage("You almost hit the people!");
                        displayResultsNotification("You have failed the test...");
                        testHalt = true;
                    }
                }
            }

            //}

			/*
             * Speeding event
			 */
            if (events.contains("speedingEvent")) {
                if (!userSpeeding) {
                    if (userCar.getSpeed() > SPEED_LIMIT && failureScreen.getText() == "") {
                        userSpeeding = true;
                        System.out.println("---------Speeding event timer started---------");
                        speedingEvent.startEventTimer();
                        manageMessage("Apply the brakes! You are speeding!", Color.HOTPINK);
                    }
                }
                if (userCar.getSpeed() >= SPEED_LIMIT + 20) {
                    if (!speedingEvent.isTimerStopped()) {
                        speedingEvent.stopEventTimer();
                        System.out.println("---------Speeding event timer stopped---------");
                        speedingEvent.setTimerStopped(true);
                        manageMessage("You went way over the speed limit!");
                        displayResultsNotification("You have failed the test...");
                        testHalt = true;
                    }
                }

            }

			/*
             * Giveway event
			 */
            if (events.contains("givewayEvent")) {
                if ((userCar.getxPos() < givewayEvent.getClosestGivewayLoc() + 10000)
                        && !givewayEvent.getTimerStarted()) {
                    System.out.println("Giveway timer started");
                    System.out.println("---------Giveway event timer started---------");
                    givewayEvent.startEventTimer();

                    //// System.out.print("SIMULATION ENDED");
                } else if (userCar.getxPos() < givewayEvent.getClosestGivewayLoc()) {

                    if (!givewayEvent.isTimerStopped()) {
                        givewayEvent.stopEventTimer();
                        givewayEvent.setTimerStopped(true);
                        //// System.out.print("SIMULATION ENDED");
                        manageMessage("You failed to give way!");
                        displayResultsNotification("You have failed the test...");
                        testHalt = true;
                    }
                }
            }
        }

    }

    /**
     * Used to create the camera
     *
     * @param camPlacement The location where to position the camera
     * @return The created camera
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
            camera.setTranslateX(600); // 600
            camera.setTranslateY(-720); // -720
            camera.setTranslateZ(-520); // -520
            camera.getTransforms().add((new Rotate(-17.0, Rotate.Z_AXIS))); // -17
            camera.getTransforms().add((new Rotate(-90.0, Rotate.Y_AXIS))); // -90

        } else {
            // TODO throws
            System.out.println("No camera created");
            return null;
        }
        return camera;
    }

    /**
     * If the brake button is pressed
     */
    @FXML
    private void brakeButtonPressed() {

        braking = true;
        eventRunning = false;

        tempDisableBrakeButton();

        // Check if events have started or exist
        if ((crashEvent == null || !crashEvent.getTimerStarted())
                && (speedingEvent == null || !speedingEvent.getTimerStarted())
                && (givewayEvent == null || !givewayEvent.getTimerStarted())
                && (schoolCrossingEvent == null || !schoolCrossingEvent.getTimerStarted())) {

            manageMessage("Brakes applied incorrectly. Score: -300");
            Score score = new Score("failedAttempt", -300);
            scoringOps.add(score);
        }

        // If crash event occuring
        if (events.contains("crashEvent")) {

            if (crashEvent.getTimerStarted()) {

                crashEvent.stopEventTimer();
                System.out.println("---------Crash event timer stopped---------");
                crashEvent.setTimerStopped(true);

                Score score = new Score("crashevent", crashEvent.getTimerStartedTime(),
                        crashEvent.getTimerStoppedTime());
                scoringOps.add(score);

                if (score.getScore() != 0) {
                    manageMessage("Brakes applied correctly.  Score: " + score.getScore());
                } else {
                    manageMessage("Brakes applied correctly but too slow to react.");
                }

                crashEvent = new EventHandler();

            }
        }

        // If schoolCrossingEvent occuring
        if (events.contains("schoolCrossingEvent")) {

            if (schoolCrossingEvent.getTimerStarted()) {
                tempDisableBrakeButton();
                schoolCrossingEvent.stopEventTimer();
                System.out.println("---------School crossing event timer stopped---------");
                appliedBrakeAtSchoolCrossing = true;
                schoolCrossingEvent.setTimerStopped(true);

                Score score = new Score("schoolCrossingEvent", schoolCrossingEvent.getTimerStartedTime(),
                        schoolCrossingEvent.getTimerStoppedTime());
                scoringOps.add(score);

                if (score.getScore() != 0) {
                    manageMessage("Brakes applied correctly.  Score: " + score.getScore());
                } else {
                    manageMessage("Brakes applied correctly but too slow to react.");
                }

                schoolCrossingEvent = new EventHandler();

            }
        }

        // If speeding event occuring
        if (events.contains("speedingEvent")) {

            if (speedingEvent.getTimerStarted()) {

                speedingEvent.stopEventTimer();
                System.out.println("---------Speeding event timer stopped---------");
                speedingEvent.setTimerStopped(true);

                Score score = new Score("speedingEvent", speedingEvent.getTimerStartedTime(),
                        speedingEvent.getTimerStoppedTime());
                scoringOps.add(score);

                if (score.getScore() != 0) {
                    manageMessage("Brakes applied correctly.  Score: " + score.getScore());
                } else {
                    manageMessage("Brakes applied correctly but too slow to react.");
                }

                speedingEvent = new EventHandler();

            }
        }
        endOldEvent();

        // Event removed on request
        /*
         * // if giveway event occuring if (events.contains("givewayEvent")) { if
		 * (givewayEvent.getTimerStarted()) { givewayEvent.stopEventTimer();
		 * System.out.println("---------Giveway event timer stopped---------");
		 * givewayEvent.setTimerStopped(true);
		 * 
		 * Score score = new Score("givewayEvent", givewayEvent.getTimerStartedTime(),
		 * givewayEvent.getTimerStoppedTime()); scoringOps.add(score);
		 * 
		 * if (score.getScore() != 0) {
		 * manageMessage("Brakes applied correctly.  Score: " + score.getScore()); }
		 * else { manageMessage("Brakes applied correctly but too slow to react."); }
		 * 
		 * if (givewayEvent.getClosestGivewayLoc() ==
		 * givewayEvent.getGivewayLocations().get(0)) {
		 * givewayEvent.getGivewayLocations().remove(0); }
		 * 
		 * } }
		 */

    }

    /**
     * Disables the brake button for a period of time
     */
    private void tempDisableBrakeButton() {

        final Thread t = new Thread(new Runnable() {

            @Override
            public void run() {
                brakeButton.setDisable(true);

                try {
                    // Disable break button for 3 seconds
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

    /**
     * Disables the brake button for a period of time
     */
    private void tempDisableBrakeButtonFail() {

        final Thread t = new Thread(new Runnable() {

            @Override
            public void run() {
                brakeButton.setDisable(true);

                try {
                    // Disable break button for 3 seconds
                    Thread.sleep(1000);
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
        // Wait minimum of 5 seconds between events starting and max of 15
        int eventBreak = rand.nextInt(10000) + 5000;

        final Thread t = new Thread(new Runnable() {

            @Override
            public void run() {

                while (!testHalt) {


                    try {
                        if (events.contains("schoolCrossingEvent")) {
                            if (!appliedBrakeAtSchoolCrossing) {
                                System.out.println("Sleeping before crossing");
                                Thread.sleep(18000);
                            }
                        }

                        Thread.sleep(eventBreak);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }


                    System.out.println("Choosing event");

                    int eventType = -1;

                    // Choose event based on random number
                    if (events.size() != 0) {
                        eventType = rand.nextInt((events.size()));
                    }

                    if (eventRunning) {
                        System.out.println("event is running");
                        if (eventType != -1) {
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
                            }

                            // Event removed on request
                    /*
                     * else if (events.get(eventType).equals("givewayEvent")) {
					 * System.out.println("givewayEvent started"); EventHandler givewayEvent = new
					 * EventHandler(); int posOfGiveway = givewayEvent.startGivewayEvent(userCar);
					 * // givewayGroup = createGivewayEvent(posOfGiveway); }
					 */
                        }
                    }

                }
            }
        });

        t.setDaemon(true);
        t.start();
    }

/*
    /**
	 * Creates a giveway event, draws a giveway sign and horizontal road. User must
	 * apply brakes before the sign
	 * 
	 * @param xPos The position of the giveway location
	 * @return The group creating the givewayevent
	 *
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
	*/

    /**
     * Resets everything back to initial values ready for a new event
     */

    private void endOldEvent() {

        final Thread t = new Thread(new Runnable() {

            @Override
            public void run() {
                System.out.println("Ending old event");
                eventNotRestarting = false;
                eventRunning = false;
                userSpeeding = false;

                speedingEvent = new EventHandler();
                crashEvent = new EventHandler();
                givewayEvent = new EventHandler();
                schoolCrossingEvent = new EventHandler();

                System.out.println("Speeding up cars");
                // Move cars away from user (Crash event)

                aiCar1.setCarSpeedLimit((int) (SPEED_LIMIT * 1.2));
                aiCar2.setCarSpeedLimit((int) (SPEED_LIMIT * 1.2));

                try {
                    System.out.println("sleeping for 1");
                    // Sleep for 2 second
                    Thread.sleep(2000);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                System.out.println("restoring cars speed");
                // Restore cars (Crash event)
                aiCar1.setSpeed(SPEED_LIMIT);
                aiCar2.setSpeed(SPEED_LIMIT);
                userCar.setSpeed(SPEED_LIMIT);
                eventRunning = true;
                //assignRandomEventTime();
            }

        });
        t.setDaemon(true);
        t.start();
    }

    /**
     * Displays the scores
     *
     * @param event The mouse click actionevent
     * @throws IOException If fxml not found
     */
    @FXML
    private void displayScores(ActionEvent event) throws IOException {

        eventRunning = false;

        FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/Results.fxml"));
        Parent root = (Parent) loader.load();

        ResultsController resultsControl = loader.<ResultsController>getController();
        resultsControl.setScoringOps(scoringOps);

        Parent p = loader.getRoot();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(p));
        stage.show();
    }

    /**
     * Used to set chosen car from chooseCarController
     *
     * @param userChosenCarString The chosen car
     */
    public void setUserChosenCarString(String userChosenCarString) {
        this.userChosenCarString = userChosenCarString;
    }

    /**
     * Used to set chosen events from chooseCarController
     *
     * @param events The chosen events
     */
    public void setEvents(ArrayList<String> events) {
        this.events = events;
    }

    /**
     * Used to set chosen view from chooseCarController
     *
     * @param userView The chosen view
     */
    public void setUserView(String userView) {
        this.userView = userView;
    }

    public void setAiColours(ArrayList<String> aiColours) {
        this.aiColours = aiColours;
    }

    public void setAiCount(int aiCount) {
        this.aiCount = aiCount;
    }

    public void setSimTime(int simTime) {
        this.simTime = simTime;
    }
}

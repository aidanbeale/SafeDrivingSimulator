 *
 *   Copyright 2017 John Humphrys
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package simulation;

import com.interactivemesh.jfx.importer.ImportException;
import com.interactivemesh.jfx.importer.tds.TdsModelImporter;

import javafx.scene.Group;
import javafx.scene.Node;

/**
 * The Car model class holds all information about a car
 * 
 * @author John Humphrys
 *
 */
public class Car {
	private int speed;
	private int xPos;
	private String model;
	private Group carGroup;
	private boolean userControlling;
	private int carSpeedLimit;

	/**
	 * Initalise Car with a number of variables
	 * 
	 * @param speed
	 *            The speed the car is travelling at
	 * @param xPos
	 *            The xPos of the car in the 3D world
	 * @param model
	 *            The type of model the Car is using
	 * @param userControlling
	 *            If the user is controlling the car or not
	 * @param carSpeedLimit
	 *            The speed limit the car is set to
	 */
	public Car(int speed, int xPos, String model, boolean userControlling, int carSpeedLimit) {
		this.speed = speed;
		this.xPos = xPos;
		this.model = model;
		this.userControlling = userControlling;
		this.carSpeedLimit = carSpeedLimit;

		init();
	}

	/**
	 * Init the Car for use in chooseCar controller
	 * 
	 * @param model
	 *            The model to load
	 */
	public Car(String model) {
		this.model = model;
		init();
	}

	/**
	 * init imports the 3d model from file
	 */
	private void init() {
		Node[] carMesh = import3dModel(model);
		carGroup = meshIntoGroup(carMesh);
	}

	/**
	 * This method will import the carmodel requested
	 * @see http://www.interactivemesh.org/models/jfx3dimporter.html
	 * 
	 * @param carName
	 *            The file name of the car
	 * @return A group containing the car mesh
	 */
	private Node[] import3dModel(String carName) {

		// Create model importer
		TdsModelImporter modelImporter = new TdsModelImporter();

		try {
			// Read car model from path
			String path = "src\\simModels\\" + carName;
			modelImporter.read(path);
		} catch (ImportException e) {
			System.out.println("Error importing 3ds model: " + e.getMessage());
			return null;
		}

		// Get car mesh
		Node[] carMesh = modelImporter.getImport();
		modelImporter.close();

		return carMesh;
	}

	/**
	 * Adds the car mesh into a group
	 * 
	 * @param carMesh
	 *            The mesh to be added to the group
	 * @return The created group
	 */
	private Group meshIntoGroup(Node[] carMesh) {

		// Add car mesh to group
		Group model3D = new Group(carMesh);

		// Set the basic layout of the car
		model3D.setLayoutX(100);
		model3D.setLayoutY(100);

		return model3D;
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public int getxPos() {
		return xPos;
	}

	public void setxPos(int xPos) {
		this.xPos = xPos;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public Group getCarGroup() {
		return carGroup;
	}

	public void setCarGroup(Group carGroup) {
		this.carGroup = carGroup;
	}

	public int getCarSpeedLimit() {
		return carSpeedLimit;
	}

	public void setCarSpeedLimit(int carSpeedLimit) {
		this.carSpeedLimit = carSpeedLimit;
	}

}

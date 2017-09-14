package simulation;

import com.interactivemesh.jfx.importer.ImportException;
import com.interactivemesh.jfx.importer.tds.TdsModelImporter;

import javafx.scene.Group;
import javafx.scene.Node;

public class Car {
	private int speed;
	private int xPos;
	private String model;
	private Group carGroup;
	private boolean userControlling;

	public Car(int speed, int xPos, String model, boolean userControlling) {
		super();
		this.speed = speed;
		this.xPos = xPos;
		this.model = model;
		this.userControlling = userControlling;

		init();
	}

	private void init() {
		Node[] carMesh = import3dModel(model);
		carGroup = meshIntoGroup(carMesh);
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
			String path = "src\\carModels\\" + carName + ".3DS";
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
}

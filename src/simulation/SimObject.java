package simulation;

import com.interactivemesh.jfx.importer.ImportException;
import com.interactivemesh.jfx.importer.tds.TdsModelImporter;

import javafx.scene.Group;
import javafx.scene.Node;

public class SimObject {

	String model;
	Group objGroup;
	int xPos;
	int yPos;
	int zPos;

	public SimObject(String model, int xPos, int yPos, int zPos) {
		this.model = model;
		this.xPos = xPos;
		this.yPos = yPos;
		this.zPos = zPos;

		init();
	}

	private void init() {
		Node[] objMesh = import3dModel(model);
		objGroup = meshIntoGroup(objMesh);
	}

	/**
	 * This method will import the carmodel requested
	 * 
	 * @param carName
	 *            The file name of the car
	 * @return A group containing the car mesh
	 */
	private Node[] import3dModel(String objName) {

		// Create model importer
		// @see http://www.interactivemesh.org/models/jfx3dimporter.html
		TdsModelImporter modelImporter = new TdsModelImporter();

		try {
			// Read car model from path
			String path = "src\\simModels\\" + objName + ".3DS";
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

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public int getxPos() {
		return xPos;
	}

	public void setxPos(int xPos) {
		this.xPos = xPos;
	}

	public int getyPos() {
		return yPos;
	}

	public void setyPos(int yPos) {
		this.yPos = yPos;
	}

	public int getzPos() {
		return zPos;
	}

	public void setzPos(int zPos) {
		this.zPos = zPos;
	}

	public Group getObjGroup() {
		return objGroup;
	}

	public void setObjGroup(Group objGroup) {
		this.objGroup = objGroup;
	}

}

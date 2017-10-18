/**
 * Copyright 2017 John Humphrys, Aidan Beale
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

package simulation;

import com.interactivemesh.jfx.importer.ImportException;
import com.interactivemesh.jfx.importer.tds.TdsModelImporter;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.transform.Rotate;

/**
 * Class used to create 3d simulation objects
 *
 * @author John Humphrys
 *
 */
public class SimObject {

    String model;
    Group objGroup;
    int xPos;
    int yPos;
    int zPos;

    /**
     * Standard constructor
     *
     * @param model
     *            The model to use
     * @param xPos
     *            The X position
     * @param yPos
     *            The Y position
     * @param zPos
     *            The Z position
     */
    public SimObject(String model, int xPos, int yPos, int zPos) {
        this.model = model;
        this.xPos = xPos;
        this.yPos = yPos;
        this.zPos = zPos;

        init();
    }

    /**
     * Used to load the model then translate it to the required location
     */
    private void init() {
        Node[] objMesh = import3dModel(model);
        objGroup = meshIntoGroup(objMesh);
        objGroup.setTranslateX(xPos);
        objGroup.setTranslateY(yPos);
        objGroup.setTranslateZ(zPos);

        // Set rotate by default
        objGroup.setRotationAxis(Rotate.Y_AXIS);
        objGroup.setRotate(90.0);
    }

    /**
     * This method will import the object requested
     *
     * @param objName
     *            The file name of the object
     * @return A group containing the object mesh
     */
    private Node[] import3dModel(String objName) {

        // Create model importer
        // @see http://www.interactivemesh.org/models/jfx3dimporter.html
        TdsModelImporter modelImporter = new TdsModelImporter();

        try {
            // Read car model from path
            String path = "src\\simModels\\" + objName;
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

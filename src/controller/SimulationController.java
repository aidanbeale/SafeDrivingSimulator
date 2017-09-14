package controller;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.Map;

import com.interactivemesh.jfx.importer.ImportException;
import com.interactivemesh.jfx.importer.ModelImporter;
import com.interactivemesh.jfx.importer.obj.ObjModelImporter;
import com.interactivemesh.jfx.importer.tds.TdsModelImporter;
import com.jfoenix.controls.JFXButton;

import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Camera;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.PerspectiveCamera;
import javafx.scene.PointLight;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.control.CheckBox;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.CullFace;
import javafx.scene.shape.DrawMode;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.Sphere;
import javafx.scene.shape.TriangleMesh;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.control.CheckBox;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;

public class SimulationController {

	@FXML private JFXButton beginSimButton;
	@FXML private AnchorPane simAnchor;
	@FXML private Group simGroup;
	
	private Node[] carMesh;
	private Group carMeshGroup;
	private int trans = -1200;
	private PerspectiveCamera camera;

    @FXML
    private void handleSimBegin(ActionEvent event) {
    	moveCar();
    }
    
    /**
     * Calls the nessasary methods to initialise the simulation scene
     */
    @FXML
    private void initialize() {
    	
    	// Import car and add to subscene
    	Node[] carMesh = import3dModel("mini-greenNo");
    	carMeshGroup = meshIntoGroup(carMesh);
    	SubScene subScene = addMeshToSubScene(carMeshGroup);
    	simGroup.getChildren().add(subScene);
    }
    
    private void moveCar() {
    	new Thread(new Runnable() {
    	    @Override public void run() {
    	    while(true) {
    	    	try {
					Thread.sleep(2);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    	        Platform.runLater(new Runnable() {
    	            @Override public void run() {
    	            	//carMeshGroup.setTranslateX(trans--);
    	            	//System.out.println("trans car to " + trans);
    	            	
    	            	camera.setTranslateX(trans++);
    	            	System.out.println("trans cam to " + trans);
    	            }
    	        });
    	    }
    	    }
    	}).start();
    }
    
    /**
     * Method adds a car mesh to a subscene
     * 
     * @param carMesh The carmesh to add to the subscene
     * @return The subscene
     */
    private SubScene addMeshToSubScene(Group carMesh) { // TODO need to be able to add multiple car meshes to the scene
        
    	// Create view camera
    	camera = new PerspectiveCamera();
        
    	// First person view
    	camera.setTranslateX(-1200);
    	camera.setTranslateY(-414);
    	camera.setTranslateZ(-420);
    	camera.setRotationAxis(Rotate.Y_AXIS);
    	camera.setRotate(270.0);
        
        

        // Create sub scene
        SubScene subScene = new SubScene(carMesh, 740, 740, true, SceneAntialiasing.BALANCED);
        
        // Colour in the background
        subScene.setFill(Color.GREEN);
        subScene.setCamera(camera);
 
        return subScene;
    }
    
    /**
     * This method will import the carmodel requested
     * 
     * @param carName The file name of the car
     * @return A group containing the car mesh
     */
    private Node[] import3dModel(String carName) {
    	
    	// Create model importer
    	// @see http://www.interactivemesh.org/models/jfx3dimporter.html
    	TdsModelImporter modelImporter = new TdsModelImporter();
    	
        try {
        	// Read car model from path
            String path = "C:\\Users\\John\\Documents\\UniEclipseplswork\\SafeDrivingSimulator\\src\\carModels\\" + carName + ".3DS";
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
}

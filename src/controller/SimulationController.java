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

    @FXML
    private void handleSimBegin(ActionEvent event) {
    }
    
    @FXML
    private void initialize() {
    	
    	Group carMesh = import3dModel("mini");
    	SubScene subScene = addMeshToSubScene(carMesh);
    	simGroup.getChildren().add(subScene);
    }
    
    private SubScene addMeshToSubScene(Group carMesh) {
        
    	// Create view camera
    	PerspectiveCamera camera = new PerspectiveCamera();
        camera.setTranslateZ(-10000);

        // Create sub scene
        SubScene subScene = new SubScene(carMesh, 740, 740, true, SceneAntialiasing.BALANCED);
        
        // Colour in the background
        subScene.setFill(Color.GREEN);
        subScene.setCamera(camera);
 
        return subScene;
    }
    
    private Group import3dModel(String carName) {
    	
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
        
        // Add car mesh to group
        Group model3D = new Group(carMesh);
        
        // Set the basic layout of the car
        model3D.setLayoutX(100);
        model3D.setLayoutY(100);
        
        return model3D;
    }
}

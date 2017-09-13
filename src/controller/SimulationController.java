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
 
        PerspectiveCamera camera = new PerspectiveCamera();
        camera.setTranslateZ(-10000);
 
        //
        // importing 3ds Modell
        //
        TdsModelImporter myModel = new TdsModelImporter();
        try {
            String path = "C:\\Users\\John\\Documents\\UniEclipseplswork\\SafeDrivingSimulator\\src\\controller\\golf5.3DS";
            myModel.read(path);
        } catch (ImportException e) {
            System.out.println("Error importing 3ds model: " + e.getMessage());
            return;
        }
 
        //
        // adding Model
        //
        final Node[] myMesh = myModel.getImport();
        myModel.close();
        final Group model3D = new Group(myMesh);
        model3D.setLayoutX(600);
        model3D.setLayoutY(600);
 
        //
        // setting Anchorpane and scene and start
        //
        
        
 
        SubScene subScene = new SubScene(model3D, 300, 300, true, SceneAntialiasing.BALANCED);
        subScene.setCamera(camera);
 
        simGroup.getChildren().add(subScene);
    }
    
    @FXML
    private void initialize() {
    	
        PerspectiveCamera camera = new PerspectiveCamera();
        camera.setTranslateZ(-1000);

        TdsModelImporter myModel = new TdsModelImporter();
        try {
            String path = "C:\\Users\\John\\Documents\\UniEclipseplswork\\SafeDrivingSimulator\\src\\controller\\hst.3ds";
            myModel.read(path);
        } catch (ImportException e) {
            System.out.println("Error importing 3ds model: " + e.getMessage());
            return;
        }
 
        Node[] myMesh = myModel.getImport();
        myModel.close();
        
        Group model3D = new Group(myMesh);
        
        model3D.setLayoutX(600);
        model3D.setLayoutY(600);  
 
        //
        SubScene subScene = new SubScene(model3D, 740, 740, true, SceneAntialiasing.BALANCED);
        subScene.setFill(Color.GREEN);
        subScene.setCamera(camera);
 
        simGroup.getChildren().add(subScene);
    }

}

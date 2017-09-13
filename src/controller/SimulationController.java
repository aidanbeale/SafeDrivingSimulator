package controller;

import com.jfoenix.controls.JFXButton;

import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.SubScene;
import javafx.scene.control.CheckBox;
import javafx.scene.image.Image;
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
import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;

public class SimulationController {

	private static final int VIEWPORT_SIZE = 800;

	private static final double MODEL_SCALE_FACTOR = 40;
	private static final double MODEL_X_OFFSET = 0;
	private static final double MODEL_Y_OFFSET = 0;
	private static final double MODEL_Z_OFFSET = VIEWPORT_SIZE / 2;

	// private static final String textureLoc =
	// "http://icons.iconarchive.com/icons/aha-soft/desktop-halloween/256/Skull-and-bones-icon.png";
	// // icon license linkware, backlink to http://www.aha-soft.com
	private static final String textureLoc = "https://i.ytimg.com/vi/57hP8tfRkgs/hqdefault.jpg";
	// texture sourced from:
	// http://www.creattor.com/textures-patterns/seamless-marble-textures-pack-2688
	// texture license: http://creativecommons.org/licenses/by-nc/3.0/ => Creative
	// Commons Attribution-NonCommercial 3.0 Unported

	private Image texture;
	private PhongMaterial texturedMaterial = new PhongMaterial();

	private MeshView meshView = loadMeshView();

	@FXML
	private JFXButton beginSimButton;
	@FXML
	private SubScene simSubScene;

	private MeshView loadMeshView() {
		float[] points = { -5, 5, 0, -5, -5, 0, 5, 5, 0, 5, -5, 0 };
		float[] texCoords = { 1, 1, 1, 0, 0, 1, 0, 0 };
		int[] faces = { 2, 2, 1, 1, 0, 0, 2, 2, 3, 3, 1, 1 };

		TriangleMesh mesh = new TriangleMesh();
		mesh.getPoints().setAll(points);
		mesh.getTexCoords().setAll(texCoords);
		mesh.getFaces().setAll(faces);

		return new MeshView(mesh);
	}

	private Group buildScene() {
		meshView.setTranslateX(VIEWPORT_SIZE / 2 + MODEL_X_OFFSET);
		meshView.setTranslateY(VIEWPORT_SIZE / 2 * 9.0 / 16 + MODEL_Y_OFFSET);
		meshView.setTranslateZ(VIEWPORT_SIZE / 2 + MODEL_Z_OFFSET);
		meshView.setScaleX(MODEL_SCALE_FACTOR);
		meshView.setScaleY(MODEL_SCALE_FACTOR);
		meshView.setScaleZ(MODEL_SCALE_FACTOR);

		return new Group(meshView);
	}

	@FXML
	private void handleSimBegin(ActionEvent event) {
		
	    texture = new Image(textureLoc);
	    texturedMaterial.setDiffuseMap(texture);

	    Group group = buildScene();

	    RotateTransition rotate = rotate3dGroup(group);

	    VBox layout = new VBox(
	        createControls(rotate),
	        createScene3D(group)
	    );

	    Scene scene = new Scene(layout, Color.CORNSILK);
	    Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
	    
	    window.setScene(scene);
	    window.show();
		
		/*
		Group root = createContent(8, false);

		// Create camera
		PerspectiveCamera camera = new PerspectiveCamera(true);

		// and position it
		camera.getTransforms().addAll(new Rotate(-12, Rotate.Y_AXIS), new Rotate(-20, Rotate.X_AXIS),
				new Translate(0, 0, -15));

		// add camera as node to scene graph
		root.getChildren().add(camera);

		Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();

		// Setup a scene
		Scene scene = new Scene(root, 400, 400, false);
		scene.setCamera(camera);

		// Add the scene to the stage and show the stage
		window.setScene(scene);
		window.show();
		*/
	}

	private Group createContent(int res, boolean wirePlusSolid) {

		Group root = new Group();

		// Create a sphere
		Sphere sphere;

		if (wirePlusSolid) {
			sphere = new Sphere(2, res);
			sphere.setMaterial(new PhongMaterial(Color.RED));
			sphere.setDrawMode(DrawMode.FILL);
			root.getChildren().add(sphere);
		}

		sphere = new Sphere(2, res);
		sphere.setMaterial(new PhongMaterial(Color.WHITE));
		sphere.setDrawMode(DrawMode.LINE);

		// Add sphere to group
		root.getChildren().add(sphere);

		return root;
	}

	 private RotateTransition rotate3dGroup(Group group) {
		    RotateTransition rotate = new RotateTransition(Duration.seconds(10), group);
		    rotate.setAxis(Rotate.Y_AXIS);
		    rotate.setFromAngle(0);
		    rotate.setToAngle(360);
		    rotate.setInterpolator(Interpolator.LINEAR);
		    rotate.setCycleCount(RotateTransition.INDEFINITE);

		    return rotate;
		  }
	
	private SubScene createScene3D(Group group) {
		SubScene scene3d = new SubScene(group, VIEWPORT_SIZE, VIEWPORT_SIZE * 9.0 / 16);
		scene3d.setFill(Color.rgb(10, 10, 40));
		scene3d.setCamera(new PerspectiveCamera());
		return scene3d;
	}

	private VBox createControls(RotateTransition rotateTransition) {
		CheckBox cull = new CheckBox("Cull Back");
		meshView.cullFaceProperty()
				.bind(Bindings.when(cull.selectedProperty()).then(CullFace.BACK).otherwise(CullFace.NONE));
		CheckBox wireframe = new CheckBox("Wireframe");
		meshView.drawModeProperty()
				.bind(Bindings.when(wireframe.selectedProperty()).then(DrawMode.LINE).otherwise(DrawMode.FILL));

		CheckBox rotate = new CheckBox("Rotate");
		rotate.selectedProperty().addListener(observable -> {
			if (rotate.isSelected()) {
				rotateTransition.play();
			} else {
				rotateTransition.pause();
			}
		});

		CheckBox texture = new CheckBox("Texture");
		meshView.materialProperty()
				.bind(Bindings.when(texture.selectedProperty()).then(texturedMaterial).otherwise((PhongMaterial) null));

		VBox controls = new VBox(10, rotate, texture, cull, wireframe);
		controls.setPadding(new Insets(10));
		return controls;
	}

}

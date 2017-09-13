package controller;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args) {
    	launch();
    }

	@Override
	public void start(Stage primaryStage) throws Exception {
		
		// Display initial login screen
        primaryStage.setTitle("Safe Driving Simulator");
        Parent root = FXMLLoader.load(getClass().getResource("../view/Login.fxml"));
        
        Scene scene = new Scene(root);
        
        primaryStage.setScene(scene);
        primaryStage.show();
	}
}
package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

// @see http://code.makery.ch/library/javafx-8-tutorial/part1/

public class Login extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("Safe Driving Simulator");
        
        Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
        Scene scene = new Scene(root);
        
        LoginController controller = new LoginController();
        controller.initialise();
        
        stage.setScene(scene);
        stage.show();
    }

    public void run() {
        launch();
    }
}
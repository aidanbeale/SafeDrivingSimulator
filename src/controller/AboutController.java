package controller;

import java.io.IOException;

import com.jfoenix.controls.JFXTextArea;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AboutController {
	
	@FXML
	JFXTextArea mainText;
	private String about = "Created by John Humphrys in COS80007 - Advanced Java.\n Copyright (c) John Humphrys 2017\n";
	private String licence = "";
    public void initialize() {
    	mainText.setText(licence);
    }
	
	@FXML
	private void returnToLogin(ActionEvent event) throws IOException {

			Parent root = FXMLLoader.load(getClass().getResource("../view/Login.fxml"));
			Scene scene = new Scene(root);
			
			Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
			window.setScene(scene);
			window.show();
	}
}

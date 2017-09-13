package controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;

abstract class CycledView extends StackPane {
	
	private CycledView nextStage;
	
	/*
	CycledView(CycledView nextStage) {
		this.nextStage = nextStage;
	}
	*/
	
	public void setNextStage(CycledView nextStage) {
		this.nextStage = nextStage;
	}
	
	protected void startNextStage() {
		getScene().setRoot(nextStage);
	}
	
	private void updateGUI(String requiredFXML) {
		getScene().setRoot(value);
        Scene sceneLogin = new Scene(FXMLLoader.load(getClass().getResource(requiredFXML)));

	}

}
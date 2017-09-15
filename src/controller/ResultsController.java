package controller;

import java.util.ArrayList;

import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.util.Callback;
import simulation.Score;

public class ResultsController { // Referenced this https://www.youtube.com/watch?v=nbl0kOum-ps

	@FXML
	JFXTreeTableView<RowProp> resultsTable;

	@FXML
	private void initialize() {
		loadTable();
		
	}

	private void loadTable() {
		JFXTreeTableColumn<RowProp, String> eventCol = new JFXTreeTableColumn<>("Event");
		eventCol.setPrefWidth(187);
		eventCol.setCellValueFactory(
				new Callback<TreeTableColumn.CellDataFeatures<RowProp, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<RowProp, String> param) {
						return param.getValue().getValue().event;
					}
				});

		JFXTreeTableColumn<RowProp, String> optTimeCol = new JFXTreeTableColumn<>("Optimal Time");
		optTimeCol.setPrefWidth(250);
		optTimeCol.setCellValueFactory(
				new Callback<TreeTableColumn.CellDataFeatures<RowProp, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<RowProp, String> param) {
						return param.getValue().getValue().optTime;
					}
				});

		JFXTreeTableColumn<RowProp, String> yourTimeCol = new JFXTreeTableColumn<>("Your Time");
		yourTimeCol.setPrefWidth(250);
		yourTimeCol.setCellValueFactory(
				new Callback<TreeTableColumn.CellDataFeatures<RowProp, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<RowProp, String> param) {
						return param.getValue().getValue().yourTime;
					}
				});
		
		JFXTreeTableColumn<RowProp, String> diffCol = new JFXTreeTableColumn<>("Difference");
		diffCol.setPrefWidth(187);
		diffCol.setCellValueFactory(
				new Callback<TreeTableColumn.CellDataFeatures<RowProp, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<RowProp, String> param) {
						return param.getValue().getValue().difference;
					}
				});

		JFXTreeTableColumn<RowProp, String> scoreCol = new JFXTreeTableColumn<>("Raw Score");
		scoreCol.setPrefWidth(187);
		scoreCol.setCellValueFactory(
				new Callback<TreeTableColumn.CellDataFeatures<RowProp, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<RowProp, String> param) {
						return param.getValue().getValue().score;
					}
				});

		JFXTreeTableColumn<RowProp, String> scorePercentCol = new JFXTreeTableColumn<>("Score Percentage");
		scorePercentCol.setPrefWidth(186);
		scorePercentCol.setCellValueFactory(
				new Callback<TreeTableColumn.CellDataFeatures<RowProp, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<RowProp, String> param) {
						return param.getValue().getValue().scorePercent;
					}
				});

		ObservableList<RowProp> rows = FXCollections.observableArrayList();
		
		// loadResults();
		rows.add(new RowProp("testevent", "testopt", "testyous", "testdiff", "testscore", "testscore%"));
		rows.add(new RowProp("testevaent", "testaopt", "testydous", "testdiff", "testscdore", "testsdcore%"));
		
        final TreeItem<RowProp> root = new RecursiveTreeItem<RowProp>(rows, RecursiveTreeObject::getChildren);
        resultsTable.getColumns().setAll(eventCol, optTimeCol, yourTimeCol, diffCol, scoreCol, scorePercentCol);
        resultsTable.setRoot(root);
        resultsTable.setShowRoot(false);
		
	}
	
	private ArrayList<Score> loadResults() {
		return null;
	}

	class RowProp extends RecursiveTreeObject<RowProp> {
		StringProperty event;
		StringProperty optTime;
		StringProperty yourTime;
		StringProperty difference;
		StringProperty score;
		StringProperty scorePercent;

		public RowProp(String event, String optTime, String yourTime, String difference, String score, String scorePercent) {
			this.event = new SimpleStringProperty(event);
			this.optTime = new SimpleStringProperty(optTime);
			this.yourTime = new SimpleStringProperty(yourTime);
			this.difference = new SimpleStringProperty(difference);
			this.score = new SimpleStringProperty(score);
			this.scorePercent = new SimpleStringProperty(scorePercent);
		}

	}
}

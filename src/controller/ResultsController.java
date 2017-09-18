package controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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

	private ArrayList<Score> scoringOps = new ArrayList<>();

	@FXML
	JFXTreeTableView<RowProp> resultsTable;
	
	
	@FXML
	private void initialize() {
		// loadTable();
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

		ObservableList<RowProp> updatedRows = loadResults(rows);

		final TreeItem<RowProp> root = new RecursiveTreeItem<RowProp>(updatedRows, RecursiveTreeObject::getChildren);
		resultsTable.getColumns().setAll(eventCol, optTimeCol, yourTimeCol, diffCol, scoreCol, scorePercentCol);
		resultsTable.setRoot(root);
		resultsTable.setShowRoot(false);

	}

	private ObservableList<RowProp> loadResults(ObservableList<RowProp> rows) {
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss:SSSS");

		for (Score s : scoringOps) {
			if (s.getEvent().equals("Failed Braking Attempt")) {
				rows.add(new RowProp(s.getEvent(), "N/A", "N/A", "N/A", String.valueOf(s.getScore()), "N/A"));
			} else {
				rows.add(new RowProp(s.getEvent(), sdf.format(s.getOptimalTime()), sdf.format(s.getYourTime()),
						String.valueOf(s.getDiff()) + "ms", String.valueOf(s.getScore()),
						String.valueOf(s.getScorePercentage()) + "%"));
			}
		}
		return rows;
	}

	class RowProp extends RecursiveTreeObject<RowProp> {
		StringProperty event;
		StringProperty optTime;
		StringProperty yourTime;
		StringProperty difference;
		StringProperty score;
		StringProperty scorePercent;

		public RowProp(String event, String optTime, String yourTime, String difference, String score,
				String scorePercent) {
			this.event = new SimpleStringProperty(event);
			this.optTime = new SimpleStringProperty(optTime);
			this.yourTime = new SimpleStringProperty(yourTime);
			this.difference = new SimpleStringProperty(difference);
			this.score = new SimpleStringProperty(score);
			this.scorePercent = new SimpleStringProperty(scorePercent);
		}

	}

	public void setScoringOps(ArrayList<Score> scoringOps) {
		this.scoringOps = scoringOps;
		loadTable();
	}
	
	@FXML
	private void saveCSV() {
		FileUtility fu = new FileUtility();
		fu.writeBookings(scoringOps);
	}
}

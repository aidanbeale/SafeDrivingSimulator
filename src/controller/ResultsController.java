/**
 * Copyright 2017 John Humphrys
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package controller;

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
import javafx.geometry.Side;
import javafx.scene.chart.PieChart;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;
import simulation.Score;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * The results controller manages the display of results into the results view
 * table
 *
 * @author John Humphrys
 *
 */
public class ResultsController {

    private ArrayList<Score> scoringOps;

    @FXML
    JFXTreeTableView<RowProp> resultsTable;

    @FXML
    AnchorPane pieChartPane;

    /**
     * The load table method is used to create the table and columns
     *
     * @see https://www.youtube.com/watch?v=nbl0kOum-ps
     */
    private void loadTable() {
        JFXTreeTableColumn<RowProp, String> eventCol = new JFXTreeTableColumn<>("Event");
        eventCol.setPrefWidth(150);
        eventCol.setCellValueFactory(
                new Callback<TreeTableColumn.CellDataFeatures<RowProp, String>, ObservableValue<String>>() {
                    @Override
                    public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<RowProp, String> param) {
                        return param.getValue().getValue().event;
                    }
                });

        JFXTreeTableColumn<RowProp, String> optTimeCol = new JFXTreeTableColumn<>("Optimal Time");
        optTimeCol.setPrefWidth(120);
        optTimeCol.setCellValueFactory(
                new Callback<TreeTableColumn.CellDataFeatures<RowProp, String>, ObservableValue<String>>() {
                    @Override
                    public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<RowProp, String> param) {
                        return param.getValue().getValue().optTime;
                    }
                });

        JFXTreeTableColumn<RowProp, String> yourTimeCol = new JFXTreeTableColumn<>("Your Time");
        yourTimeCol.setPrefWidth(120);
        yourTimeCol.setCellValueFactory(
                new Callback<TreeTableColumn.CellDataFeatures<RowProp, String>, ObservableValue<String>>() {
                    @Override
                    public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<RowProp, String> param) {
                        return param.getValue().getValue().yourTime;
                    }
                });

        JFXTreeTableColumn<RowProp, String> diffCol = new JFXTreeTableColumn<>("Difference");
        diffCol.setPrefWidth(120);
        diffCol.setCellValueFactory(
                new Callback<TreeTableColumn.CellDataFeatures<RowProp, String>, ObservableValue<String>>() {
                    @Override
                    public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<RowProp, String> param) {
                        return param.getValue().getValue().difference;
                    }
                });

        JFXTreeTableColumn<RowProp, String> scoreCol = new JFXTreeTableColumn<>("Raw Score");
        scoreCol.setPrefWidth(120);
        scoreCol.setCellValueFactory(
                new Callback<TreeTableColumn.CellDataFeatures<RowProp, String>, ObservableValue<String>>() {
                    @Override
                    public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<RowProp, String> param) {
                        return param.getValue().getValue().score;
                    }
                });

        JFXTreeTableColumn<RowProp, String> scorePercentCol = new JFXTreeTableColumn<>("Score Percentage");
        scorePercentCol.setPrefWidth(120);
        scorePercentCol.setCellValueFactory(
                new Callback<TreeTableColumn.CellDataFeatures<RowProp, String>, ObservableValue<String>>() {
                    @Override
                    public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<RowProp, String> param) {
                        return param.getValue().getValue().scorePercent;
                    }
                });

        JFXTreeTableColumn<RowProp, String> rating = new JFXTreeTableColumn<>("Rating");
        rating.setId("idGroup");
        rating.setPrefWidth(80);
        rating.setCellValueFactory(
                new Callback<TreeTableColumn.CellDataFeatures<RowProp, String>, ObservableValue<String>>() {
                    @Override
                    public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<RowProp, String> param) {
                        if(Integer.parseInt(param.getValue().getValue().score.get()) <= 999) {
                            ObservableValue<String> returnVal = new SimpleStringProperty("★");
                            return returnVal;
                        } else if(Integer.parseInt(param.getValue().getValue().score.get()) <= 1999) {
                            ObservableValue<String> returnVal = new SimpleStringProperty("★★");
                            return returnVal;
                        } else if(Integer.parseInt(param.getValue().getValue().score.get()) <= 3000) {
                            ObservableValue<String> returnVal = new SimpleStringProperty("★★★");
                            return returnVal;
                        } else {
                            return null;
                        }
                    }
                });

        ObservableList<RowProp> rows = FXCollections.observableArrayList();

        ObservableList<RowProp> updatedRows = loadResults(rows);

        final TreeItem<RowProp> root = new RecursiveTreeItem<RowProp>(updatedRows, RecursiveTreeObject::getChildren);
        resultsTable.getColumns().setAll(eventCol, optTimeCol, yourTimeCol, diffCol, scoreCol, scorePercentCol, rating);
        resultsTable.setRoot(root);
        resultsTable.setShowRoot(false);
    }

    /**
     * Used to load the results into the columns
     *
     * @param rows
     *            Create each row
     * @return The populated rows
     */
    private ObservableList<RowProp> loadResults(ObservableList<RowProp> rows) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss:SSSS");

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

    /**
     * Populate the table
     * @author John Humphrys
     *
     */
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

    public void loadPieChart() {
        int crashEvents = 0;
        int failedAttempt = 0;
        int speedingEvent = 0;

        for (Score s : scoringOps) {
            if(s.getEvent().equals("Crash Event")) {
                crashEvents ++;
            } else if(s.getEvent().equals("Failed Braking Attempt")) {
                failedAttempt ++;
            } else if(s.getEvent().equals("Speeding Event")) {
                speedingEvent ++;
            }
        }


        ObservableList<PieChart.Data> pieChartData =
                FXCollections.observableArrayList(
                        new PieChart.Data("Crash Event", crashEvents),
                        new PieChart.Data("Failed Braking Attempt", failedAttempt),
                        new PieChart.Data("Speeding Event", speedingEvent));
        final PieChart chart = new PieChart(pieChartData);
        chart.setTitle("Event Results");
        //chart.setLabelLineLength(10);
        //chart.setLegendSide(Side.LEFT);

        pieChartPane.getChildren().add(chart);
    }

    /**
     * Loads the table data
     * @param scoringOps The list of Score objects to add to the table
     */
    public void setScoringOps(ArrayList<Score> scoringOps) {
        this.scoringOps = scoringOps;
        loadTable();
        loadPieChart();
    }

    /**
     * Used to write the csv to file
     */
    @FXML
    private void saveCSV() {
        FileUtility fu = new FileUtility();
        fu.writeBookings(scoringOps);
    }
}

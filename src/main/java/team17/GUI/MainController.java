package team17.GUI;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import team17.GUI.GanttChart.TaskData;
import team17.GUI.GanttChart.GanttChart;

import java.util.Arrays;

public class MainController {
    @FXML
    private AnchorPane ganttChartContainer;

    @FXML
    private void initialize() {
        createGanttChart();
    }

    private void createGanttChart() {
        String[] processors = new String[] { "P1", "P2", "P3" };

        final NumberAxis xAxis = new NumberAxis();
        final CategoryAxis yAxis = new CategoryAxis();

        final GanttChart<Number,String> chart = new GanttChart<Number,String>(xAxis,yAxis);
        xAxis.setLabel("");
        xAxis.setTickLabelFill(Color.CHOCOLATE);
        xAxis.setMinorTickCount(4);

        yAxis.setLabel("");
        yAxis.setTickLabelFill(Color.CHOCOLATE);
        yAxis.setTickLabelGap(10);
        yAxis.setCategories(FXCollections.<String>observableArrayList(Arrays.asList(processors)));

        chart.setTitle("Current Schedule");
        chart.setLegendVisible(false);
        chart.setBlockHeight(20);
        chart.setVerticalGridLinesVisible(false);
        String machine;

        machine = processors[0];
        XYChart.Series series1 = new XYChart.Series();
        series1.getData().add(new XYChart.Data(0, machine, new TaskData( "a", 1, "status-red")));
        series1.getData().add(new XYChart.Data(4, machine, new TaskData( "b", 1, "status-red")));
        series1.getData().add(new XYChart.Data(6, machine, new TaskData( "c", 1, "status-red")));
        series1.getData().add(new XYChart.Data(9, machine, new TaskData( "d", 1, "status-red")));

        machine = processors[1];
        XYChart.Series series2 = new XYChart.Series();
        series2.getData().add(new XYChart.Data(0, machine, new TaskData( "1", 1, "status-green")));
        series2.getData().add(new XYChart.Data(1, machine, new TaskData( "2", 1, "status-green")));
        series2.getData().add(new XYChart.Data(2, machine, new TaskData( "3",2, "status-green")));

        machine = processors[2];
        XYChart.Series series3 = new XYChart.Series();
        series3.getData().add(new XYChart.Data(0, machine, new TaskData( "a",1, "status-blue")));
        series3.getData().add(new XYChart.Data(1, machine, new TaskData( "a",2, "status-red")));
        series3.getData().add(new XYChart.Data(3, machine, new TaskData( "a",1, "status-green")));

        chart.getData().addAll(series1, series2, series3);

        chart.getStylesheets().add(getClass().getResource("ganttchart.css").toExternalForm());

        ganttChartContainer.getChildren().add(chart);
    }

}

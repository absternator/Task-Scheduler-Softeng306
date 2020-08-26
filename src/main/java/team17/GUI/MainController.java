package team17.GUI;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import team17.Algorithm.AlgorithmState;
import team17.GUI.GanttChart.GanttChart;
import team17.GUI.GanttChart.GanttChartHelper;
import team17.IO.CLI;

public class MainController {
    @FXML
    private AnchorPane ganttChartContainer;

    private CLI _config;
    private AlgorithmState _algorithmState;

    public MainController(CLI config) {
        _config = config;
    }

    public void setAlgorithmState(AlgorithmState algorithmState) {
        _algorithmState = algorithmState;
    }


    public void init() {
        createGanttChart();
    }

    private void createGanttChart() {
        String[] processors = new String[_config.getProcessors()];
        for(int i = 0; i < _config.getProcessors(); i++) {
            processors[i] = "Processor " + String.valueOf(i+1);
        }

        final NumberAxis xAxis = new NumberAxis();
        final CategoryAxis yAxis = new CategoryAxis();

        final GanttChart<Number, String> chart = new GanttChart<Number, String>(xAxis, yAxis);
        xAxis.setLabel("");
        xAxis.setTickLabelFill(Color.CHOCOLATE);
        xAxis.setMinorTickCount(4);

        yAxis.setLabel("");
        yAxis.setTickLabelFill(Color.CHOCOLATE);
        yAxis.setTickLabelGap(10);
        yAxis.setCategories(FXCollections.<String>observableArrayList(processors));

        chart.setTitle("Current Schedule");
        chart.setLegendVisible(false);
        chart.setBlockHeight(20);
        chart.setVerticalGridLinesVisible(false);

        chart.getStylesheets().add(getClass().getResource("ganttchart.css").toExternalForm());

        GanttChartHelper gch = new GanttChartHelper(chart, _config.getProcessors());
        _algorithmState.addObserver(gch);

        ganttChartContainer.getChildren().add(chart);
    }

}

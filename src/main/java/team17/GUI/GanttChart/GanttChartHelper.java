package team17.GUI.GanttChart;

import javafx.collections.FXCollections;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.paint.Color;
import team17.Algorithm.ScheduledTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GanttChartHelper {

    private int _numProcessors;
    private GanttChart<Number, String> _chart;

    public GanttChartHelper(GanttChart<Number, String> chart, int numProcessors) {
        _chart = chart;
        _numProcessors = numProcessors;
    }

    /**
     * Set up the settings for the axis and xychart
     */
    public void initialise() {
        String[] processors = new String[_numProcessors];
        for (int i = 0; i < _numProcessors; i++) {
            processors[i] = "Processor " + String.valueOf(i + 1);
        }


        NumberAxis xAxis = (NumberAxis) _chart.getXAxis();
        xAxis.setLabel("");
        xAxis.setTickLabelFill(Color.CHOCOLATE);
        xAxis.setMinorTickCount(4);
        xAxis.setAnimated(false);

        CategoryAxis yAxis = (CategoryAxis) _chart.getYAxis();
        yAxis.setLabel("");
        yAxis.setTickLabelFill(Color.CHOCOLATE);
        yAxis.setTickLabelGap(10);
        yAxis.setCategories(FXCollections.<String>observableArrayList(processors));


        _chart.setLegendVisible(false);
        _chart.setBlockHeight(20);
        _chart.setVerticalGridLinesVisible(false);
        _chart.setPrefHeight(320);
        _chart.setPrefWidth(605);
    }

    /**
     * Updates gantt chart with the latest solution
     * @param solution
     */
    public void updateGanttChart(List<ScheduledTask> solution) {
        //clear the original data
        _chart.getData().clear();

        // loop through the schedule and group tasks by processor number
        HashMap<Integer, List<ScheduledTask>> processorTasks = new HashMap<>();
        for (ScheduledTask st : solution) {
            if (processorTasks.containsKey(st.getProcessorNum())) {
                processorTasks.get(st.getProcessorNum()).add(st);
            } else {
                List list = new ArrayList<>();
                list.add(st);
                processorTasks.put(st.getProcessorNum(), list);
            }
        }
        for (int i = 0; i < processorTasks.size(); i++) {
            String processor = "Processor " + String.valueOf(i + 1);
            // Create new series for each processorTasks
            XYChart.Series<Number, String> series = new XYChart.Series();
            for (ScheduledTask st : processorTasks.get(i + 1)) {
                // Create new data object for ech task
                series.getData().add(new XYChart.Data<Number, String>(st.getStartTime(), processor, new TaskData(st.getNode().getId(), st.getNode().getWeight(), "status-red")));
            }
            _chart.getData().add(series);
        }
    }

}

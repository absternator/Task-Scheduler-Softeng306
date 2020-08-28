package team17.GUI.GanttChart;

import javafx.scene.chart.XYChart;

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

    public void updateGanttChart(List<ScheduledTask> solution) {
        //clear the original data
        _chart.getData().clear();

        // loop through the schedule and group tasks to the correct processorTasks
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
        for (int i = 0; i < _numProcessors; i++) {
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

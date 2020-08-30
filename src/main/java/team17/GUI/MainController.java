package team17.GUI;

import eu.hansolo.tilesfx.Tile;
import eu.hansolo.tilesfx.TileBuilder;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;

import javafx.embed.swing.SwingNode;

import javafx.fxml.FXML;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.util.Duration;

import team17.Algorithm.AlgorithmState;
import team17.DAG.DAGGraph;
import team17.GUI.GraphVisualisation.GraphVisualisation;
import team17.GUI.GanttChart.GanttChart;
import team17.GUI.GanttChart.GanttChartHelper;
import team17.IO.CLI;

public class MainController {

    @FXML
    private AnchorPane ganttChartContainer;

    @FXML
    private Pane memoryPane;
    @FXML
    private TextFlow inputText;
    @FXML
    private TextFlow outputText;
    @FXML
    private TextFlow processorsNumberText;
    @FXML
    private Text statusText;
    @FXML
    private SwingNode graphPane;
    @FXML
    private Text runningTime;
    @FXML
    private Pane statusPane;

    @FXML
    private Text completeText;

    @FXML
    private Text expandedText;

    @FXML
    private Text unexpandedText;

    @FXML
    private Text prunedText;

    @FXML
    private Text bestCostText;

    private Tile _memoryUsageTile;
    private double _maxMemory;
    private double _usedMemory;
    private double _duration;
    private double _startTime;
    private boolean _timing;
    private final CLI _config;
    private final DAGGraph _graph;
    private AlgorithmState _algorithmState;
    private GanttChartHelper _gch;

    public MainController(CLI config, DAGGraph graph) {
        _config = config;
        _graph = graph;
    }

    public void setAlgorithmState(AlgorithmState algorithmState) {
        _algorithmState = algorithmState;
    }

    /**
     * This method initialise the settings for GUI
     */
    public void init() {
        setUpGanttChart();

        //read and set the input and output file names
        setUpInputFileName();
        setUpOutputFileName();

        //embed input graph to GraphPane
        setUpGraphPane();

        //read and set the number of processor
        setUpNumberOfProcessors();

        //Get the max memory that can be used for this computer
        _maxMemory = Runtime.getRuntime().maxMemory() / 1048576; // in bytes

        //set up the memory usage tile pane
        setUpMemoryPane();

        //start polling
        _timing=true;
        _startTime = System.currentTimeMillis();
        startTiming();
        updateGUI();
    }


    /**
     * Method to read the memory usage from the system periodically and update the corresponding GUI element
     */
    private void updateGUI() {
        Timeline tm = new Timeline();
        KeyFrame frame = new KeyFrame(Duration.millis(1000), event -> {
            _usedMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
            _usedMemory = _usedMemory / 1000000;
            _memoryUsageTile.setValue(_usedMemory);
            //update the timer in the GUI
            int min = (int) (_duration /60);
            double sec = _duration -min*60;
            sec = Math.round(sec * 100.0) / 100.0;
            String str = min + "m" + sec + "s";
            runningTime.setText(str);
            //update gantt chart
            if (_algorithmState.getCompleteSolution() != null) {
                _gch.updateGanttChart(_algorithmState.getCompleteSolution().fullSchedule());
                bestCostText.setText(String.valueOf(_algorithmState.getCompleteSolution().getScheduledTask().getFinishTime()));
            }

            updateSolutionStats();

            if (_algorithmState.isFinished()) {
                _algorithmState.setFinished(false);
                //change the status from "running" to "done"
                updateStatus();
            }
        });
        tm.setCycleCount(Timeline.INDEFINITE);
        tm.getKeyFrames().add(frame);
        tm.play();
    }

    private void startTiming() {
        Timeline tm = new Timeline();
        KeyFrame frame = new KeyFrame(Duration.millis(1), event -> {
            //check whether the sorting is finished
            if (_timing) {
                // the sort is still running so keep updating the timer
                double currentTime =  System.currentTimeMillis();
                _duration = (currentTime - _startTime)/1000; // in second
            }
            if (_algorithmState.isFinished()) {
                //stop timing
                _timing=false;
            }
        });
        tm.setCycleCount(Timeline.INDEFINITE);
        tm.getKeyFrames().add(frame);
        tm.play();
    }

    /**
     * Updates the statistics relating to the search
     */
    private void updateSolutionStats() {
        completeText.setText(String.valueOf(_algorithmState.getNumCompleteSolutions()));
        expandedText.setText(String.valueOf(_algorithmState.getNumExpandedPartialSolutions()));
        unexpandedText.setText(String.valueOf(_algorithmState.getNumUnexpandedPartialSolutions()));
        prunedText.setText(String.valueOf(_algorithmState.getNumPruned()));
    }

    /**
     * Method to get the input file name and update the corresponding GUI element
     */
    public void setUpInputFileName() {
        String inputFile = _config.getInput();
        // remove the path "src/main..."
        if(inputFile.contains("/")){
            inputFile = inputFile.substring(inputFile.lastIndexOf('/') + 1);
        }
        inputFile = "  " + inputFile;
        Text iText = new Text(inputFile);
        iText.setStyle("-fx-font: 20 System;");
        inputText.getChildren().add(iText);
    }

    public void setUpOutputFileName() {
        String outputFile = _config.getOutput();
        // remove the path "src/main..."
        if(outputFile.contains("/")){
            outputFile = outputFile.substring(outputFile.lastIndexOf('/') + 1);
        }

        outputFile = "  " + outputFile;
        Text oText = new Text(outputFile);
        oText.setStyle("-fx-font: 20 System;");
        outputText.getChildren().add(oText);
    }

    public void setUpNumberOfProcessors() {
        int processorsNumber = _config.getProcessors();
        String processorNumberString = String.valueOf(processorsNumber);
        processorNumberString = "  " + processorNumberString;
        Text pText = new Text(processorNumberString);
        pText.setStyle("-fx-font: 20 System;");
        processorsNumberText.getChildren().add(pText);
    }

    public void updateStatus() {
        statusText.setText("Done");
    }

    private void setUpMemoryPane() {
        _memoryUsageTile = TileBuilder.create()
                .skinType(Tile.SkinType.GAUGE)
                .maxValue(_maxMemory)
                .threshold(_maxMemory * 0.85)
                .thresholdVisible(false)
                .unit("MB")
                .startFromZero(true)
                .animated(true)
                //.backgroundColor(Color.WHITE)
                .prefWidth(memoryPane.getWidth())
                .prefHeight(memoryPane.getHeight())
                .build();
        _memoryUsageTile.setValue(0);
        memoryPane.getChildren().addAll(_memoryUsageTile);
    }

    private void setUpGraphPane() {
        GraphVisualisation gv = new GraphVisualisation(_graph);
        gv.createSwingGraph(graphPane);
    }

    private void setUpGanttChart() {
        final NumberAxis xAxis = new NumberAxis();
        final CategoryAxis yAxis = new CategoryAxis();

        final GanttChart<Number, String> chart = new GanttChart<>(xAxis, yAxis);
        _gch = new GanttChartHelper(chart, _config.getProcessors());
        //Set up axis, title and other details of the chart
        _gch.initialise();

        chart.getStylesheets().add(getClass().getResource("ganttchart.css").toExternalForm());

        ganttChartContainer.getChildren().add(chart);
    }
}

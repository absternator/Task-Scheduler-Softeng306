package team17.GUI;

import eu.hansolo.tilesfx.Tile;
import eu.hansolo.tilesfx.TileBuilder;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.embed.swing.SwingNode;
import javafx.fxml.FXML;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
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
    private Pane MemoryPane;
    @FXML
    private TextFlow InputText;
    @FXML
    private TextFlow OutputText;
    @FXML
    private TextFlow ProcessorsNumberText;
    @FXML
    private Text StatusText;
    @FXML
    private SwingNode GraphPane;
    @FXML
    private Text runningTime;
    @FXML
    private Pane StatusPane;

    private Tile memoryUsageTile;
    private double maxMemory;
    private double usedMemory;
    private int processorsNumber;
    private String inputFile;
    private String outputFile;
    private double duration;
    private double startTime;
    private boolean timing;
    private CLI _config;
    private DAGGraph _graph;
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
     * This method initialise the setting for GUI
     */
    public void init() {
        createGanttChart();
        //read and set the input file name
        setUpInputFileName();

        //embed input graph to GraphPane
        setUpGraphPane();

        //read and set the number of processor
        setUpNumberOfProcessors();

        //Get the max memory that can be used for this computer
        maxMemory = Runtime.getRuntime().maxMemory() / 1048576; // in bytes

        //set up the memory usage tile pane
        setUpMemoryPane();
        memoryUsageTile.setValue(0);

        //start polling
        timing=true;
        startTime = System.currentTimeMillis();
        startTiming();
        updateGUI();
    }


    /**
     * Method to read the memory usage from the system periodically and update the corresponding GUI element
     */
    private void updateGUI() {
        Timeline tm = new Timeline();
        KeyFrame frame = new KeyFrame(Duration.millis(300), event -> {
            usedMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
            usedMemory = usedMemory / 1000000;
            memoryUsageTile.setValue(usedMemory);
            //update the timer in the GUI
            int min = (int) (duration/60);
            double sec = duration-min*60;
            sec = Math.round(sec * 100.0) / 100.0;
            String str = String.valueOf(min) + "m" + String.valueOf(sec)+ "s";
            runningTime.setText(String.valueOf(str));
            //update gantt chart
            if (_algorithmState.getCompleteSolution() != null) {
                _gch.updateGanttChart(_algorithmState.getCompleteSolution().fullSchedule());
            }
            if (_algorithmState.getFinished()) {
                _algorithmState.setFinished(false);
                setUpOutputFileName();
                //change the status from "running" to "done"
                UpdateStatus();

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
            if (timing != false) {
                // the sort is still running so keep updating the timer
                double currentTime =  System.currentTimeMillis();
                duration = (currentTime - startTime)/1000; // in second
            }
            if (_algorithmState.getFinished()) {
                //stop timing
                timing=false;
            }

        });
        tm.setCycleCount(Timeline.INDEFINITE);
        tm.getKeyFrames().add(frame);
        tm.play();
    }
    /**
     * Method to get the input file name and update the corresponding GUI element
     */
    public void setUpInputFileName() {
        inputFile = _config.getInput();
        // remove the path "src/main..."
        if(inputFile.contains("/")){
            inputFile = inputFile.substring(inputFile.lastIndexOf('/') + 1);
        }
        inputFile = "  " + inputFile;
        Text iText = new Text(inputFile);
        iText.setStyle("-fx-font: 20 System;");
        InputText.getChildren().add(iText);
    }

    public void setUpOutputFileName() {
        outputFile = _config.getOutput();
        // remove the path "src/main..."
        if(outputFile.contains("/")){
            outputFile = outputFile.substring(outputFile.lastIndexOf('/') + 1);
        }

        outputFile = "  " + outputFile;
        Text oText = new Text(outputFile);
        oText.setStyle("-fx-font: 20 System;");
        OutputText.getChildren().add(oText);
    }

    public void setUpNumberOfProcessors() {
        processorsNumber = _config.getProcessors();
        String processorNumberString = String.valueOf(processorsNumber);
        processorNumberString = "  " + processorNumberString;
        Text pText = new Text(processorNumberString);
        pText.setStyle("-fx-font: 20 System;");
        ProcessorsNumberText.getChildren().add(pText);
    }

    public void UpdateStatus() {
        StatusText.setText("Done");
    }


    private void setUpMemoryPane() {
        this.memoryUsageTile = TileBuilder.create()
                .skinType(Tile.SkinType.GAUGE)
                .maxValue(maxMemory)
                .threshold(maxMemory * 0.85)
                .thresholdVisible(false)
                .unit("MB")
                .startFromZero(true)
                .animated(true)
                //.backgroundColor(Color.WHITE)
                .prefWidth(MemoryPane.getWidth())
                .prefHeight(MemoryPane.getHeight())
                .build();
        MemoryPane.getChildren().addAll(memoryUsageTile);
    }

    private void setUpGraphPane() {
        GraphVisualisation gv = new GraphVisualisation(_graph);
        gv.createSwingGraph(GraphPane);
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
        xAxis.setAnimated(false);

        yAxis.setLabel("");
        yAxis.setTickLabelFill(Color.CHOCOLATE);
        yAxis.setTickLabelGap(10);
        yAxis.setCategories(FXCollections.<String>observableArrayList(processors));

        chart.setTitle("Current Schedule");
        chart.setLegendVisible(false);
        chart.setBlockHeight(20);
        chart.setVerticalGridLinesVisible(false);

        chart.getStylesheets().add(getClass().getResource("ganttchart.css").toExternalForm());

        _gch = new GanttChartHelper(chart, _config.getProcessors());

        ganttChartContainer.getChildren().add(chart);
    }
}

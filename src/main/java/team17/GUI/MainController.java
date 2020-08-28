package team17.GUI;

import eu.hansolo.tilesfx.Tile;
import eu.hansolo.tilesfx.TileBuilder;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
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

    private Tile memoryUsageTile;
    private double maxMemory;
    private int processorsNumber;
    private String inputFile;
    private String outputFile;


    private CLI _config;
    private AlgorithmState _algorithmState;
    private GanttChartHelper _gch;

    public MainController(CLI config) {
        _config = config;
    }

    public void setAlgorithmState(AlgorithmState algorithmState) {
        _algorithmState = algorithmState;
    }


    public void init() {
        createGanttChart();
        setUpInputFileName();

        setUpNumberOfProcessors();
        //show memory usage
        maxMemory = Runtime.getRuntime().maxMemory() / 1048576; // in bytes
        setUpMemoryPane();
        memoryUsageTile.setValue(0);
        readMemory();
        startTiming();
    }

    /*
    using polling to read the memory usage
    period = 1 seconds
     */
    private void readMemory() {
        Timeline tm = new Timeline(new KeyFrame(Duration.millis(1000), event -> {
            double usedMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
            usedMemory = usedMemory / 1000000;
            memoryUsageTile.setValue(usedMemory);
            //determine whether the overall sorting is finished or not
            if (_algorithmState.getFinished()) {
                _algorithmState.setFinished(false);
                setUpOutputFileName();
                UpdateStatus();
            }

            if (_algorithmState.getCompleteSolution() != null) {
                _gch.updateGanttChart(_algorithmState.getCompleteSolution().fullSchedule());
            }

        }));
        tm.setCycleCount(Timeline.INDEFINITE);
        tm.play();
    }

    public void setUpInputFileName() {
        inputFile = _config.getInput();
        inputFile = inputFile.substring(inputFile.lastIndexOf('/') + 1);
        inputFile = "  " + inputFile;
        Text iText = new Text(inputFile);
        iText.setStyle("-fx-font: 22 System;");
        InputText.getChildren().add(iText);
    }

    public void setUpOutputFileName() {
        outputFile = _config.getOutput();
        //OutputFile = "src/main/graphout.dot";
        outputFile = outputFile.substring(outputFile.lastIndexOf('/') + 1);
        outputFile = "  " + outputFile;
        Text oText = new Text(outputFile);
        oText.setStyle("-fx-font: 22 System;");
        OutputText.getChildren().add(oText);
    }

    public void setUpNumberOfProcessors() {
        processorsNumber = _config.getProcessors();
        String processorNumberString = String.valueOf(processorsNumber);
        processorNumberString = "  " + processorNumberString;
        Text pText = new Text(processorNumberString);
        pText.setStyle("-fx-font: 22 System;");
        ProcessorsNumberText.getChildren().add(pText);
    }

    public void UpdateStatus() {
        StatusText.setText("Done");
    }

    private void startTiming() {

    }

    private void finishTiming() {

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

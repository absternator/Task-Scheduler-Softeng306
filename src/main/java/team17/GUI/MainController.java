package team17.GUI;


import eu.hansolo.tilesfx.TileBuilder;
import eu.hansolo.tilesfx.Tile;
import eu.hansolo.tilesfx.TileBuilder;
import eu.hansolo.tilesfx.colors.Bright;
import javafx.collections.FXCollections;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;
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
    private String InputFile;
    private String OutputFile;

    private CLI _config;
    private AlgorithmState _algorithmState;

    public MainController(CLI config) {
        _config = config;
    }

    public void setAlgorithmState(AlgorithmState algorithmState) {
        _algorithmState = algorithmState;
    }


    public void init() {
        setUpInputFileName();
        setUpOutputFileName();
        setUpNumberOfProcessors();
        //show memory usage
        maxMemory=Runtime.getRuntime().maxMemory()/1048576; // in bytes
        setUpMemoryPane();
        memoryUsageTile.setValue(0);
        readValue();
        startTiming();


    }
    private void readValue(){
        Timeline tm = new Timeline(new KeyFrame(Duration.millis(50), event -> {
            double usedMemory=Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory();
            usedMemory= usedMemory/1000000;
            memoryUsageTile.setValue(usedMemory);
        /* to do:
        if(finished) {
                    finished = false;
                    tm.stop();
                    setUpOutputFileName();
                    UpdateStatus();
                }
         */
        } ));
        tm.setCycleCount(Timeline.INDEFINITE);
        tm.play();

    }

    public void setUpInputFileName(){
        InputFile=_config.getInput();
        InputFile = InputFile.substring(InputFile.lastIndexOf('/')+1);
        InputFile = "  " + InputFile;
        Text iText = new Text (InputFile);
        iText.setStyle("-fx-font: 22 System;");
        InputText.getChildren().add(iText);
    }

    public void setUpOutputFileName(){
        OutputFile=_config.getOutput();
        OutputFile="src/main/graphout.dot";
        OutputFile = OutputFile.substring(OutputFile.lastIndexOf('/')+1);
        OutputFile = "  "+OutputFile;
        Text oText = new Text (OutputFile);
        oText.setStyle("-fx-font: 22 System;");
        OutputText.getChildren().add(oText);
    }

    public void setUpNumberOfProcessors(){
        processorsNumber=_config.getProcessors();
        String processorNumberString = String.valueOf(processorsNumber);
        processorNumberString = "  "+ processorNumberString;
        Text pText = new Text(processorNumberString);
        pText.setStyle("-fx-font: 22 System;");
        ProcessorsNumberText.getChildren().add(pText);
    }

    public void UpdateStatus(){
        StatusText.setText("Done");
    }

    private void startTiming(){

    }

    private void finishTiming(){

    }


    private void setUpMemoryPane(){
        this.memoryUsageTile = TileBuilder.create()
                .skinType(Tile.SkinType.GAUGE)
                .maxValue(maxMemory)
                .threshold(maxMemory*0.85)
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



}

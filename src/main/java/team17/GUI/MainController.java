package team17.GUI;



import eu.hansolo.tilesfx.TileBuilder;
import eu.hansolo.tilesfx.Tile;
import eu.hansolo.tilesfx.TileBuilder;
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
import javafx.util.Duration;
import team17.Algorithm.AlgorithmState;
import team17.GUI.GanttChart.GanttChart;
import team17.GUI.GanttChart.GanttChartHelper;
import team17.IO.CLI;

public class MainController {

    @FXML
    private Pane MemoryPane;
    private Tile memoryUsageTile;
    private double maxMemory;

    private CLI _config;
    private AlgorithmState _algorithmState;

    public MainController(CLI config) {
        _config = config;
    }

    public void setAlgorithmState(AlgorithmState algorithmState) {
        _algorithmState = algorithmState;
    }


    public void init() {
        //show memory usage
        maxMemory=Runtime.getRuntime().maxMemory()/1048576; // in bytes
        setUpMemoryPane();
        memoryUsageTile.setValue(0);
        readValue();

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
                }
         */
        } ));
        tm.setCycleCount(Timeline.INDEFINITE);
        tm.play();

    }

    public void setUpInputFileName(){

    }

    public void setUpOutputFileName(){

    }

    public void setUpNumberOfProcessors(){

    }

    public void UpdateStatus(){

    }

    public void setUpTime(){

    }

    private void setUpMemoryPane(){
        this.memoryUsageTile = TileBuilder.create()
                .skinType(Tile.SkinType.GAUGE)
                .maxValue(maxMemory)
                .threshold(maxMemory*0.9)
                .thresholdVisible(false)
                .unit("MB")
                .startFromZero(true)
                .animated(true)
                .build();
        MemoryPane.getChildren().addAll(memoryUsageTile);
    }



}

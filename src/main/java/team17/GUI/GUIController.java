package team17.GUI;


import eu.hansolo.tilesfx.TileBuilder;
import eu.hansolo.tilesfx.Tile;
import eu.hansolo.tilesfx.TileBuilder;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.util.Duration;


public class GUIController {
    @FXML
    private Pane MemoryPane;


    private Tile memoryUsageTile;
    private double maxMemory;


    public void initialize(){
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
                .maxValue(100)
                .threshold(100*0.9)
                .thresholdVisible(false)
                .unit("MB")
                .startFromZero(true)
                .animated(true)
                .build();
        MemoryPane.getChildren().addAll(memoryUsageTile);
    }


}

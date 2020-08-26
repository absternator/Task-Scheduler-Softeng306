package team17.GUI;


import eu.hansolo.tilesfx.TileBuilder;
import eu.hansolo.tilesfx.Tile;
import eu.hansolo.tilesfx.TileBuilder;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;


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
    }

    private void readValue(){

    }

    public void setUpInputFileName(){

    }

    public void setUpOutputFileName(){

    }

    public void setUpNumberOfProcessors(){

    }

    public void setUpStatus(){

    }

    public void setUpTime(){

    }

    private void setUpMemoryPane(){
        this.memoryUsageTile = TileBuilder.create()
                .skinType(Tile.SkinType.GAUGE)
                .maxValue(maxMemory)
                .unit("MB")
                .startFromZero(true)
                .animated(true)
                .build();
        MemoryPane.getChildren().addAll(memoryUsageTile);
    }


}

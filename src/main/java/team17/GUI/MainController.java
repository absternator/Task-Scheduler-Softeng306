package team17.GUI;


import eu.hansolo.tilesfx.TileBuilder;
import eu.hansolo.tilesfx.Tile;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.embed.swing.SwingNode;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.util.Duration;
import team17.Algorithm.AlgorithmState;
import team17.GUI.GraphVisualisation.GraphVisualisation;
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

    @FXML
    private Pane GraphPane;

    private Tile memoryUsageTile;
    private double maxMemory;
    private int processorsNumber;
    private String inputFile;
    private String outputFile;


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

    private void setUpGraphPane() {
        SwingNode sn = new SwingNode();
        GraphVisualisation gv = new GraphVisualisation();
        gv.createSwingContent(sn);
        GraphPane.getChildren().add(sn);
    }


}

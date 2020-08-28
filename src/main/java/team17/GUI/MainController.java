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

    private Tile _memoryUsageTile;
    private double _maxMemory;
    private int _processorsNumber;
    private String _inputFile;
    private String _outputFile;


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
        _maxMemory = Runtime.getRuntime().maxMemory() / 1048576; // in bytes
        setUpMemoryPane();
        _memoryUsageTile.setValue(0);
        readValue();
        startTiming();


    }

    private void readValue() {
        Timeline tm = new Timeline(new KeyFrame(Duration.millis(1000), event -> {
            double usedMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
            usedMemory = usedMemory / 1000000;
            _memoryUsageTile.setValue(usedMemory);

            if (_algorithmState.getFinished()) {
                //tm.stop();
                _algorithmState.setFinished(false);
                setUpOutputFileName();
                UpdateStatus();
            }

        }));
        tm.setCycleCount(Timeline.INDEFINITE);
        tm.play();

    }

    public void setUpInputFileName() {
        _inputFile = _config.getInput();
        _inputFile = _inputFile.substring(_inputFile.lastIndexOf('/') + 1);
        _inputFile = "  " + _inputFile;
        Text iText = new Text(_inputFile);
        iText.setStyle("-fx-font: 22 System;");
        InputText.getChildren().add(iText);
    }

    public void setUpOutputFileName() {
        _outputFile = _config.getOutput();
        //OutputFile = "src/main/graphout.dot";
        _outputFile = _outputFile.substring(_outputFile.lastIndexOf('/') + 1);
        _outputFile = "  " + _outputFile;
        Text oText = new Text(_outputFile);
        oText.setStyle("-fx-font: 22 System;");
        OutputText.getChildren().add(oText);
    }

    public void setUpNumberOfProcessors() {
        _processorsNumber = _config.getProcessors();
        String processorNumberString = String.valueOf(_processorsNumber);
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
        this._memoryUsageTile = TileBuilder.create()
                .skinType(Tile.SkinType.GAUGE)
                .maxValue(_maxMemory)
                .threshold(_maxMemory * 0.85)
                .thresholdVisible(false)
                .unit("MB")
                .startFromZero(true)
                .animated(true)
                //.backgroundColor(Color.WHITE)
                .prefWidth(MemoryPane.getWidth())
                .prefHeight(MemoryPane.getHeight())
                .build();
        MemoryPane.getChildren().addAll(_memoryUsageTile);
    }

    private void setUpGraphPane() {
        SwingNode sn = new SwingNode();
        GraphVisualisation gv = new GraphVisualisation();
        gv.createSwingContent(sn);
        GraphPane.getChildren().add(sn);
    }


}

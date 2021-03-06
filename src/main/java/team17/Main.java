package team17;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import team17.Algorithm.Algorithm;
import team17.Algorithm.AlgorithmState;
import team17.Algorithm.DFS;
import team17.Algorithm.ScheduledTask;
import team17.DAG.DAGGraph;
import team17.DAG.InvalidGraphException;
import team17.GUI.MainController;
import team17.IO.CLI;
import team17.IO.FileReadWriter;
import team17.IO.IncorrectCLIInputException;
import team17.IO.InvalidEntryException;

import java.io.IOException;
import java.util.List;

/**
 * Main class of this program
 */
public class Main extends Application {

    private static CLI _config;
    private static AlgorithmState _algorithmState;
    private static DAGGraph _graph;
    private static FileReadWriter _frw;

    public static void main(String[] args) {
        _config = new CLI();
        try {
            _config.readCLI(args);
        } catch (IncorrectCLIInputException e) {
            System.out.println(e.getMessage());
            System.out.println();
            _config.printHelpFormatter();
            System.exit(1);
        }

        _frw = new FileReadWriter(_config);
        try {
            _graph = _frw.readDotFile();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidGraphException e) {
            System.out.println(e.getMessage());
            System.exit(1);
        } catch (InvalidEntryException e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }

        if (_config.getVisualise()) {
            launch();
        } else {
            startAlgorithm();
            Platform.exit();
        }
    }

    /**
     * This method starts the main algorithm of the program
     */
    private static void startAlgorithm() {

        List<ScheduledTask> schedule;
        Algorithm algorithm= new DFS(_graph, _algorithmState);

        if (_config.getCores() < 2) {
            schedule = algorithm.getOptimalSchedule(_graph).fullSchedule(); // Returns list of Schedule
        } else {
            schedule = algorithm.getOptimalScheduleParallel(_graph, _config.getCores() - 1).fullSchedule();
        }

        try {
            _frw.writeOutput(schedule);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (_algorithmState != null) {
            _algorithmState.setFinished(true);
        }
    }

    @Override
    public void start(Stage primaryStage) {
        try {

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("view.fxml"));
            MainController mainController = new MainController(_config, _graph);
            loader.setController(mainController);
            Parent root = loader.load();

            _algorithmState = new AlgorithmState();
            mainController.setAlgorithmState(_algorithmState);
            mainController.init();

            // To make sure application stops
            primaryStage.setOnCloseRequest(e -> {
                Platform.exit();
                System.exit(0);
            });

            Thread thread = new Thread(Main::startAlgorithm);
            thread.start();

            Scene scene = new Scene(root, 1000, 750);
            scene.getStylesheets().add(getClass().getResource("view.css").toExternalForm());
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);

            primaryStage.setTitle("17-Peaches\uD83C\uDF51Task Scheduler");
            primaryStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}





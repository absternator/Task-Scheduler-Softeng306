package team17;

import team17.Algorithm.Algorithm;
import team17.Algorithm.AStar;
import team17.Algorithm.DFS;

import javafx.application.Application;
import javafx.application.Platform;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import team17.Algorithm.*;
import team17.DAG.DAGGraph;
import team17.GUI.MainController;
import team17.IO.CLI;
import team17.IO.FileReadWriter;

import java.io.IOException;
import java.util.List;

public class Main extends Application {

    private static CLI _config;
    private static AlgorithmState _algorithmState;
    private static DAGGraph _graph;
    private static FileReadWriter _frw;
    private static volatile boolean _guiActive = false;
    private static volatile boolean _algoActive = false;


    public static void main(String[] args) {
        //Run from command line
//        args = new String[]{"../../src/main/resources/graph.dot", "2", "-v"};

        //Run in IDE
//        args = new String[]{"src/main/resources/INPUT0.dot", "2", "-v"};
       args = new String[]{"src/main/resources/graph2.dot", "2", "-v"};

        _config = new CLI(args);

        _frw = new FileReadWriter(_config);
        try {
            _graph = _frw.readDotFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (_config.getVisualise()) {
            launch();
        } else {
            startAlgorithm();
//            Platform.exit();
        }
    }

    private static void startAlgorithm() {
        _algoActive = true;

        List<ScheduledTask> schedule;
        Algorithm algorithm;

        if (false) {
            algorithm = new DFS(_graph, _algorithmState); //TODO remove graph parameter
            schedule = algorithm.getOptimalSchedule(_graph).fullSchedule();// Returns list of Schedule
        } else {
            // for small graphs, use the A* algorithm
            algorithm = new AStar(_graph, _algorithmState); //TODO remove graph parameter
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

            // Sets algorithm state as nonactive
            _algoActive = false;

            // Check if GUI is still active, if not, exit the program
            if (!_guiActive) {
                Platform.exit();
                System.exit(0);
            }
        }
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            _guiActive = true;

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
                _guiActive = false; // Sets GUI to nonactive

                // Checks if algorithm is still running, if not then exit the program
                if (!_algoActive) {
                    System.exit(0);
                }
            });

            // run Astar
            Thread thread = new Thread(){
                public void run(){
                    startAlgorithm();
                }
            };
            thread.start();

            Scene scene = new Scene(root, 1010, 750);
            scene.getStylesheets().add(getClass().getResource("view.css").toExternalForm());
            primaryStage.setScene(scene);

            //primaryStage.setOnCloseRequest();
            primaryStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}





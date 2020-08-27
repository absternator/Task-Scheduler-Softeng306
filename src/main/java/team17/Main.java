package team17;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import team17.Algorithm.*;
import team17.DAG.Graph;
import team17.GUI.MainController;
import team17.IO.CLI;
import team17.IO.FileReadWriter;

import java.io.IOException;
import java.util.List;

public class Main extends Application {

    private static CLI _config;
    private static AlgorithmState _algorithmState;


    public static void main(String[] args) {
        //Run from command line
        //args = new String[]{"../../src/main/resources/graph.dot", "2"};

        //Run in IDE
        args = new String[]{"src/main/resources/graph5.dot", "2"};

        _config = new CLI(args);

        if (_config.getVisualise()) {
            launch();
        } else {
            startAlgorithm();
        }
    }

    private static void startAlgorithm() {
        try {
            FileReadWriter frw = new FileReadWriter(_config);
            Graph graph = frw.readDotFile();
            List<ScheduledTask> schedule;
            Algorithm algorithm;

            if (false) {
                algorithm = new DFS(graph,_algorithmState); //TODO remove graph parameter
                schedule = algorithm.getOptimalSchedule(graph).fullSchedule();// Returns list of Schedule
            } else {
                // for small graphs, use the A* algorithm
                algorithm = new AStar(graph,_algorithmState); //TODO remove graph parameter
                if (_config.getCores() < 2) {
                    schedule = algorithm.getOptimalSchedule(graph).fullSchedule(); // Returns list of Schedule
                } else {
                    schedule = algorithm.getOptimalScheduleParallel(graph, _config.getCores() - 1).fullSchedule();
                }
            }
            frw.writeOutput(schedule);
            if(_algorithmState != null) {
                _algorithmState.setFinished(true);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("FINISHED");
    }


    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("view.fxml"));
            MainController mainController = new MainController(_config);
            loader.setController(mainController);
            Parent root = loader.load();

            _algorithmState = new AlgorithmState();
            mainController.setAlgorithmState(_algorithmState);
            mainController.init();

            // run Astar
            Thread thread = new Thread(){
                public void run(){
                    startAlgorithm();
                }
            };
            thread.start();

            Scene scene = new Scene(root, 1000, 750);
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}





package team17;

import team17.Algorithm.Algorithm;
import team17.Algorithm.AStar;
import team17.Algorithm.DFS;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
<<<<<<< HEAD

import team17.Algorithm.ScheduledTask;
import team17.DAG.Graph;
=======
import team17.Algorithm.AlgorithmAStar;
import team17.Algorithm.ListScheduling;
import team17.Algorithm.ScheduledTask;
import team17.DAG.Graph;
import team17.GUI.visualiser;
>>>>>>> working application, removed non existent controller
import team17.IO.CLI;
import team17.IO.FileReadWriter;

import java.io.IOException;
import java.util.List;

public class Main extends Application {

    public static void main(String[] args) {
        //Run from command line
        //args = new String[]{"../../src/main/resources/graph.dot", "2"};

        //Run in IDE

        args = new String[]{"src/main/resources/graph.dot", "2"};


        CLI cli = new CLI(args);
        FileReadWriter frw = new FileReadWriter(cli);
        Algorithm algorithm;
        launch();

        try {
            Graph graph = frw.readDotFile();
            List<ScheduledTask> schedule;

            // Temp Conditions
            //graph.getNodeList().size() > 11
            //                    || (graph.getNodeList().size() >10 && graph.getNumOfProcessors() > 3)
            //                    || (graph.getNodeList().size() >9 && graph.getNumOfProcessors() > 6)
            if (false) {
                algorithm = new DFS(graph); //TODO remove graph parameter
                if(cli.getCores()<2) {
                    schedule = algorithm.getOptimalSchedule(graph).fullSchedule(); // Returns list of Schedule
                } else {
                    schedule = algorithm.getOptimalScheduleParallel(graph, cli.getCores()-1).fullSchedule();
                }
            } else {
                // for small graphs, use the A* algorithm
                algorithm = new AStar(graph); //TODO remove graph parameter
                if(cli.getCores()<2) {
                    schedule = algorithm.getOptimalSchedule(graph).fullSchedule(); // Returns list of Schedule
                } else {
                    schedule = algorithm.getOptimalScheduleParallel(graph, cli.getCores()-1).fullSchedule();
                }
            }

            frw.writeOutput(schedule);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void startVisualisation(String[] args) {
        new visualiser().startVisualisation(args);
    }


    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
            FXMLLoader loader =new FXMLLoader();
            loader.setLocation(getClass().getResource("view.fxml"));
            Parent root=loader.load();
            primaryStage.setScene(new Scene(root, 1000, 800));
            primaryStage.show();
        }catch (Exception e){

        }
    }
}





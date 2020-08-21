package team17;

import team17.Algorithm.Algorithm;
import team17.Algorithm.AStar;
import team17.Algorithm.DFS;
import team17.Algorithm.ScheduledTask;
import team17.DAG.Graph;
import team17.IO.CLI;
import team17.IO.FileReadWriter;

import java.io.IOException;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        //Run from command line
        //args = new String[]{"../../src/main/resources/graph.dot", "2"};

        //Run in IDE
        args = new String[]{"src/main/resources/graph2.dot", "2"};

        CLI cli = new CLI(args);
        FileReadWriter frw = new FileReadWriter(cli);
        Algorithm algorithm;

        try {
            Graph graph = frw.readDotFile();


            // Temp Conditions
            //graph.getNodeList().size() > 11
            //                    || (graph.getNodeList().size() >10 && graph.getNumOfProcessors() > 3)
            //                    || (graph.getNodeList().size() >9 && graph.getNumOfProcessors() > 6)
            if (true) {
                algorithm = new DFS(graph); //TODO remove graph parameter
            } else {
                // for small graphs, use the A* algorithm
                algorithm = new AStar(graph); //TODO remove graph parameter
            }
            List<ScheduledTask> schedule = algorithm.getOptimalSchedule(graph).fullSchedule();// Returns list of Schedule

            frw.writeOutput(schedule);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}





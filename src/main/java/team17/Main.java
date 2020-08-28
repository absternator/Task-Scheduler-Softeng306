package team17;

import team17.Algorithm.*;
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

        args = new String[]{"src/main/resources/graph.dot", "2"};


        CLI cli = new CLI(args);
        FileReadWriter frw = new FileReadWriter(cli);
        Algorithm algorithm;

        try {
            Graph graph = frw.readDotFile();
            List<ScheduledTask> schedule;
            PartialSolution solution;

            // Temp Conditions
            //graph.getNodeList().size() > 11
            //                    || (graph.getNodeList().size() >10 && graph.getNumOfProcessors() > 3)
            //                    || (graph.getNodeList().size() >9 && graph.getNumOfProcessors() > 6)
            if (false) {
                algorithm = new DFS(graph); //TODO remove graph parameter
            } else {
                // for small graphs, use the A* algorithm
                algorithm = new AStar(graph); //TODO remove graph parameter
            }

            if(cli.getCores()<2) {
                solution = algorithm.getOptimalSchedule(graph);
                if (solution == null) {
                    algorithm = new DFS(graph);
                    solution = algorithm.getOptimalSchedule(graph);
                }
            } else {
                solution = algorithm.getOptimalScheduleParallel(graph, cli.getCores()-1);
                if (solution == null) {
                    algorithm = new DFS(graph);
                    solution = algorithm.getOptimalScheduleParallel(graph, cli.getCores()-1);
                }
            }
            schedule = solution.fullSchedule(); // Returns list of Schedule

            frw.writeOutput(schedule);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}





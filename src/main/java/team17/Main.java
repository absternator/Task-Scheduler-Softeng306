package team17;

import team17.Algorithm.AlgorithmAStar;
import team17.Algorithm.ScheduledTask;
import team17.CLI.CLI;
import team17.CLI.FileReadWriter;
import team17.DAG.Graph;

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

        try {
            // "src/main/resources/graph.dot"
            Graph graph = frw.readDotFile();

            AlgorithmAStar aStar = new AlgorithmAStar(graph);
            List<ScheduledTask> schedule = aStar.getOptimalSchedule(); // Returns list of Schedule

            frw.writeOutput(schedule);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}





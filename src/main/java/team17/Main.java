package team17;

import team17.Algorithm.AlgorithmAStar;
import team17.Algorithm.ScheduledTask;
import team17.DAG.Graph;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class Main {

    public static void main(String[] args){
        try {
            readDotFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This reads dot file
     * @throws IOException Throw IO exception if file does not exist.
     */
    public static void readDotFile() throws IOException {
        Graph graph = new Graph();
        File file = new File("src/main/resources/graph2.dot");
        BufferedReader br = new BufferedReader(new FileReader(file));
        br.readLine();
        String line;
        while (!(line = br.readLine()).equals("}")) {
            graph.addGraph(line);
        }
        graph.addFinishNode();
        graph.setBottomLevel();
        graph.setNumOfProcessors(2);// Test : number of processors to run on
        System.out.println(graph);
        AlgorithmAStar aStar = new AlgorithmAStar(graph);
        List<ScheduledTask> schedule = aStar.getOptimalSchedule(); // Returns list of Schedule including "end"
        System.out.println(schedule);
    }

}





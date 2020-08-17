package team17;

import team17.Algorithm.ListScheduling;
import team17.CLI.CLI;
import team17.Algorithm.AlgorithmAStar;
import team17.Algorithm.ScheduledTask;
import team17.DAG.Graph;
import team17.DAG.Node;

import java.io.*;
import java.util.Collections;
import java.util.List;

public class Main {

    private static String _outputFileName;
    private static String _inputFileName = "";

    public static void main(String[] args) {
        //Run from command line
        //args = new String[]{"../../src/main/resources/graph.dot", "2"};

        //Run in IDE
        args = new String[]{"src/main/resources/testBandaid.dot", "2"};

        CLI cli = new CLI(args);
        _inputFileName = cli.getInput();
        _outputFileName = cli.getOutput();

        try {
            // "src/main/resources/graph.dot"
            Graph graph = readDotFile(_inputFileName);
            List<ScheduledTask> schedule;
            if (graph.getNodeList().size() < 10) {
                // for small graphs, use the A* algorithm
                AlgorithmAStar aStar = new AlgorithmAStar(graph);
                schedule = aStar.getOptimalSchedule(); // Returns list of Schedule

            } else {
                // for large graphs, use a band-aid solution (List scheduling)
                ListScheduling bandaid = new ListScheduling(graph);
                schedule = bandaid.getSchedule();
            }

            writeOutput(schedule);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This reads dot file
     *
     * @throws IOException Throw IO exception if file does not exist.
     */
    public static Graph readDotFile(String fileName) throws IOException {
        Graph graph = new Graph();
        File file = new File(fileName);
        BufferedReader br = new BufferedReader(new FileReader(file));
        br.readLine();
        String line;
        while (!(line = br.readLine()).equals("}")) {
            graph.addGraph(line);
        }
        graph.addFinishNode();
        graph.setBottomLevel();
        graph.setNumOfProcessors(3);// Test : number of processors to run on

        return graph;
    }

    /**
     * This function writes the graph to the dot file
     */
    private static void writeOutput(List<ScheduledTask> solution) throws IOException {
        String outputFileName = _outputFileName != null && !_outputFileName.isEmpty() ? _outputFileName : _inputFileName + "-output";
        Collections.sort(solution);

        File file = new File(outputFileName + ".dot");
        PrintWriter pw = new PrintWriter(new FileWriter(file));

        pw.println("digraph \"" + outputFileName + "\" {");
        for (ScheduledTask task : solution) {
            Node node = task.getNode();
            pw.printf("\t%-10s[Weight=%d,Start=%d,Processor=%d];\n", node.getId(), node.getWeight(), task.getStartTime(), task.getProcessorNum());
            if (!node.getDependencies().isEmpty()) {
                for (Node incoming : node.getDependencies()) {
                    String entry = incoming.getId() + " -> " + node.getId();
                    pw.printf("\t%-10s[Weight=%d];\n", entry, node.getIncomingEdges().get(incoming));
                }
            }
        }
        pw.println("}");
        pw.close();
    }

}





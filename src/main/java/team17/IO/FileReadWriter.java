package team17.IO;

import org.apache.commons.lang3.StringUtils;
import team17.Algorithm.ScheduledTask;
import team17.DAG.Graph;
import team17.DAG.Node;

import java.io.*;
import java.util.Collections;
import java.util.List;

/**
 * This class handles the reading in, parsing and writing to dot files
 */
public class FileReadWriter {

    private CLI _cli;
    private String _digraphName;

    public FileReadWriter(CLI cli) {
        _cli = cli;
    }

    /**
     * This method reads in the dot file to set up the graph object
     *
     * @throws IOException Throw IO exception if file does not exist.
     */
    public Graph readDotFile() throws IOException {
        Graph graph = new Graph();
        File file = new File(_cli.getInput());
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line = br.readLine();
        _digraphName = StringUtils.substringBetween(line,"\"");
        while (!(line = br.readLine()).equals("}")) {
            parse(graph,line);
        }
        graph.addFinishNode();
        graph.setBottomLevel();
        graph.setNumOfProcessors(_cli.getProcessors());// Test : number of processors to run on

        return graph;
    }

    /**
     * This parses the information in the input dot file to adds tasks to graph.
     * @param line Each line of the dot file is passed in.
     */
    public void parse(Graph graph, String line){
        String entity = StringUtils.deleteWhitespace(StringUtils.substringBefore(line,"["));
        String weight = StringUtils.substringBetween(line,"Weight=","]");

        if (!entity.contains("->")) { // this is adding a node
            graph.addNode(entity,Integer.parseInt(weight));
            //Assume node has been added in graph before adding edge
        } else {
            String[] nodes = entity.split("->");
            try {
                graph.addEdge(nodes[0],nodes[1],Integer.parseInt(weight));
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * This function writes the optimal schedule to the output dot file
     * @param solution the set of scheduled tasks in the final optimal solution
     */
    public void writeOutput(List<ScheduledTask> solution) throws IOException {
        String outputFileName = _cli.getOutput();

        //Puts the list in alphabetical order to match the input.
        Collections.sort(solution);

        File file = new File(outputFileName + ".dot");
        PrintWriter pw = new PrintWriter(new FileWriter(file));

        pw.println("digraph \"output" + StringUtils.capitalize(_digraphName) + "\" {");
        for (ScheduledTask task : solution) {
            Node node = task.getNode();
            pw.printf("\t%-10s[Weight=%d,Start=%d,Processor=%d];\n", node.getId(), node.getWeight(), task.getStartTime(), task.getProcessorNum());

            //Prints out the edges for the node using the dependencies
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

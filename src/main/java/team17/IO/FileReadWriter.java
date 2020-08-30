package team17.IO;

import org.apache.commons.lang3.StringUtils;
import team17.Algorithm.AlgorithmConfig;
import team17.Algorithm.ScheduledTask;
import team17.DAG.DAGGraph;
import team17.DAG.DAGNode;

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
    public DAGGraph readDotFile() throws IOException {
        DAGGraph graph = new DAGGraph();
        File file = new File(_cli.getInput());
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line = br.readLine();
        _digraphName = StringUtils.substringBetween(line,"\"");
        while (!(line = br.readLine()).equals("}")) {
            if (!StringUtils.isBlank(line)) {
                parse(graph,line);
            }
        }

        graph.initialise();

        AlgorithmConfig.setNumOfProcessors(_cli.getProcessors());

        return graph;
    }

    /**
     * This parses the information in the input dot file to adds tasks to graph.
     * @param line Each line of the dot file is passed in.
     */
    private void parse(DAGGraph graph, String line) throws IOException {
        String entity = StringUtils.deleteWhitespace(StringUtils.substringBefore(line,"["));
        String weight = StringUtils.substringBetween(line,"Weight=","]");

        if (weight == null) { // Not an entry for a node/edge
            return;
        } else if (entity.isEmpty() || weight.isEmpty()) { // Not a valid entry for the graph
            throw new IOException("Invalid graph entry: Entity = " + entity + ", Weight = " + weight );
        }

        if (!entity.contains("->")) { // this is adding a node
            graph.addNode(entity,Integer.parseInt(weight));
            // Assume node has been added in graph before adding edge
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
          // Puts the list in alphabetical order to match the input.
        Collections.sort(solution);

        File file = new File(_cli.getOutput());
        PrintWriter pw = new PrintWriter(new FileWriter(file));

        pw.println("digraph \"output" + StringUtils.capitalize(_digraphName) + "\" {");
        for (ScheduledTask task : solution) {
            DAGNode node = task.getNode();
            pw.printf("\t%-10s[Weight=%d,Start=%d,Processor=%d];\n", node.getId(), node.getWeight(), task.getStartTime(), task.getProcessorNum());

            // Prints out the edges for the node using the dependencies
            if (!node.getDependencies().isEmpty()) {
                for (DAGNode incoming : node.getDependencies()) {
                    String entry = incoming.getId() + " -> " + node.getId();
                    pw.printf("\t%-10s[Weight=%d];\n", entry, node.getIncomingEdges().get(incoming));
                }
            }
        }
        pw.println("}");
        pw.close();
    }
}

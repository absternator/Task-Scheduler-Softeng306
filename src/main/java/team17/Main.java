package team17;

import org.apache.commons.cli.*;
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
        int nProcessors;
        boolean visualise;
        int nCores;

        //Run from command line
        //args = new String[]{"../../src/main/resources/graph.dot", "2"};

        //Run in IDE
        args = new String[]{"src/main/resources/graph.dot", "2"};

        Options options = new Options();

        Option parallelCoresOpt = new Option("p", true, "use N cores for execution in parallel (default is sequential)");
        Option visualiseOpt = new Option("v", false, "visualise the search");
        Option outputOpt = new Option("o", true, "output file is named OUTPUT (default is INPUT−output.dot)");

        options.addOption(parallelCoresOpt);
        options.addOption(visualiseOpt);
        options.addOption(outputOpt);

        //Check first two args, then options
        if (args.length >= 2) {
            if (args[0].endsWith(".dot")) {
                _inputFileName = args[0];
            }
            nProcessors = Integer.parseInt(args[1]);
        }

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd;
        try {
            cmd = parser.parse(options, args);
            visualise = cmd.hasOption("v");
            // getOptionValue returns null if no argument
            if (cmd.getOptionValue("p") != null) {
                nCores = Integer.parseInt(cmd.getOptionValue("p"));
            }
            _outputFileName = cmd.getOptionValue("o");
            if (_outputFileName != null) {
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        try {
            // "src/main/resources/graph.dot"
            Graph graph = readDotFile(_inputFileName);

            AlgorithmAStar aStar = new AlgorithmAStar(graph);
            List<ScheduledTask> schedule = aStar.getOptimalSchedule(); // Returns list of Schedule

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
        graph.set_numOfProcessors(2);// Test : number of processors to run on

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





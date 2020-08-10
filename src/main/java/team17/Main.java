package team17;

import org.apache.commons.cli.*;
import team17.DAG.DagGraph;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class Main {

    public static void main(String[] args){
        String input = "";
        int nProcessors;
        boolean visualise;
        int nCores;
        String output;

        Options options = new Options();

        Option parallelCoresOpt = new Option("p", true, "use N cores for execution in parallel (default is sequential)");
        Option visualiseOpt = new Option("v", false, "visualise the search");
        Option outputOpt = new Option("o", true, "output file is named OUTPUT (default is INPUT−output.dot)");

        options.addOption(parallelCoresOpt);
        options.addOption(visualiseOpt);
        options.addOption(outputOpt);

        //Check first two args, then options
        if(args.length>=2) {
            if(args[0].endsWith(".dot")) {
                input = args[0];
            }
            nProcessors = Integer.parseInt(args[1]);
            System.out.println("Number of processors: " + nProcessors + " File name: " + input);
        }

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd;
        try {
            cmd = parser.parse(options, args);
            visualise = cmd.hasOption("v");
            String s = "V: " + visualise;
            // getOptionValue returns null if no argument
            if(cmd.getOptionValue("p")!=null){
                nCores = Integer.parseInt(cmd.getOptionValue("p"));
                s += " P:" + nCores;
            }
            output = cmd.getOptionValue("o");
            if(output!=null){
                s += " O: " + output;
            }
            System.out.println(s);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        try {
            // "src/main/resources/graph.dot"
            readDotFile(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
       This reads the graph from the dot file
     */
    public static void readDotFile (String fileName) throws IOException {
        DagGraph graph = new DagGraph();
        HashMap<String,Integer> nodeCounter = new HashMap<>(); //stores nodes passed into graph
        File file = new File(fileName);
        BufferedReader br = new BufferedReader(new FileReader(file));
        br.readLine();
        String line;
        while (!(line = br.readLine()).equals("}")) {
            graph.addGraph(line,nodeCounter);
        }
        System.out.println(graph);
    }

}





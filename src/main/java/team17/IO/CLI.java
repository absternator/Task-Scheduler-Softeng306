package team17.IO;

import org.apache.commons.cli.*;
import org.apache.commons.lang3.StringUtils;

/**
 * This class contains the command line configuration parsing logic
 */
public class CLI {
    String _input = "";
    int _nProcessors;
    boolean _visualise;
    int _nCores;
    String _output;

    public CLI(String[] args) {

        Options options = new Options();

        Option parallelCoresOpt = new Option("p", true, "use N cores for execution in parallel (default is sequential)");
        Option visualiseOpt = new Option("v", false, "visualise the search");
        Option outputOpt = new Option("o", true, "output file is named OUTPUT (default is INPUT-output.dot)");

        parallelCoresOpt.setArgName("N");
        outputOpt.setArgName("OUTPUT");

        options.addOption(parallelCoresOpt);
        options.addOption(visualiseOpt);
        options.addOption(outputOpt);

        HelpFormatter formatter = new HelpFormatter();

        String header = "Parameters:\n <INPUT.dot>   a task graph with integer weights in dot format\n" +
                " <P>           number of processors to schedule the INPUT graph on\nOptional:";

        String footer = "Please enter the appropriate parameters to start the scheduler";

        // Check first two args, then optional arguments
        if(args.length>=2) {
            if(args[0].endsWith(".dot")) {
                _input = args[0];
            } else {
                formatter.printHelp("scheduler", header, options, footer);
                System.exit(1);
            }
            _nProcessors = Integer.parseInt(args[1]);
        } else {
            formatter.printHelp("scheduler", header, options, footer);
            System.exit(1);
        }

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd;
        try {
            cmd = parser.parse(options, args);
            _visualise = cmd.hasOption("v");
            // getOptionValue returns 0 if no argument
            if(cmd.getOptionValue("p")!=null) {
                _nCores = Integer.parseInt(cmd.getOptionValue("p"));
            }
            _output = cmd.getOptionValue("o");
        } catch (ParseException e) {
            e.printStackTrace();
            formatter.printHelp("scheduler", header, options, footer);
            System.exit(1);
        }
    }

    /**
     * This method is a get method for the input dot file
     * @return a string of the file name
     */
    public String getInput() {
        return _input;
    }

    /**
     * This method is a get method for the output name specified
     * @return a string of the output file name, null if default
     */
    public String getOutput() {
        String fileName = StringUtils.isBlank(_output) ? StringUtils.substringBefore(_input, ".dot") + "-output" : _output;
        return fileName +".dot";
    }

    /**
     * This method is a get method for the number of processors
     * @return an integer of the number of processors
     */
    public int getProcessors() {
        return _nProcessors;
    }

    /**
     * This method is a get method for whether or not the algorithm is visualised
     * @return a boolean value true if it should be visualised and false otherwise
     */
    public boolean getVisualise() {
        return _visualise;
    }

    /**
     * This method is a get method for the number of cores to execute in parallel
     * @return an integer of the number of cores, 0 if sequential
     */
    public int getCores() {
        return _nCores;
    }

}

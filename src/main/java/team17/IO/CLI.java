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
    Options _options;
    HelpFormatter _formatter;
    String _cmdLineSyntax;
    String _header;
    String _footer;

    public CLI() {

        _cmdLineSyntax = "java -jar scheduler .jar INPUT.dot P [OPTION]";

        _options = new Options();

        Option parallelCoresOpt = new Option("p", true, "use N cores for execution in parallel (default is sequential)");
        Option visualiseOpt = new Option("v", false, "visualise the search");
        Option outputOpt = new Option("o", true, "output file is named OUTPUT (default is INPUT-output.dot)");

        parallelCoresOpt.setArgName("N");
        outputOpt.setArgName("OUTPUT");

        _options.addOption(parallelCoresOpt);
        _options.addOption(visualiseOpt);
        _options.addOption(outputOpt);

        _formatter = new HelpFormatter();

        _header = "Parameters:\n <INPUT.dot>   a task graph with integer weights in dot format\n" +
                " <P>           number of processors to schedule the INPUT graph on\nOptional:";

        _footer = "Please enter the appropriate parameters to start the scheduler";
    }

    /**
     * Reads the command line inputs and sets the fields
     *
     * @throws IncorrectCLIInputException When inappropriate inputs are detected
     */
    public void readCLI(String[] args) throws IncorrectCLIInputException {
        // Check first two args, then optional arguments
        if(args.length>=2) {
            // Check INPUT.dot ends in dot file extension
            if(args[0].endsWith(".dot")) {
                _input = args[0];
            } else {
                throw new IncorrectCLIInputException("Expected first parameter: INPUT.dot");
            }
            // Check if P is not an integer
            try{
                _nProcessors = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                throw new IncorrectCLIInputException("Expected second parameter: integer P");
            }

            // Check if number of processors is greater than 0
            if(_nProcessors < 1) {
                throw new IncorrectCLIInputException("Expected second parameter: integer P where P > 0");
            }
        } else {
            throw new IncorrectCLIInputException("Expected two parameters: INPUT.dot, P");
        }

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd;
        try {
            cmd = parser.parse(_options, args);
            _visualise = cmd.hasOption("v");
            // getOptionValue returns 0 if no argument
            if(cmd.getOptionValue("p")!=null) {
                // Check if N is not an integer
                _nCores = Integer.parseInt(cmd.getOptionValue("p"));

                // Check if number of cores is greater than 0
                if(_nCores < 1) {
                    throw new IncorrectCLIInputException("Expected option: integer N where N > 0");
                }
            }
            _output = cmd.getOptionValue("o");
        } catch (ParseException e) {
            e.printStackTrace();
            printHelpFormatter();
            System.exit(1);
        } catch (NumberFormatException e) {
            throw new IncorrectCLIInputException("Expected option: integer N");
        }
    }

    /**
     * This method prints out the help
     */
    public void printHelpFormatter() {
        _formatter.printHelp(_cmdLineSyntax, _header, _options, _footer);
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

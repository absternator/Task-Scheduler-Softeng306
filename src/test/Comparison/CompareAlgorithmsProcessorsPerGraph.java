import org.junit.Assume;
import org.junit.BeforeClass;
import org.junit.Test;
import team17.Algorithm.AStar;
import team17.Algorithm.AlgorithmState;
import team17.Algorithm.DFS;
import team17.DAG.DAGGraph;
import team17.DAG.InvalidGraphException;
import team17.IO.CLI;
import team17.IO.FileReadWriter;
import team17.IO.IncorrectCLIInputException;
import team17.IO.InvalidEntryException;

import java.io.IOException;

public class CompareAlgorithmsProcessorsPerGraph {
    // ******************** SET TO TRUE IF YOU WANNA RUN THIS ***********************
    private static boolean _runComparisons = false;
    // ******************************************************************************

    private String[] _args;
    private static final String  _srcGraphDot = "src/main/resources/graph.dot";
    private final int _timeout = 60000 * 15; // 15 minutes

    @BeforeClass
    public static void checkIfRun() {
        Assume.assumeTrue(_runComparisons);
    }

    @Test (timeout = _timeout)
    public void compareGraph_1P() throws IOException {

        _args = new String[]{_srcGraphDot, "1"};
        compareAlgorithmsFor(_args);
    }

    @Test (timeout = _timeout)
    public void compareGraph_2P() throws IOException {

        _args = new String[]{_srcGraphDot, "2"};
        compareAlgorithmsFor(_args);
    }


    @Test (timeout = _timeout)
    public void compareGraph_3P() throws IOException {

        _args = new String[]{_srcGraphDot, "3"};
        compareAlgorithmsFor(_args);
    }

    @Test (timeout = _timeout)
    public void compareGraph_4P() throws IOException {

        _args = new String[]{_srcGraphDot, "4"};
        compareAlgorithmsFor(_args);
    }

    @Test (timeout = _timeout)
    public void compareGraph_5() throws IOException {

        _args = new String[]{_srcGraphDot, "5"};
        compareAlgorithmsFor(_args);
    }

    @Test (timeout = _timeout)
    public void compareGraph_6P() throws IOException {

        _args = new String[]{_srcGraphDot, "6"};
        compareAlgorithmsFor(_args);
    }

    @Test (timeout = _timeout)
    public void compareGraph_7P() throws IOException {
        _args = new String[]{_srcGraphDot, "7"};
        compareAlgorithmsFor(_args);
    }

    @Test (timeout = _timeout)
    public void compareGraph_8P() throws IOException {

        _args = new String[]{_srcGraphDot, "8"};
        compareAlgorithmsFor(_args);
    }

     private void compareAlgorithmsFor(String[] args) throws IOException {
        CLI cli = new CLI();
         try {
             cli.readCLI(args);
         } catch (IncorrectCLIInputException e) {
             e.printStackTrace();
         }
         FileReadWriter frw = new FileReadWriter(cli);
         DAGGraph graph = null;
         try {
             graph = frw.readDotFile();
         } catch (InvalidGraphException e) {
             e.printStackTrace();
         } catch (InvalidEntryException e) {
             e.printStackTrace();
         }
         AStar aStar = new AStar(graph, new AlgorithmState());
        DFS dfs = new DFS(graph, new AlgorithmState());

        System.out.println("Starting test for: " + args[0] + " on " + args[1]);
        long start = System.nanoTime();
        aStar.getOptimalSchedule(graph);
        long end = System.nanoTime();
        long aTimeElapsed = end - start;
        System.out.println("\t\tA* time: " + aTimeElapsed + " ns");

        start = System.nanoTime();
        dfs.getOptimalSchedule(graph);
        end = System.nanoTime();
        long dfsTimeElapsed = end - start;
        System.out.println("\t\tDFS time: " + dfsTimeElapsed + " ns");
        String result = (dfsTimeElapsed < aTimeElapsed) ? "Dfs was faster" : "A* was faster";
        System.out.println(result + "\n");
    }

    public long timeAStar(String[] args) throws IOException {
        DAGGraph graph = readGraph(args);
        AStar aStar = new AStar(graph, new AlgorithmState());
        long start = System.nanoTime();
        aStar.getOptimalSchedule(graph);
        long end = System.nanoTime();
        long aTimeElapsed = end - start;
        System.out.println("\t\tA* time: " + aTimeElapsed + " ns");
        return aTimeElapsed;
    }

    public long timeDFS(String[] args) throws IOException {
        DAGGraph graph = readGraph(args);
        DFS dfs = new DFS(graph, new AlgorithmState());
        long start = System.nanoTime();
        dfs.getOptimalSchedule(graph);
        long end = System.nanoTime();
        long dfsTimeElapsed = end - start;
        System.out.println("\t\tDFS time: " + dfsTimeElapsed + " ns");
        return dfsTimeElapsed;
    }

    public DAGGraph readGraph(String[] args) throws IOException {
        System.out.println("Starting test for: " + args[0] + " on " + args[1]);
        CLI cli = new CLI();
        try {
            cli.readCLI(args);
        } catch (IncorrectCLIInputException e) {
            e.printStackTrace();
        }
        FileReadWriter frw = new FileReadWriter(cli);
        DAGGraph graph = null;
        try {
            graph = frw.readDotFile();
        } catch (InvalidGraphException e) {
            e.printStackTrace();
        } catch (InvalidEntryException e) {
            e.printStackTrace();
        }
        return graph;
    }
}

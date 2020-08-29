import org.junit.Assume;
import org.junit.BeforeClass;
import org.junit.Test;
import sun.util.resources.cldr.sah.CalendarData_sah_RU;
import team17.Algorithm.AStar;
import team17.Algorithm.AlgorithmState;
import team17.Algorithm.DFS;
import team17.DAG.DAGGraph;
import team17.IO.CLI;
import team17.IO.FileReadWriter;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class CompareAlgorithms {
    // ******************** SET TO TRUE IF YOU WANNA RUN THIS ***********************
    private static boolean _runComparisons = true;
    private boolean _runSlowGraphs = false; // these graphs are pretty slow, run at own risk
    // ******************************************************************************

    private String[] _args;

    @BeforeClass
    public static void checkIfRun() {
        Assume.assumeTrue(_runComparisons);
    }

    @Test
    public void compareNodes7_2P() throws IOException {

        _args = new String[]{"src/main/resources/Nodes_7_OutTree.dot", "2"};
        compareAlgorithmsFor(_args);
    }

    @Test
    public void compareNodes7_4P() throws IOException {

        _args = new String[]{"src/main/resources/Nodes_7_OutTree.dot", "4"};
        compareAlgorithmsFor(_args);
    }

    @Test
    public void compareNodes8_2P() throws IOException {

        _args = new String[]{"src/main/resources/Nodes_8_Random.dot", "2"};
        compareAlgorithmsFor(_args);
    }

    @Test
    public void compareNodes8_4P() throws IOException {

        _args = new String[]{"src/main/resources/Nodes_8_Random.dot", "4"};
        compareAlgorithmsFor(_args);
    }

    @Test
    public void compareNodes9_2P() throws IOException {

        _args = new String[]{"src/main/resources/Nodes_9_SeriesParallel.dot", "2"};
        compareAlgorithmsFor(_args);
    }

    @Test
    public void compareNodes9_4P() throws IOException {

        _args = new String[]{"src/main/resources/Nodes_9_SeriesParallel.dot", "4"};
        compareAlgorithmsFor(_args);
    }

    @Test
    public void compareNodes10_2P() throws IOException {

        _args = new String[]{"src/main/resources/Nodes_10_Random.dot", "2"};
        compareAlgorithmsFor(_args);
    }

    @Test
    public void compareNodes10_4P() throws IOException {

        _args = new String[]{"src/main/resources/Nodes_10_Random.dot", "4"};
        compareAlgorithmsFor(_args);
    }

    @Test
    public void compareNodes11_2P() throws IOException {

        _args = new String[]{"src/main/resources/Nodes_11_OutTree.dot", "2"};
        compareAlgorithmsFor(_args);
    }

    @Test
    public void compareNodes11_4P() throws IOException {

        _args = new String[]{"src/main/resources/Nodes_11_OutTree.dot", "4"};
        compareAlgorithmsFor(_args);
    }

    @Test
    public void compareINPUT1_3P() throws IOException {
        Assume.assumeTrue(_runSlowGraphs);
        _args = new String[]{"src/main/resources/INPUT1.dot", "3"};
        compareAlgorithmsFor(_args);
    }

    private void compareAlgorithmsFor(String[] args) throws IOException {
        CLI cli = new CLI(args);
        FileReadWriter frw = new FileReadWriter(cli);
        DAGGraph graph = frw.readDotFile();
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
}

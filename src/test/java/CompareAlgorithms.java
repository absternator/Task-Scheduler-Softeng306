import org.junit.Assume;
import org.junit.BeforeClass;
import org.junit.Test;
import team17.Algorithm.AStar;
import team17.Algorithm.AlgorithmState;
import team17.Algorithm.DFS;
import team17.DAG.DAGGraph;
import team17.IO.CLI;
import team17.IO.FileReadWriter;

import java.io.IOException;

public class CompareAlgorithms {
    // ******************** SET TO TRUE IF YOU WANNA RUN THIS ***********************
    private static boolean _runComparisons = true;
    private boolean _runSlowGraphs = true; // these graphs are pretty slow, run at own risk
    // ******************************************************************************

    private String[] _args;
    private final int _timeout = 60000 * 15; // 35 minutes

    @BeforeClass
    public static void checkIfRun() {
        Assume.assumeTrue(_runComparisons);
    }

    //    @Test
//    public void compareNodes7_2P() throws IOException {
//
//        _args = new String[]{"src/main/resources/Nodes_7_OutTree.dot", "2"};
//        compareAlgorithmsFor(_args);
//    }
//
//    @Test
//    public void compareNodes7_4P() throws IOException {
//
//        _args = new String[]{"src/main/resources/Nodes_7_OutTree.dot", "4"};
//        compareAlgorithmsFor(_args);
//    }
//
//    @Test
//    public void compareNodes8_2P() throws IOException {
//
//        _args = new String[]{"src/main/resources/Nodes_8_Random.dot", "2"};
//        compareAlgorithmsFor(_args);
//    }
//
//    @Test
//    public void compareNodes8_4P() throws IOException {
//
//        _args = new String[]{"src/main/resources/Nodes_8_Random.dot", "4"};
//        compareAlgorithmsFor(_args);
//    }
//
//    @Test
//    public void compareNodes9_2P() throws IOException {
//
//        _args = new String[]{"src/main/resources/Nodes_9_SeriesParallel.dot", "2"};
//        compareAlgorithmsFor(_args);
//    }
//
//    @Test
//    public void compareNodes9_4P() throws IOException {
//
//        _args = new String[]{"src/main/resources/Nodes_9_SeriesParallel.dot", "4"};
//        compareAlgorithmsFor(_args);
//    }
//
//    @Test
//    public void compareNodes10_2P() throws IOException {
//
//        _args = new String[]{"src/main/resources/Nodes_10_Random.dot", "2"};
//        compareAlgorithmsFor(_args);
//    }
//
//    @Test
//    public void compareNodes10_4P() throws IOException {
//
//        _args = new String[]{"src/main/resources/Nodes_10_Random.dot", "4"};
//        compareAlgorithmsFor(_args);
//    }
//
//    @Test
//    public void compareNodes11_2P() throws IOException {
//
//        _args = new String[]{"src/main/resources/Nodes_11_OutTree.dot", "2"};
//        compareAlgorithmsFor(_args);
//    }
//
//    @Test
//    public void compareNodes11_4P() throws IOException {
//
//        _args = new String[]{"src/main/resources/Nodes_11_OutTree.dot", "4"};
//        compareAlgorithmsFor(_args);
//    }
//
//    @Test
//    public void compareINPUT1_3P() throws IOException {
//        Assume.assumeTrue(_runSlowGraphs);
//        _args = new String[]{"src/main/resources/INPUT1.dot", "3"};
//        compareAlgorithmsFor(_args);
//    }
//
//    @Test
//    public void compareINPUT0_2P() throws IOException {
//        _args = new String[]{"src/main/resources/INPUT0.dot", "2"};
//        compareAlgorithmsFor(_args);
//    }
//    @Test
//    public void compareINPUT2_3P() throws IOException {
//        Assume.assumeTrue(_runSlowGraphs);
//        _args = new String[]{"src/main/resources/INPUT2.dot", "3"};
//        compareAlgorithmsFor(_args);
//    }
    @Test (timeout = _timeout)
    public void timeInput1_3pA() throws IOException {
        Assume.assumeTrue(_runSlowGraphs);
        _args = new String[]{"src/main/resources/INPUT1.dot", "8"};
        timeAStar(_args);
    }
    @Test (timeout = _timeout)
    public void timeInput1_3pDFS() throws IOException {
        Assume.assumeTrue(_runSlowGraphs);
        _args = new String[]{"src/main/resources/INPUT1.dot", "8"};
        timeDFS(_args);
    }

    @Test (timeout = _timeout)
    public void timeInput2_3pA() throws IOException {
        Assume.assumeTrue(_runSlowGraphs);
        _args = new String[]{"src/main/resources/INPUT2.dot", "3"};
        timeAStar(_args);
    }
    @Test (timeout = _timeout)
    public void timeInput2_3pDFS() throws IOException {
        Assume.assumeTrue(_runSlowGraphs);
        _args = new String[]{"src/main/resources/INPUT2.dot", "3"};
        timeDFS(_args);
    }

//    @Test (timeout = _timeout)
//    public void timeInput0_2pA() throws IOException {
//        _args = new String[]{"src/main/resources/INPUT0.dot", "2"};
//        timeAStar(_args);
//    }
//    @Test (timeout = _timeout)
//    public void timeInput0_2pDFS() throws IOException {
//        _args = new String[]{"src/main/resources/INPUT0.dot", "2"};
//        timeDFS(_args);
//    }

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
        CLI cli = new CLI(args);
        FileReadWriter frw = new FileReadWriter(cli);
        DAGGraph graph = frw.readDotFile();
        return graph;
    }
}

import org.junit.Assume;
import org.junit.BeforeClass;
import org.junit.Test;
import team17.Algorithm.AlgorithmState;
import team17.Algorithm.DFS;
import team17.Algorithm.PartialSolution;
import team17.DAG.DAGGraph;
import team17.IO.CLI;
import team17.IO.FileReadWriter;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class testDfsOnExampleGraphs {

    // *** SET TO TRUE IF YOU WANT TO RUN THESE TESTS ***
    private static boolean _RunThisTestSuite = true;
    // **************************************************

    private String[] _args;
    private PartialSolution _solution;

    @BeforeClass
    public static void setUpBeforeAll() {
        Assume.assumeTrue(_RunThisTestSuite);
    }

    @Test
    public void testNodes7OutTree_2P() throws IOException {

        _args = new String[]{"src/main/resources/Nodes_7_OutTree.dot", "2"};

        assertEquals(28, getDfsSolutionFor(_args));
    }

    @Test
    public void testNodes7OutTree_4P() throws IOException {

        _args = new String[]{"src/main/resources/Nodes_7_OutTree.dot", "4"};

        assertEquals(22, getDfsSolutionFor(_args));
    }

    @Test
    public void testNodes8Random_2P() throws IOException {
        _args = new String[]{"src/main/resources/Nodes_8_Random.dot", "2"};
        assertEquals(581, getDfsSolutionFor(_args));
    }

    @Test
    public void testNodes8Random_4P() throws IOException {
        _args = new String[]{"src/main/resources/Nodes_8_Random.dot", "4"};
        assertEquals(581, getDfsSolutionFor(_args));
    }

    @Test
    public void testNodes9SeriesParallel_2P() throws IOException {
        _args = new String[]{"src/main/resources/Nodes_9_SeriesParallel.dot", "2"};
        assertEquals(55, getDfsSolutionFor(_args));
    }

    @Test
    public void testNodes9SeriesParallel_4P() throws IOException {
        _args = new String[]{"src/main/resources/Nodes_9_SeriesParallel.dot", "4"};
        assertEquals(55, getDfsSolutionFor(_args));
    }

    @Test
    public void testNodes10Random_2P() throws IOException {
        _args = new String[]{"src/main/resources/Nodes_10_Random.dot", "2"};
        assertEquals(50, getDfsSolutionFor(_args));
    }

    @Test
    public void testNodes10Random_4P() throws IOException {
        _args = new String[]{"src/main/resources/Nodes_10_Random.dot", "4"};
        assertEquals(50, getDfsSolutionFor(_args));
    }

    @Test
    public void testNodes11OutTree_2P() throws IOException {
        _args = new String[]{"src/main/resources/Nodes_11_OutTree.dot", "2"};
        assertEquals(350, getDfsSolutionFor(_args));
    }

    @Test
    public void testNodes11OutTree_4P() throws IOException {
        _args = new String[]{"src/main/resources/Nodes_11_OutTree.dot", "4"};
        assertEquals(227, getDfsSolutionFor(_args));
    }

    @Test
    public void testInput1_2P() throws IOException {
        _args = new String[]{"src/main/resources/INPUT1.dot", "2"};
        assertEquals(469, getDfsSolutionFor(_args));
    }

    @Test
    public void testInput0_2P() throws IOException {
        _args = new String[]{"src/main/resources/INPUT0.dot", "2"};
        assertEquals(5644, getDfsSolutionFor(_args));
    }
    @Test
    public void testInput2_3P() throws IOException {
        _args = new String[]{"src/main/resources/INPUT2.dot", "3"};
        assertEquals(469, getDfsSolutionFor(_args));
    }

    @Test
    public void testGraph4_8P() throws IOException {
        _args = new String[]{"src/main/resources/Graph4.dot", "3"};
        assertEquals(5644, getDfsSolutionFor(_args));
    }

    private int getDfsSolutionFor(String[] args) throws IOException {
        CLI cli = new CLI(args);
        FileReadWriter frw = new FileReadWriter(cli);
        DAGGraph graph = frw.readDotFile();
        DFS dfs = new DFS(graph,new AlgorithmState());
        return dfs.getOptimalSchedule(graph).getScheduledTask().getFinishTime();
    }
}
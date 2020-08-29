import org.junit.Assume;
import org.junit.BeforeClass;
import org.junit.Test;
import team17.Algorithm.AStar;
import team17.Algorithm.AlgorithmState;
import team17.DAG.DAGGraph;
import team17.IO.CLI;
import team17.IO.FileReadWriter;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class testAStarOnExampleGraphs {

    // *** SET TO TRUE IF YOU WANT TO RUN THESE TESTS ***
    private static boolean _RunThisTestSuite = true;

    private boolean _runNodes10Random = false; // TODO Parse Nodes10
    private boolean _runNodes11OutTree = true; // May take a long time or run out of memory
    // **************************************************

    private String[] _args;

    @BeforeClass
    public static void setUpBeforeAll() {
        Assume.assumeTrue(_RunThisTestSuite);
    }

    @Test
    public void testNodes7OutTree_2P() throws IOException {

        _args = new String[]{"src/main/resources/Nodes_7_OutTree.dot", "2"};

        assertEquals(28,getAStarSolutionFor(_args));
    }

    @Test
    public void testNodes7OutTree_4P() throws IOException {

        _args = new String[]{"src/main/resources/Nodes_7_OutTree.dot", "4"};

        assertEquals(22,getAStarSolutionFor(_args));
    }

    @Test
    public void testNodes8Random_2P() throws IOException {

        _args = new String[]{"src/main/resources/Nodes_8_Random.dot", "2"};

        assertEquals(581,getAStarSolutionFor(_args));
    }

    @Test
    public void testNodes8Random_4P() throws IOException {

        _args = new String[]{"src/main/resources/Nodes_8_Random.dot", "4"};

        assertEquals(581,getAStarSolutionFor(_args));
    }

    @Test
    public void testNodes9SeriesParallel_2P() throws IOException {

        _args = new String[]{"src/main/resources/Nodes_9_SeriesParallel.dot", "2"};

        assertEquals(55, getAStarSolutionFor(_args));
    }

    @Test
    public void testNodes9SeriesParallel_4P() throws IOException {

        _args = new String[]{"src/main/resources/Nodes_9_SeriesParallel.dot", "4"};

        assertEquals(55, getAStarSolutionFor(_args));
    }

    @Test
    public void testNodes10Random_2P() throws IOException {
        Assume.assumeTrue(_runNodes10Random);
        _args = new String[]{"src/main/resources/Nodes_10_Random.dot", "2"};

        assertEquals(50, getAStarSolutionFor(_args));
    }

    @Test
    public void testNodes10Random_4P() throws IOException {
        Assume.assumeTrue(_runNodes10Random);

        _args = new String[]{"src/main/resources/Nodes_10_Random.dot", "4"};


        assertEquals(50, getAStarSolutionFor(_args));
    }

    @Test
    public void testNodes11OutTree_2P() throws IOException {
        Assume.assumeTrue(_runNodes11OutTree);
        _args = new String[]{"src/main/resources/Nodes_11_OutTree.dot", "2"};

        assertEquals(350, getAStarSolutionFor(_args));
    }

    @Test
    public void testNodes11OutTree_4P() throws IOException {
        Assume.assumeTrue(_runNodes11OutTree);

        _args = new String[]{"src/main/resources/Nodes_11_OutTree.dot", "4"};

        assertEquals(227, getAStarSolutionFor(_args));
    }

    private int getAStarSolutionFor(String[] args) throws IOException {
        CLI cli = new CLI(args);
        FileReadWriter frw = new FileReadWriter(cli);
        DAGGraph graph = frw.readDotFile();
        AStar aStar = new AStar(graph, new AlgorithmState());
        return aStar.getOptimalSchedule(graph).getScheduledTask().getStartTime();
    }
}

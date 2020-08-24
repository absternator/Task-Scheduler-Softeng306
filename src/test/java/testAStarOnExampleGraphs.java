import org.junit.Assume;
import org.junit.BeforeClass;
import org.junit.Test;
import team17.Algorithm.AStar;
import team17.Algorithm.DFS;
import team17.Algorithm.PartialSolution;
import team17.DAG.Graph;
import team17.IO.CLI;
import team17.IO.FileReadWriter;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class testAStarOnExampleGraphs {

    // *** SET TO TRUE IF YOU WANT TO RUN THESE TESTS ***
    private static boolean _RunThisTestSuite = true;

    private boolean _runNodes10Random = false; // TODO Parse Nodes10
    private boolean _runNodes11OutTree = true; // May take a long time or run out of memory
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
        _solution = getAStarSolutionFor(_args);

        assertEquals(28, _solution.getEndTime());
    }

    @Test
    public void testNodes7OutTree_4P() throws IOException {

        _args = new String[]{"src/main/resources/Nodes_7_OutTree.dot", "4"};
        _solution = getAStarSolutionFor(_args);

        assertEquals(22, _solution.getEndTime());
    }

    @Test
    public void testNodes8Random_2P() throws IOException {

        _args = new String[]{"src/main/resources/Nodes_8_Random.dot", "2"};
        _solution = getAStarSolutionFor(_args);

        assertEquals(581, _solution.getEndTime());
    }

    @Test
    public void testNodes8Random_4P() throws IOException {

        _args = new String[]{"src/main/resources/Nodes_8_Random.dot", "4"};
        _solution = getAStarSolutionFor(_args);

        assertEquals(581, _solution.getEndTime());
    }

    @Test
    public void testNodes9SeriesParallel_2P() throws IOException {

        _args = new String[]{"src/main/resources/Nodes_9_SeriesParallel.dot", "2"};
        _solution = getAStarSolutionFor(_args);

        assertEquals(55, _solution.getEndTime());
    }

    @Test
    public void testNodes9SeriesParallel_4P() throws IOException {

        _args = new String[]{"src/main/resources/Nodes_9_SeriesParallel.dot", "4"};
        _solution = getAStarSolutionFor(_args);

        assertEquals(55, _solution.getEndTime());
    }

    @Test
    public void testNodes10Random_2P() throws IOException {
        Assume.assumeTrue(_runNodes10Random);
        _args = new String[]{"src/main/resources/Nodes_10_Random.dot", "2"};
        _solution = getAStarSolutionFor(_args);

        assertEquals(50, _solution.getEndTime());
    }

    @Test
    public void testNodes10Random_4P() throws IOException {
        Assume.assumeTrue(_runNodes10Random);

        _args = new String[]{"src/main/resources/Nodes_10_Random.dot", "4"};
        _solution = getAStarSolutionFor(_args);

        assertEquals(50, _solution.getEndTime());
    }

    @Test
    public void testNodes11OutTree_2P() throws IOException {
        Assume.assumeTrue(_runNodes11OutTree);
        _args = new String[]{"src/main/resources/Nodes_11_OutTree.dot", "2"};
        _solution = getAStarSolutionFor(_args);

        assertEquals(350, _solution.getEndTime());
    }

    @Test
    public void testNodes11OutTree_4P() throws IOException {
        Assume.assumeTrue(_runNodes11OutTree);

        _args = new String[]{"src/main/resources/Nodes_11_OutTree.dot", "4"};
        _solution = getAStarSolutionFor(_args);

        assertEquals(227, _solution.getEndTime());
    }

    private PartialSolution getAStarSolutionFor(String[] args) throws IOException {
        CLI cli = new CLI(args);
        FileReadWriter frw = new FileReadWriter(cli);
        Graph graph = frw.readDotFile();
        AStar aStar = new AStar(graph);
        return aStar.getOptimalSchedule(graph);
    }
}

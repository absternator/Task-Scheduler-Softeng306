import org.junit.Assume;
import org.junit.BeforeClass;
import org.junit.Test;
import team17.Algorithm.DFS;
import team17.DAG.Graph;
import team17.IO.CLI;
import team17.IO.FileReadWriter;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class testParallelDFSOnExampleGraphs {

    // *** SET TO TRUE IF YOU WANT TO RUN THESE TESTS ***
    private static boolean _RunThisTestSuite = true;

    private boolean _runNodes10Random = true; // TODO Parse Nodes10
    private boolean _runNodes11OutTree = true; // May take a long time or run out of memory
    // **************************************************

    private String[] _args;

    @BeforeClass
    public static void setUpBeforeAll() {
        Assume.assumeTrue(_RunThisTestSuite);
    }

    @Test
    public void testNodes7OutTree_2P_2C() throws IOException {

        _args = new String[]{"src/main/resources/Nodes_7_OutTree.dot", "2", "-p", "2"};

        assertEquals(28, getDFSSolutionFor(_args));
    }

    @Test
    public void testNodes7OutTree_4P_2C() throws IOException {

        _args = new String[]{"src/main/resources/Nodes_7_OutTree.dot", "4", "-p", "2"};

        assertEquals(22, getDFSSolutionFor(_args));
    }

    @Test
    public void testNodes7OutTree_2P_4C() throws IOException {

        _args = new String[]{"src/main/resources/Nodes_7_OutTree.dot", "2", "-p", "4"};

        assertEquals(28, getDFSSolutionFor(_args));
    }

    @Test
    public void testNodes7OutTree_4P_4C() throws IOException {

        _args = new String[]{"src/main/resources/Nodes_7_OutTree.dot", "4", "-p", "4"};

        assertEquals(22, getDFSSolutionFor(_args));
    }

    @Test
    public void testNodes8Random_2P_2C() throws IOException {

        _args = new String[]{"src/main/resources/Nodes_8_Random.dot", "2", "-p", "2"};

        assertEquals(581, getDFSSolutionFor(_args));
    }

    @Test
    public void testNodes8Random_4P_2C() throws IOException {

        _args = new String[]{"src/main/resources/Nodes_8_Random.dot", "4", "-p", "2"};

        assertEquals(581, getDFSSolutionFor(_args));
    }

    @Test
    public void testNodes8Random_2P_4C() throws IOException {

        _args = new String[]{"src/main/resources/Nodes_8_Random.dot", "2", "-p", "4"};

        assertEquals(581, getDFSSolutionFor(_args));
    }

    @Test
    public void testNodes8Random_4P_4C() throws IOException {

        _args = new String[]{"src/main/resources/Nodes_8_Random.dot", "4", "-p", "4"};

        assertEquals(581, getDFSSolutionFor(_args));
    }

    @Test
    public void testNodes9SeriesParallel_2P_2C() throws IOException {

        _args = new String[]{"src/main/resources/Nodes_9_SeriesParallel.dot", "2", "-p", "2"};

        assertEquals(55, getDFSSolutionFor(_args));
    }

    @Test
    public void testNodes9SeriesParallel_4P_2C() throws IOException {

        _args = new String[]{"src/main/resources/Nodes_9_SeriesParallel.dot", "4", "-p", "2"};

        assertEquals(55, getDFSSolutionFor(_args));
    }

    @Test
    public void testNodes9SeriesParallel_2P_4C() throws IOException {

        _args = new String[]{"src/main/resources/Nodes_9_SeriesParallel.dot", "2", "-p", "4"};

        assertEquals(55, getDFSSolutionFor(_args));
    }

    @Test
    public void testNodes9SeriesParallel_4P_4C() throws IOException {

        _args = new String[]{"src/main/resources/Nodes_9_SeriesParallel.dot", "4", "-p", "4"};

        assertEquals(55, getDFSSolutionFor(_args));
    }

    @Test
    public void testNodes10Random_2P_2C() throws IOException {
        Assume.assumeTrue(_runNodes10Random);
        _args = new String[]{"src/main/resources/Nodes_10_Random.dot", "2", "-p", "2"};


        assertEquals(50, getDFSSolutionFor(_args));
    }

    @Test
    public void testNodes10Random_4P_2C() throws IOException {
        Assume.assumeTrue(_runNodes10Random);

        _args = new String[]{"src/main/resources/Nodes_10_Random.dot", "4", "-p", "2"};


        assertEquals(50, getDFSSolutionFor(_args));
    }

    @Test
    public void testNodes10Random_2P_4C() throws IOException {
        Assume.assumeTrue(_runNodes10Random);
        _args = new String[]{"src/main/resources/Nodes_10_Random.dot", "2", "-p", "4"};


        assertEquals(50, getDFSSolutionFor(_args));
    }

    @Test
    public void testNodes10Random_4P_4C() throws IOException {
        Assume.assumeTrue(_runNodes10Random);

        _args = new String[]{"src/main/resources/Nodes_10_Random.dot", "4", "-p", "4"};


        assertEquals(50, getDFSSolutionFor(_args));
    }

    @Test
    public void testNodes11OutTree_2P_2C() throws IOException {
        Assume.assumeTrue(_runNodes11OutTree);
        _args = new String[]{"src/main/resources/Nodes_11_OutTree.dot", "2", "-p", "2"};

        assertEquals(350, getDFSSolutionFor(_args));
    }

    @Test
    public void testNodes11OutTree_4P_2C() throws IOException {
        Assume.assumeTrue(_runNodes11OutTree);

        _args = new String[]{"src/main/resources/Nodes_11_OutTree.dot", "4", "-p", "2"};

        assertEquals(227, getDFSSolutionFor(_args));
    }

    @Test
    public void testNodes11OutTree_2P_4C() throws IOException {
        Assume.assumeTrue(_runNodes11OutTree);
        _args = new String[]{"src/main/resources/Nodes_11_OutTree.dot", "2", "-p", "4"};

        assertEquals(350, getDFSSolutionFor(_args));
    }

    @Test
    public void testNodes11OutTree_4P_4C() throws IOException {
        Assume.assumeTrue(_runNodes11OutTree);

        _args = new String[]{"src/main/resources/Nodes_11_OutTree.dot", "4", "-p", "4"};

        assertEquals(227, getDFSSolutionFor(_args));
    }

    private int getDFSSolutionFor(String[] args) throws IOException {
        CLI cli = new CLI(args);
        FileReadWriter frw = new FileReadWriter(cli);
        Graph graph = frw.readDotFile();
        DFS dfs = new DFS(graph);
        return dfs.getOptimalSchedule(graph).getScheduledTask().getStartTime();
    }
}
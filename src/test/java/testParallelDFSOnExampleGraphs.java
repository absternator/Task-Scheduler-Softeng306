import org.junit.Assume;
import org.junit.BeforeClass;
import org.junit.Test;
import team17.Algorithm.DFS;
import team17.DAG.DAGGraph;
import team17.DAG.InvalidGraphException;
import team17.IO.CLI;
import team17.IO.FileReadWriter;
import team17.IO.IncorrectCLIInputException;
import team17.IO.InvalidEntryException;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class testParallelDFSOnExampleGraphs {

    // *** SET TO TRUE IF YOU WANT TO RUN THESE TESTS ***
    private static boolean _RunThisTestSuite = false;

    // **************************************************

    private String[] _args;

    @BeforeClass
    public static void setUpBeforeAll() {
        Assume.assumeTrue(_RunThisTestSuite);
    }

    @Test
    public void testNodes7OutTree_2P_2C() throws IOException {

        _args = new String[]{"src/test/resources/Nodes_7_OutTree.dot", "2", "-p", "2"};

        assertEquals(28, getDFSSolutionFor(_args));
    }

    @Test
    public void testNodes7OutTree_4P_2C() throws IOException {

        _args = new String[]{"src/test/resources/Nodes_7_OutTree.dot", "4", "-p", "2"};

        assertEquals(22, getDFSSolutionFor(_args));
    }

    @Test
    public void testNodes7OutTree_2P_4C() throws IOException {

        _args = new String[]{"src/test/resources/Nodes_7_OutTree.dot", "2", "-p", "4"};

        assertEquals(28, getDFSSolutionFor(_args));
    }

    @Test
    public void testNodes7OutTree_4P_4C() throws IOException {

        _args = new String[]{"src/test/resources/Nodes_7_OutTree.dot", "4", "-p", "4"};

        assertEquals(22, getDFSSolutionFor(_args));
    }

    @Test
    public void testNodes8Random_2P_2C() throws IOException {

        _args = new String[]{"src/test/resources/Nodes_8_Random.dot", "2", "-p", "2"};

        assertEquals(581, getDFSSolutionFor(_args));
    }

    @Test
    public void testNodes8Random_4P_2C() throws IOException {

        _args = new String[]{"src/test/resources/Nodes_8_Random.dot", "4", "-p", "2"};

        assertEquals(581, getDFSSolutionFor(_args));
    }

    @Test
    public void testNodes8Random_2P_4C() throws IOException {

        _args = new String[]{"src/test/resources/Nodes_8_Random.dot", "2", "-p", "4"};

        assertEquals(581, getDFSSolutionFor(_args));
    }

    @Test
    public void testNodes8Random_4P_4C() throws IOException {

        _args = new String[]{"src/test/resources/Nodes_8_Random.dot", "4", "-p", "4"};

        assertEquals(581, getDFSSolutionFor(_args));
    }

    @Test
    public void testNodes9SeriesParallel_2P_2C() throws IOException {

        _args = new String[]{"src/test/resources/Nodes_9_SeriesParallel.dot", "2", "-p", "2"};

        assertEquals(55, getDFSSolutionFor(_args));
    }

    @Test
    public void testNodes9SeriesParallel_4P_2C() throws IOException {

        _args = new String[]{"src/test/resources/Nodes_9_SeriesParallel.dot", "4", "-p", "2"};

        assertEquals(55, getDFSSolutionFor(_args));
    }

    @Test
    public void testNodes9SeriesParallel_2P_4C() throws IOException {

        _args = new String[]{"src/test/resources/Nodes_9_SeriesParallel.dot", "2", "-p", "4"};

        assertEquals(55, getDFSSolutionFor(_args));
    }

    @Test
    public void testNodes9SeriesParallel_4P_4C() throws IOException {

        _args = new String[]{"src/test/resources/Nodes_9_SeriesParallel.dot", "4", "-p", "4"};

        assertEquals(55, getDFSSolutionFor(_args));
    }

    @Test
    public void testNodes10Random_2P_2C() throws IOException {
        _args = new String[]{"src/test/resources/Nodes_10_Random.dot", "2", "-p", "2"};


        assertEquals(50, getDFSSolutionFor(_args));
    }

    @Test
    public void testNodes10Random_4P_2C() throws IOException {
        _args = new String[]{"src/test/resources/Nodes_10_Random.dot", "4", "-p", "2"};


        assertEquals(50, getDFSSolutionFor(_args));
    }

    @Test
    public void testNodes10Random_2P_4C() throws IOException {
        _args = new String[]{"src/test/resources/Nodes_10_Random.dot", "2", "-p", "4"};


        assertEquals(50, getDFSSolutionFor(_args));
    }

    @Test
    public void testNodes10Random_4P_4C() throws IOException {
         _args = new String[]{"src/test/resources/Nodes_10_Random.dot", "4", "-p", "4"};


        assertEquals(50, getDFSSolutionFor(_args));
    }

    @Test
    public void testNodes11OutTree_2P_2C() throws IOException {
        _args = new String[]{"src/test/resources/Nodes_11_OutTree.dot", "2", "-p", "2"};

        assertEquals(350, getDFSSolutionFor(_args));
    }

    @Test
    public void testNodes11OutTree_4P_2C() throws IOException {
        _args = new String[]{"src/test/resources/Nodes_11_OutTree.dot", "4", "-p", "2"};

        assertEquals(227, getDFSSolutionFor(_args));
    }

    @Test
    public void testNodes11OutTree_2P_4C() throws IOException {
        _args = new String[]{"src/test/resources/Nodes_11_OutTree.dot", "2", "-p", "4"};

        assertEquals(350, getDFSSolutionFor(_args));
    }

    @Test
    public void testNodes11OutTree_4P_4C() throws IOException {
        _args = new String[]{"src/test/resources/Nodes_11_OutTree.dot", "4", "-p", "4"};

        assertEquals(227, getDFSSolutionFor(_args));
    }

    private int getDFSSolutionFor(String[] args) throws IOException {
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
        DFS dfs = new DFS(graph, null);
        return dfs.getOptimalScheduleParallel(graph, cli.getCores() - 1).getScheduledTask().getStartTime();
    }
}

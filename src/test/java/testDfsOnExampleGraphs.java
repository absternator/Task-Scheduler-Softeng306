import org.junit.Assume;
import org.junit.BeforeClass;
import org.junit.Test;
import team17.Algorithm.AlgorithmState;
import team17.Algorithm.DFS;
import team17.Algorithm.PartialSolution;

import team17.DAG.DAGGraph;
import team17.DAG.InvalidGraphException;

import team17.IO.CLI;
import team17.IO.FileReadWriter;
import team17.IO.IncorrectCLIInputException;
import team17.IO.InvalidEntryException;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class testDfsOnExampleGraphs {

    // *** SET TO TRUE IF YOU WANT TO RUN THESE TESTS ***
    private static boolean _RunThisTestSuite = false;

    // **************************************************

    private String[] _args;
    private PartialSolution _solution;

    @BeforeClass
    public static void setUpBeforeAll() {
        Assume.assumeTrue(_RunThisTestSuite);
    }

    @Test
    public void testNodes7OutTree_2P() throws IOException {

        _args = new String[]{"src/test/resources/Nodes_7_OutTree.dot", "2"};

        assertEquals(28, getDfsSolutionFor(_args));
    }

    @Test
    public void testNodes7OutTree_4P() throws IOException {

        _args = new String[]{"src/test/resources/Nodes_7_OutTree.dot", "4"};

        assertEquals(22, getDfsSolutionFor(_args));
    }

    @Test
    public void testNodes8Random_2P() throws IOException {

        _args = new String[]{"src/test/resources/Nodes_8_Random.dot", "2"};

        assertEquals(581, getDfsSolutionFor(_args));
    }

    @Test
    public void testNodes8Random_4P() throws IOException {

        _args = new String[]{"src/test/resources/Nodes_8_Random.dot", "4"};

        assertEquals(581, getDfsSolutionFor(_args));
    }

    @Test
    public void testNodes9SeriesParallel_2P() throws IOException {

        _args = new String[]{"src/test/resources/Nodes_9_SeriesParallel.dot", "2"};
        assertEquals(55, getDfsSolutionFor(_args));
    }

    @Test
    public void testNodes9SeriesParallel_4P() throws IOException {

        _args = new String[]{"src/test/resources/Nodes_9_SeriesParallel.dot", "4"};

        assertEquals(55, getDfsSolutionFor(_args));
    }

    @Test
    public void testNodes10Random_2P() throws IOException {
        _args = new String[]{"src/test/resources/Nodes_10_Random.dot", "2"};

        assertEquals(50, getDfsSolutionFor(_args));
    }

    @Test
    public void testNodes10Random_4P() throws IOException {
        _args = new String[]{"src/test/resources/Nodes_10_Random.dot", "4"};
        assertEquals(50, getDfsSolutionFor(_args));
    }

    @Test
    public void testNodes11OutTree_2P() throws IOException {
        _args = new String[]{"src/test/resources/Nodes_11_OutTree.dot", "2"};
        assertEquals(350, getDfsSolutionFor(_args));
    }

    @Test
    public void testNodes11OutTree_4P() throws IOException {

        _args = new String[]{"src/test/resources/Nodes_11_OutTree.dot", "4"};

        assertEquals(227, getDfsSolutionFor(_args));
    }

    @Test
    public void testInput1_2P() throws IOException {

        _args = new String[]{"src/test/resources/INPUT1.dot", "8"};

        assertEquals(119, getDfsSolutionFor(_args));

    }

    private int getDfsSolutionFor(String[] args) throws IOException {
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
        } catch (InvalidGraphException | InvalidEntryException e) {
            e.printStackTrace();
        }
        DFS dfs = new DFS(graph,new AlgorithmState());
        return dfs.getOptimalSchedule(graph).getScheduledTask().getFinishTime();
    }
}
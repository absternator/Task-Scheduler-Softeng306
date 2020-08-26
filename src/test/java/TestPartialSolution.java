import org.junit.Assume;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import team17.Algorithm.PartialSolution;
import team17.Algorithm.ScheduledTask;
import team17.DAG.Graph;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TestPartialSolution {

    // *** SET TO TRUE IF YOU WANT TO RUN THESE TESTS ***
    private static boolean _RunThisTestSuite = false;
    // **************************************************

    private PartialSolution _ps;
    private Graph _graph;
    private ScheduledTask _st;

    @BeforeClass
    public static void beforeClass() {
        Assume.assumeTrue(_RunThisTestSuite);
    }

    @Before
    public void setUp() throws Exception {
        _graph = new Graph();
        _graph.addNode("A", 3);
        _graph.addNode("B", 2);
        _graph.addNode("C", 4);
        _graph.addEdge("A", "B", 1);
        _graph.addEdge("A", "C", 5);

        _st = new ScheduledTask(1, _graph.getNode("A"), 0);

        _ps = new PartialSolution(null, _st);
    }

    @Test
    public void testEquals() {
        PartialSolution ps2 = new PartialSolution(null, _st);

        assertTrue(_ps.equals(ps2));
    }

    @Test
    public void testEqualsContent() throws Exception {
        Graph compare = new Graph();
        compare.addNode("A", 3);
        compare.addNode("B", 2);
        compare.addNode("C", 4);
        compare.addEdge("A", "B", 1);
        compare.addEdge("A", "C", 5);
        ScheduledTask st2 = new ScheduledTask(1, compare.getNode("A"), 0);
        PartialSolution ps2 = new PartialSolution(null, st2);

        assertTrue(_ps.equals(ps2));
    }

    @Test
    public void testNotEquals() throws Exception {

        Graph graph2 = new Graph();
        graph2.addNode("A", 1);
        graph2.addNode("B", 1);
        graph2.addNode("C", 1);
        graph2.addEdge("A", "B", 1);
        graph2.addEdge("A", "C", 1);

        ScheduledTask st2 = new ScheduledTask(1, graph2.getNode("A"), 0);
        PartialSolution ps2 = new PartialSolution(null, st2);

        assertFalse(_ps.equals(ps2));
    }
}

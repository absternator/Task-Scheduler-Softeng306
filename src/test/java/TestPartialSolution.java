import org.junit.Assume;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import team17.Algorithm.PartialSolution;
import team17.Algorithm.ScheduledTask;
import team17.DAG.DAGGraph;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TestPartialSolution {

    // *** SET TO TRUE IF YOU WANT TO RUN THESE TESTS ***
    private static boolean _RunThisTestSuite = true;
    // **************************************************

    private PartialSolution _ps;
    private PartialSolution _root;
    private DAGGraph _graph;
    private ScheduledTask _st;

    @BeforeClass
    public static void beforeClass() {
        Assume.assumeTrue(_RunThisTestSuite);
    }

    @Before
    public void setUp() throws Exception {
        _graph = new DAGGraph();
        _graph.addNode("A", 3);
        _graph.addNode("B", 2);
        _graph.addNode("C", 4);
        _graph.addEdge("A", "B", 1);
        _graph.addEdge("A", "C", 5);
        _graph.addFinishNode();
        _graph.setBottomLevel();
        _root = new PartialSolution(null, null, 0);

        _st = new ScheduledTask(1, _graph.getNode("A"), 0);

        _ps = new PartialSolution(_root, _st, 0);
    }

    @Test
    public void testEquals() {
        PartialSolution ps2 = new PartialSolution(_root, _st, 0);
        assertTrue(_ps.equals(ps2));
    }

    @Test
    public void testEqualsContent() {
        ScheduledTask st2 = new ScheduledTask(1, _graph.getNode("A"), 0);
        PartialSolution ps2 = new PartialSolution(_root, st2, 0);

        assertTrue(_ps.equals(ps2));
    }

    @Test
    public void testNotEqualsNode() {

        ScheduledTask st2 = new ScheduledTask(1, _graph.getNode("B"), 0);
        PartialSolution ps2 = new PartialSolution(_root, st2, 0);

        assertFalse(_ps.equals(ps2));
    }

    @Test
    public void testNotEqualsStartTime() {

        ScheduledTask st2 = new ScheduledTask(1, _graph.getNode("A"), 3);
        PartialSolution ps2 = new PartialSolution(_root, st2, 0);

        assertFalse(_ps.equals(ps2));
    }
}

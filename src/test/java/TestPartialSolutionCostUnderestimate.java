import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import team17.Algorithm.AlgorithmConfig;
import team17.Algorithm.PartialSolution;
import team17.Algorithm.ScheduledTask;
import team17.DAG.DAGGraph;

import static org.junit.Assert.*;

/**
 * These test getCostUnderestimate and getIdleTime in PartialSolution.
 */
public class TestPartialSolutionCostUnderestimate {
    private static DAGGraph _graph;
    private static PartialSolution _root;
    private static PartialSolution _ps;
    private static ScheduledTask _st;

    @BeforeClass
    public static void setUpClass() throws Exception {
        _graph = new DAGGraph();
        _graph.addNode("A", 3);
        _graph.addNode("B", 2);
        _graph.addNode("C", 4);
        _graph.addNode("D", 9);
        _graph.addEdge("A", "B", 1);
        _graph.addEdge("A", "C", 2);
        _graph.addFinishNode();
        _graph.setBottomLevel();
        _graph.calculateTotalNodeWeight();
        _root = new PartialSolution(null, null, 0);

        _st = new ScheduledTask(1, _graph.getNode("A"), 0);

        _ps = new PartialSolution(_root, _st, 4);
    }

    @Before
    public void setUpTest() {
        AlgorithmConfig.setNumOfProcessors(2);
    }

    @Test
    public void testGetIdleTimeNone() {
        PartialSolution ps = new PartialSolution(_ps, new ScheduledTask(1, _graph.getNode("B"), 3), 1);
        assertEquals(0, ps.getIdleTime());
    }

    @Test
    public void testGetIdleTime_4p() {
        AlgorithmConfig.setNumOfProcessors(4);
        PartialSolution ps = new PartialSolution(_ps, new ScheduledTask(3, _graph.getNode("B"), 3), 2);
        assertEquals(3, ps.getIdleTime());
    }

    @Test
    public void testGetIdleTime() {
        AlgorithmConfig.setNumOfProcessors(4);
        PartialSolution ps = new PartialSolution(_ps, new ScheduledTask(1, _graph.getNode("B"), 13), 2);
        assertEquals(10, ps.getIdleTime());
    }

    @Test
    public void testGetCostUnderestimateNoIdleTime() {
        PartialSolution ps = new PartialSolution(_ps, new ScheduledTask(1, _graph.getNode("B"), 3), 2);
        assertEquals(9, ps.getCostUnderestimate());
    }

    @Test
    public void testGetCostUnderestimateWithIdleTime() {
        PartialSolution ps = new PartialSolution(_ps, new ScheduledTask(1, _graph.getNode("B"), 5), 2);
        assertEquals(10, ps.getCostUnderestimate());
    }

    @Test
    public void testGetCostUnderestimateNoIdleTime_4p() {
        AlgorithmConfig.setNumOfProcessors(4);
        PartialSolution ps = new PartialSolution(_ps, new ScheduledTask(1, _graph.getNode("B"), 3), 2);
        PartialSolution ps2 = new PartialSolution(ps, new ScheduledTask(4, _graph.getNode("D"), 0), 2);
        assertEquals(9, ps2.getCostUnderestimate());
    }

    @Test
    public void testGetCostUnderestimateWithIdleTime_3p() {
        AlgorithmConfig.setNumOfProcessors(3);
        PartialSolution ps = new PartialSolution(_ps, new ScheduledTask(1, _graph.getNode("B"), 15), 2);
        PartialSolution ps2 = new PartialSolution(ps, new ScheduledTask(3, _graph.getNode("C"), 10), 2);
        assertEquals(17, ps2.getCostUnderestimate());
    }
}

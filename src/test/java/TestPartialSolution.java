import org.junit.Before;
import org.junit.Test;
import team17.Algorithm.PartialSolution;
import team17.Algorithm.ScheduledTask;
import team17.DAG.Graph;

import static org.junit.Assert.fail;

public class TestPartialSolution {
    private PartialSolution _ps;
    private Graph _graph;
    private ScheduledTask _st;

    @Before
    public void setUp() throws Exception {
        _graph = new Graph();
        _graph.addNode("A", 3);
        _graph.addNode("B", 2);
        _graph.addNode("C", 4);
        _graph.addEdge("A", "B", 1);
        _graph.addEdge("A", "C", 5);

        _st = new ScheduledTask(1, _graph.getNode("A"), 0);

        _ps = new PartialSolution(null, _graph, _st);
    }

    @Test
    public void testEquals() {
        PartialSolution ps2 = new PartialSolution(null, _graph, _st);

        if (!_ps.equals(ps2)) {
            fail();
        }
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
        PartialSolution ps2 = new PartialSolution(null, compare, st2);

        if (!_ps.equals(ps2)) {
            fail();
        }
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
        PartialSolution ps2 = new PartialSolution(null, graph2, st2);

        if (_ps.equals(ps2)) {
            fail();
        }
    }
}

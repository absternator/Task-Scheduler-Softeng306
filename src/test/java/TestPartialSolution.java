import org.junit.Before;
import org.junit.Test;
import team17.Algorithm.PartialSolution;
import team17.Algorithm.ScheduledTask;
import team17.DAG.Graph;

import static org.junit.Assert.fail;

public class TestPartialSolution {
    private PartialSolution _ps;

    @Before
    public void setUp(){
    }

    @Test
    public void testEquals() throws Exception {
        Graph graph = new Graph();
        graph.addNode("A", 3);
        graph.addNode("B", 2);
        graph.addNode("C", 4);
        graph.addEdge("A", "B", 1);
        graph.addEdge("A", "C", 5);


        Graph graph2 = new Graph();
        graph2.addNode("A", 3);
        graph2.addNode("B", 2);
        graph2.addNode("C", 4);
        graph2.addEdge("A", "B", 1);
        graph2.addEdge("A", "C", 5);

        ScheduledTask st = new ScheduledTask(1, graph.getNode("A"), 0);

        ScheduledTask st2 = new ScheduledTask(1, graph2.getNode("A"), 0);

        _ps = new PartialSolution(null, graph, st);

        PartialSolution ps2 = new PartialSolution(null, graph2, st2);
        if (!_ps.equals(ps2)){
            fail();
        }
    }
}

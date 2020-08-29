import org.junit.Before;
import org.junit.Test;
import team17.DAG.DAGGraph;

import static org.junit.Assert.*;

public class TestGraph {
    DAGGraph _graph;

    @Before
    public void setUp() {
        _graph = new DAGGraph();
    }

    @Test
    public void testSetBottomLevelSimple() throws Exception {

        _graph.addNode("A", 3);
        _graph.addNode("B", 2);
        _graph.addNode("C", 4);
        _graph.addEdge("A", "B", 1);
        _graph.addEdge("A", "C", 5);

        _graph.setBottomLevel();
        assertTrue(_graph.getNode("A").getBottomLevel() == 7
                || _graph.getNode("B").getBottomLevel() == 2
                || _graph.getNode("C").getBottomLevel() == 4);
    }

    @Test(timeout = 3000)
    public void testSetBottomLevelCycle() throws Exception {

        _graph.addNode("A", 3);
        _graph.addNode("B", 2);
        _graph.addNode("C", 4);
        _graph.addEdge("A", "B", 1);
        _graph.addEdge("B", "C", 2);
        _graph.addEdge("C", "A", 3);

        try {
            _graph.setBottomLevel();
            fail();
        } catch (Exception e) {
            // pass
        }
    }

    @Test
    public void testSetEquivalentNodes() throws Exception {
        _graph.addNode("A", 3);
        _graph.addNode("B", 2);
        _graph.addNode("C", 2);
        _graph.addNode("D", 4);
        _graph.addEdge("A", "B", 1);
        _graph.addEdge("B", "D", 2);
        _graph.addEdge("A", "C", 1);
        _graph.addEdge("C", "D", 2);

        _graph.setEquivalentNodes();
        assertTrue(_graph.getNode("B").getEquivalenceId() == _graph.getNode("C").getEquivalenceId()
                && _graph.getNode("A").getEquivalenceId() != _graph.getNode("B").getEquivalenceId()
                && _graph.getNode("D").getEquivalenceId() != _graph.getNode("B").getEquivalenceId()
                && _graph.getNode("A").getEquivalenceId() != _graph.getNode("D").getEquivalenceId());

    }

}

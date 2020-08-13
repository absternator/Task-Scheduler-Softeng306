import org.junit.Before;
import org.junit.Test;
import team17.DAG.Graph;

import static org.junit.Assert.fail;

public class TestGraph {
    Graph _graph;

    @Before
    public void setUp() throws Exception {
        _graph = new Graph();
    }

    @Test
    public void testSetBottomLevelSimple() throws Exception {

        _graph.addNode("A", 3);
        _graph.addNode("B", 2);
        _graph.addNode("C", 4);
        _graph.addEdge("A", "B", 1);
        _graph.addEdge("A", "C", 5);

        _graph.setBottomLevel();
        if (_graph.getNode("A").getBottomLevel() != 7 || _graph.getNode("B").getBottomLevel() != 2 || _graph.getNode("C").getBottomLevel() != 4) {
            fail();
        }
    }

    @Test (timeout = 3000)
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
    /*
    // Test is probably out of scope?
    @Test (timeout = 3000)
    public void testSetBottomLevelCycleWithTwoNodes() throws Exception {

        _graph.addNode("A", 3);
        _graph.addNode("B", 2);
        _graph.addNode("C", 4);
        _graph.addEdge("A", "B", 1);
        _graph.addEdge("B", "A", 3);

        try {
            _graph.setBottomLevel();
            fail();
        } catch (Exception e) {
            // pass
        }

    }
    */
}

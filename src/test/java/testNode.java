import org.junit.BeforeClass;
import org.junit.Test;
import team17.DAG.DAGGraph;
import team17.DAG.InvalidGraphException;
import team17.IO.CLI;
import team17.IO.FileReadWriter;
import team17.IO.IncorrectCLIInputException;
import team17.IO.InvalidEntryException;

import java.io.IOException;

import static org.junit.Assert.*;

public class testNode {
    private static DAGGraph _graph;
    private static FileReadWriter _frw;

    @BeforeClass
    public static void setUp() throws IOException, InvalidGraphException, InvalidEntryException, IncorrectCLIInputException {
        String[] args = new String[]{"src/test/resources/testNode.dot", "2"};

        CLI cli = new CLI();
        cli.readCLI(args);
        _frw = new FileReadWriter(cli);
        _graph = _frw.readDotFile();
    }

    @Test
    public void testIsEquivalentEqualNodes() {
        assertTrue(_graph.getNode("b").isEquivalent(_graph.getNode("c")));
    }

    @Test
    public void testSameNode() {
        assertTrue(_graph.getNode("b").isEquivalent(_graph.getNode("b")));
    }

    @Test
    public void testIsEquivalentDifferentNodes() {
        assertFalse(_graph.getNode("b").isEquivalent(_graph.getNode("a")));
    }

    @Test
    public void testIsEquivalentDifferentOutgoingEdgeWeight() {
        assertFalse(_graph.getNode("b").isEquivalent(_graph.getNode("e")));
    }

    @Test
    public void testIsEquivalentDifferentWeight() {
        assertFalse(_graph.getNode("b").isEquivalent(_graph.getNode("h")));
    }

    @Test
    public void testIsEquivalentDifferentDependents() {
        assertFalse(_graph.getNode("b").isEquivalent(_graph.getNode("f")));
    }

    @Test
    public void testIsEquivalentDifferentDependencies() {
        assertFalse(_graph.getNode("b").isEquivalent(_graph.getNode("i")));
    }

    @Test
    public void testIsEquivalentSameEqId() throws IOException, InvalidGraphException, InvalidEntryException {
        DAGGraph graph = _frw.readDotFile();
        graph.setEquivalentNodes();
        assertTrue(graph.getNode("b").isEquivalent(graph.getNode("c")));
    }

    @Test
    public void testIsEquivalentDifferentEqId() throws IOException, InvalidGraphException, InvalidEntryException {
        DAGGraph graph = _frw.readDotFile();
        graph.setEquivalentNodes();
        assertFalse(graph.getNode("b").isEquivalent(graph.getNode("d")));
    }

}

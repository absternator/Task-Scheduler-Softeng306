import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import team17.Algorithm.Algorithm;
import team17.DAG.Graph;
import team17.IO.CLI;
import team17.IO.FileReadWriter;
import java.io.IOException;

import static org.junit.Assert.*;

public class testNode {
    private static Graph _graph;
    @BeforeClass
    public static void setUp() throws IOException {
        String[] args = new String[]{"src/main/resources/testNode.dot", "2"};

        CLI cli = new CLI(args);
        FileReadWriter frw = new FileReadWriter(cli);
        Algorithm algorithm;
        _graph = frw.readDotFile();
    }

    @Test
    public void testEqualNodes(){
        assertTrue(_graph.getNode("b").equals(_graph.getNode("c")));
    }
    @Test
    public void testSameNode(){
        assertTrue(_graph.getNode("b").equals(_graph.getNode("b")));
    }

    @Test
    public void testEqualsDifferentNodes(){
        assertFalse(_graph.getNode("b").equals(_graph.getNode("a")));
    }

    @Test
    public void testEqualsDifferentOutgoingEdgeWeight(){
        assertFalse(_graph.getNode("b").equals(_graph.getNode("e")));
    }

    @Test
    public void testEqualsDifferentWeight(){
        assertFalse(_graph.getNode("b").equals(_graph.getNode("h")));
    }

    @Test
    public void testEqualsDifferentDependents(){
        assertFalse(_graph.getNode("b").equals(_graph.getNode("f")));
    }
    @Test
    public void testEqualsDifferentDependencies(){
        assertFalse(_graph.getNode("b").equals(_graph.getNode("i")));
    }

}

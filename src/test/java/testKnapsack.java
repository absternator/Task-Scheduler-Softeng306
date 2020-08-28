import org.junit.Test;
import team17.Algorithm.AStar;
import team17.Algorithm.AlgorithmConfig;
import team17.Algorithm.KnapsackScheduler;
import team17.Algorithm.PartialSolution;
import team17.DAG.Graph;
import team17.DAG.Node;
import team17.IO.CLI;
import team17.IO.FileReadWriter;

import java.io.IOException;
import java.util.ArrayList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class testKnapsack {
    @Test
    public void testCalculateKnapsack(){
        ArrayList<Node> nodes = new ArrayList<>();
        Node a = new Node("a", 10);
        Node b = new Node("b", 5);
        Node c = new Node("c", 10);
        Node d = new Node("d", 30);
        nodes.add(a);
        nodes.add(b);
        nodes.add(c);
        nodes.add(d);

        ArrayList<Node> expected = new ArrayList<>();
        expected.add(a);
        expected.add(b);
        expected.add(c);

        ArrayList<Node> result = KnapsackScheduler.knapsack(29, nodes);
        assertTrue(result.containsAll(expected) && !result.contains(d));

    }

    @Test
    public void testGetSchedule() throws IOException {
        String[] args = new String[]{"src/main/resources/20independent.dot", "8"};
        assertEquals(9,getKnapsackSolutionFor(args));
    }

    private int getKnapsackSolutionFor(String[] args) throws IOException {
        CLI cli = new CLI(args);
        FileReadWriter frw = new FileReadWriter(cli);
        Graph graph = frw.readDotFile();
        KnapsackScheduler knapsack = new KnapsackScheduler(graph);
        PartialSolution ps = knapsack.getSchedule();
        frw.writeOutput(ps.fullSchedule());
        return ps.getScheduledTask().getStartTime();
    }
}

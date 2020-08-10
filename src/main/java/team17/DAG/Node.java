package team17.DAG;
import java.util.List;

public class Node {
    private String _id;
    private int _weight;
    private List<Node> _incoming;
    private List<Node> _outgoing;

    public Node(String id, int weight, List<Node> incoming, List<Node> outgoing){
        _id=id;
        _weight=weight;
        _incoming =incoming;
        _outgoing =outgoing;
    }
}

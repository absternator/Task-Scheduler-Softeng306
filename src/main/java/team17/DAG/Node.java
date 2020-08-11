package team17.DAG;
import java.util.*;

public class Node {
    private final String _id;
    private final int _weight;
    private int _bottomLevel;
    private Map<Node,Integer> _incomingEdges;
    private Set<Node> _dependendicies;
    private Set<Node> _dependants;



    /**
     * This is a Node constructor which adds weight and id.
     * @param id This is the id of the task
     * @param weight This is the weight of the task
     */
    public Node(String id, int weight){
        _id=id;
        _weight=weight;
        _incomingEdges = new HashMap<>();
        _dependants = new HashSet<>();
        _dependendicies = new HashSet<>();
    }

    public String get_id() {
        return _id;
    }

    public int get_weight() {
        return _weight;
    }

    public int get_bottomLevel() {
        return _bottomLevel;
    }

    public Map<Node, Integer> get_incomingEdges() {
        return _incomingEdges;
    }



    public Set<Node> get_dependendicies() {
        return _dependendicies;
    }

    public Set<Node> get_dependants() {
        return _dependants;
    }

    public void set_bottomLevel(int _bottomLevel) {
        this._bottomLevel = _bottomLevel;
    }

    public void set_incomingEdges(Node edge, int edgeWeight) {
        _incomingEdges.put(edge,edgeWeight);
    }


    public void set_dependendicies(Node dependency) {
        _dependendicies.add(dependency);
    }

    public void set_dependants(Node dependant) {
        _dependants.add(dependant);
    }

    @Override
    public boolean equals(Object other) {
        return _id.equals(((Node) other).get_id());
    }

    @Override
    public int hashCode() {
        return _id.hashCode();
    }

    @Override
    public String toString() {
        return "Node{" +
                "_id='" + _id + '\'' +
                ","  ;
    }

}

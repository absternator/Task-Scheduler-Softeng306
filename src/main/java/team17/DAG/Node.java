package team17.DAG;
import java.util.*;

public class Node {
    private final String _id;
    private final int _weight;
    private int _bottomLevel;
    private Map<Node,Integer> _incomingEdges;
    private Map<Node,Integer> _outgoingEdges;
    private Set<Node> _dependencies;
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
        _outgoingEdges = new HashMap<>();
        _dependants = new HashSet<>();
        _dependencies = new HashSet<>();
    }

    public Node(Node node){
        _id = node._id;
        _weight = node._weight;
        _dependencies=new HashSet<>(node._dependencies);
        _dependants=new HashSet<>(node._dependants);
        _bottomLevel=node._bottomLevel;
        _incomingEdges=new HashMap<>(node._incomingEdges);
    }

    public String getId() {
        return _id;
    }

    public int getWeight() {
        return _weight;
    }

    public int getBottomLevel() {
        return _bottomLevel;
    }

    public Map<Node, Integer> getIncomingEdges() {
        return _incomingEdges;
    }

    public Map<Node, Integer> getOutgoingEdges() {
        return _outgoingEdges;
    }

    public Set<Node> getDependencies() {
        return _dependencies;
    }

    public Set<Node> getDependants() {
        return _dependants;
    }

    public void setBottomLevel(int bottomLevel) {
        this._bottomLevel = bottomLevel;
    }

    public void setIncomingEdges(Node edge, int edgeWeight) {
        _incomingEdges.put(edge,edgeWeight);
    }
    public void setOutgoingEdges(Node edge, int edgeWeight) {
        _outgoingEdges.put(edge,edgeWeight);
    }


    public void setDependencies(Node dependency) {
        _dependencies.add(dependency);
    }

    public void setDependants(Node dependant) {
        _dependants.add(dependant);
    }

    @Override
    public boolean equals(Object other) {
        return _id.equals(((Node) other).getId());
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

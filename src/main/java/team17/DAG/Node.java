package team17.DAG;

import java.util.*;

public class Node {
    private final String _id;
    private final int _weight;
    private int _bottomLevel;
    private Map<Node, Integer> _incomingEdges;
    private Set<Node> _dependencies;
    private Set<Node> _dependants;
    private int _eqId;

    /**
     * This is a Node constructor which adds weight and id.
     *
     * @param id     This is the id of the task
     * @param weight This is the weight of the task
     */
    public Node(String id, int weight) {
        _id = id;
        _weight = weight;
        _incomingEdges = new HashMap<>();
        _dependants = new HashSet<>();
        _dependencies = new HashSet<>();
    }

    public Node(Node node) {
        _id = node._id;
        _weight = node._weight;
        _dependencies = new HashSet<>(node._dependencies);
        _dependants = new HashSet<>(node._dependants);
        _bottomLevel = node._bottomLevel;
        _incomingEdges = new HashMap<>(node._incomingEdges);
        _eqId=node._eqId;
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


    public Set<Node> getDependencies() {
        return _dependencies;
    }

    public Set<Node> getDependants() {
        return _dependants;
    }

    public int getEquivalenceId() {
        return _eqId;
    }

    public void setBottomLevel(int bottomLevel) {
        this._bottomLevel = bottomLevel;
    }

    public void setIncomingEdges(Node edge, int edgeWeight) {
        _incomingEdges.put(edge, edgeWeight);
    }

    public void setDependencies(Node dependency) {
        _dependencies.add(dependency);
    }

    public void setDependants(Node dependant) {
        _dependants.add(dependant);
    }

    public void setEquivalenceId(int eqId) {
        _eqId = eqId;
    }

    @Override
    public boolean equals(Object other) {
        return _id.equals(((Node) other)._id);
    }

    public boolean isEquivalent(Node other) {
        // if eqId has been set in both nodes, check if they are the same
        if (_eqId != 0 && other._eqId != 0) {
            return _eqId == other._eqId;
        }

        if (_weight != other._weight || !_incomingEdges.equals(other._incomingEdges)
                || !_dependants.equals(other._dependants)) {
            return false;
        }

        // check if the weights of the outgoing edges are the same
        for (Node dependant : _dependants) {
            // for each dependent check that the weight of the incoming edge is the same for this node and other node
            if (!dependant._incomingEdges.get(this).equals(dependant._incomingEdges.get(other))) {
                return false;
            }

        }

        return true;
    }

    @Override
    public int hashCode() {
        return _id.hashCode();
    }

    @Override
    public String toString() {
        return "Node{" +
                "_id='" + _id + '\'' +
                ",";
    }

}

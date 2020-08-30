package team17.DAG;

import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.*;

/**
 * This is a representation of the nodes in the graph
 */
public class DAGNode {
    private final String _id;
    private final int _weight;
    private int _bottomLevel;
    private Map<DAGNode,Integer> _incomingEdges;
    private Set<DAGNode> _dependencies;
    private Set<DAGNode> _dependants;
    private int _eqId;
    private int _bottomLoad;

    /**
     * This is a Node constructor which adds weight and id.
     *
     * @param id     This is the id of the task
     * @param weight This is the weight of the task
     */
    public DAGNode(String id, int weight) {
        _id = id;
        _weight = weight;
        _incomingEdges = new HashMap<>();

        _dependants = new HashSet<>();
        _dependencies = new HashSet<>();
    }

    public DAGNode(DAGNode node) {
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

    public Map<DAGNode, Integer> getIncomingEdges() {
        return _incomingEdges;
    }


    public Set<DAGNode> getDependencies() {
        return _dependencies;
    }

    public Set<DAGNode> getDependants() {
        return _dependants;
    }

    public int getEquivalenceId() {
        return _eqId;
    }

    public void setBottomLevel(int bottomLevel) {
        this._bottomLevel = bottomLevel;
    }

    public void setIncomingEdges(DAGNode edge, int edgeWeight) {
        _incomingEdges.put(edge, edgeWeight);
    }


    public void setDependencies(DAGNode dependency) {
        _dependencies.add(dependency);
    }

    public void setDependants(DAGNode dependant) {
        _dependants.add(dependant);
    }

    public void setEquivalenceId(int eqId) {
        _eqId = eqId;
    }

    /**
     * This method calculates the sum of weights of the node's children
     */
    public void setBottomLoad() {
        _bottomLoad = 0;
        Set<DAGNode> children = getChildren();
        for(DAGNode child: children) {
            _bottomLoad += child.getWeight();
        }
    }

    /**
     * Gets the children of the node without duplicate
     * @return Set of child nodes
     */
    public Set<DAGNode> getChildren() {
        Set<DAGNode> children = new HashSet<>();
        children.addAll(_dependants);
        for(DAGNode child: _dependants) {
            children.addAll(child.getChildren());
        }
        return children;
    }

    public int getBottomLoad() {
        return _bottomLoad;
    }

    @Override
    public boolean equals(Object other) {
        return _id.equals(((DAGNode) other)._id);
    }

    /**
     * Checks whether two nodes are equivalent
     *
     * @param other The other node to compare against
     * @return True if the two nodes are equivalent, else false
     */
    public boolean isEquivalent(DAGNode other) {
        // If eqId has been set in both nodes, check if they are the same
        if (_eqId != 0 && other._eqId != 0) {
            return _eqId == other._eqId;
        }

        if (_weight != other._weight || !_incomingEdges.equals(other._incomingEdges)
                || !_dependants.equals(other._dependants)) {
            return false;
        }

        // Check if the weights of the outgoing edges are the same
        for (DAGNode dependant : _dependants) {
            // For each dependent check that the weight of the incoming edge is the same for this node and other node
            if (!dependant._incomingEdges.get(this).equals(dependant._incomingEdges.get(other))) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(_id).toHashCode();
    }

    @Override
    public String toString() {
        return "Node{" +
                "_id='" + _id + '\'' +
                ",";
    }

}

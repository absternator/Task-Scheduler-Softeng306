package team17.DAG;

public class DagEdge {
    private DagNode _startNode;
    private DagNode _endNode;
    private int _edgeWeight;

    public DagEdge(DagNode startNode, DagNode endNode, int edgeWeight) {
        _startNode = startNode;
        _endNode = endNode;
        _edgeWeight = edgeWeight;
    }

    public DagNode getStartNode() {
        return _startNode;
    }
    public DagNode getEndNode() { return _endNode; }

    public int getEdgeWeight() {
        return _edgeWeight;
    }

    @Override
    public boolean equals(Object o) {
        return _startNode.equals(o);
    }

    @Override
    public int hashCode() {
        return _startNode.hashCode();
    }

    @Override
    public String toString() {
        return "{" +
                "endNode='" + _endNode + '\'' +
                ", edgeWeight=" + _edgeWeight +
                '}';
    }
}

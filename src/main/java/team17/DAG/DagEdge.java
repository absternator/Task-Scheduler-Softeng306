package team17.DAG;

public class DagEdge {
    DagNode startNode;
    DagNode endNode;
    int edgeWeight;



    public DagEdge(DagNode startNode, DagNode endNode, int edgeWeight) {
        this.startNode = startNode;
        this.endNode = endNode;
        this.edgeWeight = edgeWeight;
    }

    public DagNode getStartNode() {
        return startNode;
    }
    public DagNode getEndNode() {
        return endNode;
    }

    public int getEdgeWeight() {
        return edgeWeight;
    }

    @Override
    public boolean equals(Object o) {
        return startNode.equals(o);
    }

    @Override
    public int hashCode() {
        return startNode.hashCode();
    }

    @Override
    public String toString() {
        return "{" +
                "endNode='" + endNode + '\'' +
                ", edgeWeight=" + edgeWeight +
                '}';
    }
}

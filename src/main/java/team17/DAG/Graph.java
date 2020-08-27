package team17.DAG;

import java.util.*;

/**
 * This represents the input dot file in a graph to be used to schedule.
 */
public class Graph {
    private int _numOfProcessors; // TODO: 12/08/20 relocate if needed
    private List<Node> _nodeList;
    private Map<String, Node> _nodeLookup;

    public Graph() {
        _nodeList = new ArrayList<>();
        _nodeLookup = new HashMap<>();
    }

    public Node getNode(String id) {
        return _nodeLookup.get(id);
    }

    public int getNumOfProcessors() {
        return _numOfProcessors;
    }

    public List<Node> getNodeList() {
        return _nodeList;
    }

    public Map<String, Node> getNodeLookup() {
        return _nodeLookup;
    }

    public void setNumOfProcessors(int numOfProcessors) {
        _numOfProcessors = numOfProcessors;
    }

    /**
     * This just adds node to graph
     *
     * @param id     the id of node to be added
     * @param weight weight of node to be added
     */
    public void addNode(String id, int weight) {
        Node node = new Node(id, weight);
        _nodeList.add(node);
        _nodeLookup.put(id, node);

    }

    /**
     * This adds adges from one task to another, whilst setting up dependencies.
     *
     * @param from       Task from in string format
     * @param to         Task to task in string format
     * @param edgeWeight Communication time from 2 tasks
     * @throws Exception Thrown if task not in graph already and edge is tried to be added.
     */
    public void addEdge(String from, String to, int edgeWeight) throws Exception {
        if (!_nodeLookup.containsKey(from) || !_nodeLookup.containsKey(to)) {
            throw new Exception("ERROR: Node has to be instantiated before adding edge!");
        }
        Node fromNode = _nodeLookup.get(from);
        Node toNode = _nodeLookup.get(to);

        // Set outgoing from - > to node edge
        fromNode.setDependants(toNode);
        fromNode.setOutgoingEdges(toNode, edgeWeight);
        //set incoming from - > to node edge
        toNode.setIncomingEdges(fromNode, edgeWeight);
        toNode.setDependencies(fromNode);
    }

    public void addFinishNode() {
        Node finish = new Node("end", 0);
        _nodeList.add(finish);
        _nodeLookup.put("end", finish);
        for (Node node : _nodeList) {
            if (node.getDependants().size() == 0 && !node.equals(finish)) {
                node.setOutgoingEdges(finish, 0);
                node.setDependants(finish);
                finish.setIncomingEdges(node, 0);
                finish.setDependencies(node);
            }
        }
    }

    @Override
    public String toString() {
        return "Graph{" +
                "_numOfProcessors=" + _numOfProcessors +
                ", _nodeList=" + _nodeList +
                '}';
    }

    /**
     * This sets the bottom level for each task. Will be used as heuristic for algorithm.
     */
    public void setBottomLevel() {
        boolean progress = false;
        HashSet<Node> completed = new HashSet<>();
        LinkedList<Node> remaining = new LinkedList<>(_nodeList);
        while (!remaining.isEmpty()) {
            for (Iterator<Node> it = remaining.descendingIterator(); it.hasNext(); ) {
                Node node = it.next();
                if (completed.containsAll(node.getDependants())) {
                    int bottomLevel = 0;
                    for (Node task : node.getDependants()) {
                        if (task.getBottomLevel() > bottomLevel) {
                            bottomLevel = task.getBottomLevel();
                        }
                    }
                    node.setBottomLevel(bottomLevel + node.getWeight());
                    completed.add(node);
                    it.remove();
                    progress = true;
                }
            }
            if (!progress) throw new RuntimeException("\"Cyclic dependency, algorithm stopped!");

        }
    }
}

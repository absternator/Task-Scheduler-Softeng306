package team17.DAG;

import team17.Algorithm.AlgorithmConfig;

import java.util.*;

/**
 * This represents the input dot file in a graph to be used to schedule.
 */
public class DAGGraph {
    private List<DAGNode> _nodeList;
    private Map<String, DAGNode> _nodeLookup;

    public DAGGraph() {
        _nodeList = new ArrayList<>();
        _nodeLookup = new HashMap<>();
    }

    public DAGNode getNode(String id) {
        return _nodeLookup.get(id);
    }

    public List<DAGNode> getNodeList() {
        return _nodeList;
    }

    public Map<String, DAGNode> getNodeLookup() {
        return _nodeLookup;
    }

    /**
     * This just adds node to graph
     *
     * @param id     the id of node to be added
     * @param weight weight of node to be added
     */
    public void addNode(String id, int weight) {
        DAGNode node = new DAGNode(id, weight);
        _nodeList.add(node);
        _nodeLookup.put(id, node);

    }

    /**
     * This adds edges from one task to another, whilst setting up dependencies.
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
        DAGNode fromNode = _nodeLookup.get(from);
        DAGNode toNode = _nodeLookup.get(to);

        // Set outgoing from - > to node edge
        fromNode.setDependants(toNode);

        //set incoming from - > to node edge
        toNode.setIncomingEdges(fromNode, edgeWeight);
        toNode.setDependencies(fromNode);
    }

    public void addFinishNode() {
        DAGNode finish = new DAGNode("end", 0);
        _nodeList.add(finish);
        _nodeLookup.put("end", finish);
        for (DAGNode node : _nodeList) {
            if (node.getDependants().size() == 0 && !node.equals(finish)) {

                node.setDependants(finish);
                finish.setIncomingEdges(node, 0);
                finish.setDependencies(node);
            }
        }
    }

    public void setEquivalentNodes() {
        int eqId = 1; // the equivalence id
        ArrayList<DAGNode> unset = new ArrayList<>(_nodeList); // the nodes that haven't had their eqId set
        ArrayList<DAGNode> remove = new ArrayList<>();

        for (DAGNode node : _nodeList) {
            if (node.getEquivalenceId() == 0) { // only set the eqId if it has not already been set
                node.setEquivalenceId(eqId);
                unset.remove(node);

                // check all unset nodes for equivalence
                for (DAGNode other : unset) {
                    if (node.isEquivalent(other)) {
                        other.setEquivalenceId(eqId);
                        remove.add(other);
                    }
                }

                // remove set nodes from unset
                unset.removeAll(remove);
                remove.clear();
                eqId++;
            }
        }
    }

    @Override
    public String toString() {
        return "Graph{" +
                "_numOfProcessors=" + AlgorithmConfig.getNumOfProcessors() +
                ", _nodeList=" + _nodeList +
                '}';
    }

    /**
     * This sets the bottom level for each task. Will be used as heuristic for algorithm.
     */
    public void setBottomLevel() {
        boolean progress = false;
        HashSet<DAGNode> completed = new HashSet<>();
        LinkedList<DAGNode> remaining = new LinkedList<>(_nodeList);
        while (!remaining.isEmpty()) {
            for (Iterator<DAGNode> it = remaining.descendingIterator(); it.hasNext(); ) {
                DAGNode node = it.next();
                if (completed.containsAll(node.getDependants())) {
                    int bottomLevel = 0;
                    for (DAGNode task : node.getDependants()) {
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

    /**
     * This calculates the total weight of all the nodes in the graph and sets it in AlgorithmConfig
     */
    public void calculateTotalNodeWeight() {
        int totalWeight = 0;
        for (DAGNode node : _nodeList) {
            totalWeight += node.getWeight();
        }
        AlgorithmConfig.setTotalNodeWeight(totalWeight);
    }
}

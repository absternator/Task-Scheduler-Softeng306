package team17.DAG;

import java.util.*;

/**
 * This represents the input dot file in a graph to be used to schedule.
 */
public class Graph {
    private int _numOfProcessors; // TODO: 12/08/20 relocate if needed
    private List<Node> _nodeList;
    private Map<String,Node> _nodeLookup;

    public Graph() {
        _nodeList = new ArrayList<>();
        _nodeLookup = new HashMap<>();
    }

    public Node getNode(String id){
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
     * @param id the id of node to be added
     * @param weight weight of node to be added
     */
    public void addNode(String id,int weight){
        Node node = new Node(id,weight);
        _nodeList.add(node);
        _nodeLookup.put(id, node);

    }

    /**
     * This adds adges from one task to another, whilst setting up dependencies.
     * @param from Task from in string format
     * @param to Task to task in string format
     * @param edgeWeight Communication time from 2 tasks
     * @throws Exception Thrown if task not in graph already and edge is tried to be added.
     */
    public void addEdge(String from,String to, int edgeWeight) throws Exception {
        if(!_nodeLookup.containsKey(from) || !_nodeLookup.containsKey(to)){
            throw new Exception("ERROR: Node has to be instantiated before adding edge!");
        }
        Node fromNode = _nodeLookup.get(from);
        Node toNode = _nodeLookup.get(to);


        fromNode.setDependants(toNode);

        toNode.setIncomingEdges(fromNode,edgeWeight);
        toNode.setDependencies(fromNode);
    }
    public void addFinishNode(){
        Node finish = new Node("end",0);
        _nodeList.add(finish);
        _nodeLookup.put("end",finish);
        for (Node node: _nodeList) {
            if(node.getDependants().size() == 0 && !node.equals(finish)){
               node.setDependants(finish);
               finish.setIncomingEdges(node,0);
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
     * This parses the input dot file and adds tasks to graph.
     * @param line Each line of the dot file is passed in.
     */
    public void addGraph(String line){
        line = line.replace("Weight", "").replaceAll("[^A-Za-z0-9]", "");
        String[] array = line.split("");
        if (array.length == 2) { // this is adding a node
            String task = array[0];
            int weight = Integer.parseInt(array[1]);
            addNode(task,weight);
            //Assume node has been added in graph before adding edge
        } else {
            String from = array[0];
            String to = array[1];
            int edgeWeight = Integer.parseInt(array[2]);
            try {
                addEdge(from,to,edgeWeight);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * This sets the bottoom level for each task. Will be used as hueristic for algorithm.
     */
    public void setBottomLevel(){
        boolean progress = false;
        HashSet<Node> completed = new HashSet<>();
        LinkedList<Node> remaining = new LinkedList<>(_nodeList);
        while (!remaining.isEmpty()){
            for (Iterator<Node> it = remaining.descendingIterator(); it.hasNext();){
                Node node = it.next();
                if(completed.containsAll(node.getDependants())){
                    int bottomLevel = 0;
                    for (Node task : node.getDependants()) {
                        if(task.getBottomLevel() > bottomLevel){
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

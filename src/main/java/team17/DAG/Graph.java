package team17.DAG;

import java.util.*;

public class Graph {
    private int _numOfProcessors;
    private List<Node> _nodeList = new ArrayList<>();
    private Map<String,Node> _nodeLookup = new HashMap<>();

    public Node getNode(String id){
        return _nodeLookup.get(id);
    }

    public int get_numOfProcessors() {
        return _numOfProcessors;
    }

    public List<Node> get_NodeList() {
        return _nodeList;
    }

    public Map<String, Node> get_nodeLookup() {
        return _nodeLookup;
    }

    public void set_numOfProcessors(int numOfProcessors) {
        _numOfProcessors = numOfProcessors;
    }
    public void addNode(String id,int weight){
        Node node = new Node(id,weight);
        _nodeList.add(node);
        _nodeLookup.put(id, node);

    }
    public void addEdge(String from,String to, int edgeWeight) throws Exception {
        if(!_nodeLookup.containsKey(from) || !_nodeLookup.containsKey(to)){
            throw new Exception("ERROR: Node has to be instantiated before adding edge!");
        }
        Node fromNode = _nodeLookup.get(from);
        Node toNode = _nodeLookup.get(to);

        fromNode.set_outgoingEdges(toNode,edgeWeight);
        fromNode.set_dependants(toNode);

        toNode.set_incomingEdges(fromNode,edgeWeight);
        toNode.set_dependendicies(fromNode);
    }
    public void addFinishNode(){
        Node finish = new Node("end",0);
        _nodeList.add(finish);
        _nodeLookup.put("end",finish);
        for (Node node: _nodeList) {
            if(node.get_outgoingEdges().size() == 0 && !node.equals(finish)){
               node.set_outgoingEdges(finish,0);
               node.set_dependants(finish);
               finish.set_incomingEdges(node,0);
               finish.set_dependendicies(node);
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
    public void setBottomLevel(){
        boolean progress = false;
        HashSet<Node> completed = new HashSet<>();
        LinkedList<Node> remaining = new LinkedList<>(_nodeList);
        while (!remaining.isEmpty()){
            for (Iterator<Node> it = remaining.descendingIterator(); it.hasNext();){
                Node node = it.next();
                if(completed.containsAll(node.get_dependants())){
                    int bottomLevel = 0;
                    for (Node task : node.get_dependants()) {
                        if(task.get_bottomLevel() > bottomLevel){
                            bottomLevel = task.get_bottomLevel();
                        }
                    }
                    node.set_bottomLevel(bottomLevel + node.get_weight());
                    completed.add(node);
                    it.remove();
                    progress = true;
                }
            }
            if (!progress) throw new RuntimeException("\"Cyclic dependency, algorithm stopped!");

        }
    }
}

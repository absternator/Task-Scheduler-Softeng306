package com.company.DAG;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class DagGraph {

    private HashMap<DagNode,List<DagEdge>> dagGraph; //Stores graph as Nodes -> List of edges

    public DagGraph(){
        dagGraph = new HashMap<>();
    }
    // Add node to graph
    public void addDagNode(DagNode node){
        if (!dagGraph.containsKey(node))
       dagGraph.put(node,new LinkedList<>());
    }
    // Adds edges to graph
    public void addDagEdge(DagNode from, DagNode to, int edgeWeight) throws Exception {
        if (!dagGraph.containsKey(from)) // if node has not been instantiated throw exception
            throw new Exception("Node has to be instantiated before adding edge");

        dagGraph.get(from).add(new DagEdge(from,to,edgeWeight));
    }

    public HashMap<DagNode, List<DagEdge>> getDagGraph() {
        return dagGraph;
    }

    @Override
    // Printing graph
    public String toString() {
        StringBuilder data = new StringBuilder();
        for(DagNode key : dagGraph.keySet()){
            data.append(key).append("==> [ ").append(dagGraph.get(key)).append("]\n");
        }
        return data.toString();
    }
    // This function takes input dot file and adds nodes and edges to graph object
    public  void addGraph(String s,HashMap<String,Integer> nodeCounter){
        s = s.replace("Weight","").replaceAll("[^A-Za-z0-9]","");
        String[] array =  s.split("");
        if (array.length == 2){ // this is adding a node
            String task = array[0];
            int weight= Integer.parseInt(array[1]);
            DagNode node = new DagNode(task,weight);
            nodeCounter.put(task,weight);
            addDagNode(node);
            //Assume node has been added in graph before adding edge
        } else {
            String from = array[0];
            String to = array[1];
            int edgeWeight = Integer.parseInt(array[2]);
            DagNode fromNode = new DagNode(from,nodeCounter.get(from));
            DagNode toNode = new DagNode(to,nodeCounter.get(to));
            try {
                addDagEdge(fromNode,toNode, edgeWeight);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println(e.getMessage());
            }
        }
    }
}

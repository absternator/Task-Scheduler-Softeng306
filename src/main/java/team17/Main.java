package team17;


import team17.DAG.DagGraph;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;


public class Main {


    public static void main(String[] args) throws IOException {
        //This reads the graph from the dot file
        DagGraph graph = new DagGraph();
        HashMap<String,Integer> nodeCounter = new HashMap<>(); //stores nodes passed into graph
        File file = new File("src/main/resources/graph.dot");
        BufferedReader br = new BufferedReader(new FileReader(file));
        br.readLine();
        String s;
        while (!(s = br.readLine()).equals("}")) {
            graph.addGraph(s,nodeCounter);
        }
        System.out.println(graph);

    }

}





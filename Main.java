package com.company;

import com.company.DAG.DagGraph;


import java.io.*;
import java.util.HashMap;


public class Main {


    public static void main(String[] args) throws IOException {
        //This reads the graph from the dot file
        DagGraph graph = new DagGraph();
        HashMap<String,Integer> nodeCounter = new HashMap<>(); //stores nodes passed into graph
        File file = new File("src/com/company/graph.dot");
        BufferedReader br = new BufferedReader(new FileReader(file));
        br.readLine();
        String s;
        while (!(s = br.readLine()).equals("}")) {
            graph.addGraph(s,nodeCounter);
        }
        System.out.println(graph);

    }

}





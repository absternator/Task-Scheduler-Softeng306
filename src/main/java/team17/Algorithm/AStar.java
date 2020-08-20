package team17.Algorithm;

import team17.DAG.Graph;

import java.util.*;

/**
 * Class that contains the main skeleton of the A* algorithm
 */
public class AStar extends Algorithm {
    private final PartialSolution _root;

    public AStar(Graph graph) {
        _root = new PartialSolution(null, null);
    }

    @Override
    public PartialSolution getOptimalSchedule(Graph graph) {
        Queue<PartialSolution> open = new PriorityQueue<>();
        List<PartialSolution> closed = new ArrayList<>();
        open.add(_root);
        while (!open.isEmpty()) {
            PartialSolution partialSolution = open.poll();
            closed.add(partialSolution);
            if (partialSolution.isCompleteSchedule()) {
                return partialSolution;
            }
            Set<PartialSolution> children = expandSearch(partialSolution,graph);
            open.addAll(children);
            // TODO: 12/08/20 Need to implement partial solution equal to use 
//            for (PartialSolution child : children) {
//                if (!closed.contains(child)){
//                    open.offer(child);
//                }
//            }
        }

        return null;
    }

}

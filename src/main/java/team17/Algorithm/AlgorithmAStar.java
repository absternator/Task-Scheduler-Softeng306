package team17.Algorithm;

import team17.DAG.Graph;

import java.util.*;

public class AlgorithmAStar {
    private final PartialSolution _root;
    private Graph _graph;

    public AlgorithmAStar(Graph graph) {
        _graph = graph;
        _root = new PartialSolution(null,graph,null);
    }

    /**
     * This is the actual A* Algorithm that returns the optimal schedule
     * @return The full Schedule which is the optimal solution
     */
    public List<ScheduledTask> getOptimalSchedule(){
        Queue<PartialSolution> OPEN = new PriorityQueue<>();
        List<PartialSolution> CLOSED = new ArrayList<>();
        OPEN.add(_root);
        while(!OPEN.isEmpty()){
            PartialSolution partialSolution = OPEN.poll();
            CLOSED.add(partialSolution);
            if(partialSolution.isCompleteSchedule()){
                return partialSolution.fullSchedule();
            }
            Set<PartialSolution> children = partialSolution.expandSearch();
            OPEN.addAll(children);
            // TODO: 12/08/20 Need to implement partial solution equal to use 
//            for (PartialSolution child : children) {
//                if (!CLOSED.contains(child)){
//                    OPEN.offer(child);
//                }
//            }
        }

        return null;
    }



}

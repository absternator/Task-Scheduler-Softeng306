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

    public List<ScheduledTask> getOptimalSchedule(){

        Queue<PartialSolution> OPEN = new PriorityQueue<>(Comparator.comparingInt(PartialSolution::getCostUnderestimate));
        List<PartialSolution> CLOSED = new ArrayList<>();
        OPEN.add(_root);
        if(!OPEN.isEmpty()){
            PartialSolution partialSolution = OPEN.poll();
            CLOSED.add(partialSolution);
            if(partialSolution.isCompleteSchedule()){
                System.out.println(partialSolution.isCompleteSchedule());
                return partialSolution.fullSchedule();

            }
            Set<PartialSolution> children = partialSolution.expandSearch();
            for (PartialSolution child: children) {
                if (!CLOSED.contains(child)){
                    OPEN.offer(child);
                }
            }
        }

        return null;
    }



}

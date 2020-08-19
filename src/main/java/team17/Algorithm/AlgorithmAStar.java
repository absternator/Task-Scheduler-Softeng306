package team17.Algorithm;

import team17.DAG.Graph;

import java.util.*;

/**
 * Class that contains the main skeleton of the A* algorithm
 */
public class AlgorithmAStar {
    private final PartialSolution _root;

    public AlgorithmAStar(Graph graph) {
        _root = new PartialSolution(null, graph, null);
    }
    public Set<PartialSolution> setRoot(){
        return _root.expandRoot();
    }
    /**
     * This is the actual A* Algorithm that returns the optimal schedule
     *
     * @return The full Schedule which is the optimal solution
     */
    public List<ScheduledTask> getOptimalSchedule() {
        List<PartialSolution> closed = new ArrayList<>();
        Queue<PartialSolution> open = new PriorityQueue<>(_root.expandRoot());
        while (!open.isEmpty()) {
            PartialSolution partialSolution = open.poll();
            closed.add(partialSolution);
            if (partialSolution.isCompleteSchedule()) {
                return partialSolution.fullSchedule();
            }
            Set<PartialSolution> children = partialSolution.expandSearch();
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

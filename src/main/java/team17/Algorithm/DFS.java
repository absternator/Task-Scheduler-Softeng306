package team17.Algorithm;

import team17.DAG.Graph;

import java.util.Set;
import java.util.Stack;

public class DFS extends Algorithm {
    private final PartialSolution _root;

    public DFS(Graph graph) {
        _root = new PartialSolution(null, null);
    }

    @Override
    public PartialSolution getOptimalSchedule(Graph graph) {
        ListScheduling ls = new ListScheduling(graph);
        PartialSolution bestSchedule = ls.getSchedule();
        Set<PartialSolution> children;
        int upperBound = bestSchedule.getCostUnderestimate();
        Stack<PartialSolution> open = new Stack<>();
        open.push(_root);
        while (!open.isEmpty()) {
            PartialSolution partialSolution = open.pop();
            int costSoFar = partialSolution.getCostUnderestimate();
            if (partialSolution.isCompleteSchedule() && costSoFar < upperBound) { //TODO && !bestSchedule.equals(partialSolution)
                upperBound = costSoFar;
                bestSchedule = partialSolution;
            } else {
                children = expandSearch(partialSolution,graph);
                for (PartialSolution child:children) {
                    int cost = child.getCostUnderestimate();
                    if (cost < upperBound) {
                        open.push(child);
                    }
                }
            }
        }
        return bestSchedule;
    }

    @Override
    public PartialSolution getOptimalScheduleParallel(Graph graph, int nCores) {
        return null;
    }


}

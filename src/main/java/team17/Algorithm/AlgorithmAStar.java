package team17.Algorithm;

import team17.DAG.Graph;

import java.util.*;

public class AlgorithmAStar {
    private final PartialSolution _root;

    public AlgorithmAStar(Graph graph) {
        _root = new PartialSolution(null,graph,null);
    }

    /**
     * This is the actual A* Algorithm that returns the optimal schedule
     * @return The full Schedule which is the optimal solution
     */
    public List<ScheduledTask> getOptimalSchedule(){
        //queue count used for testing purposes only(num of children put in open)
        int queueCount = 1;
        int upperBound = Integer.MAX_VALUE;
        Queue<PartialSolution> open = new PriorityQueue<>();
        List<PartialSolution> closed = new ArrayList<>();
        open.add(_root);
        while(!open.isEmpty()){
            PartialSolution partialSolution = open.poll();
            closed.add(partialSolution);
            if(partialSolution.isCompleteSchedule()){
                System.out.println("OPEN Queue count: " + queueCount);
                System.out.println("OPEN remaining count: " + open.size());
                return partialSolution.fullSchedule();
            }
            Set<PartialSolution> children = partialSolution.expandSearch();
            // Test if works(equals)
//            for (PartialSolution c :children) {
//                queueCount++;
//            }
//           open.addAll(children);

            for (PartialSolution child : children) {
                if (child.isCompleteSchedule()){
                    int childCost = child.getCostUnderestimate();
                    if(childCost < upperBound){
                        upperBound = childCost;
                        System.out.println("new upper bound is: " + upperBound);
                    }
                }
                if (!closed.contains(child) && child.getCostUnderestimate() <= upperBound){
                    queueCount++;
                    open.offer(child);
                }
            }
        }
        return null;
    }



}

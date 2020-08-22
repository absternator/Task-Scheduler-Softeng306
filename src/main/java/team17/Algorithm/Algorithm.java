package team17.Algorithm;

import team17.DAG.Graph;
import team17.DAG.Node;

import java.util.HashSet;
import java.util.Set;

public abstract class Algorithm {

//    /**
//     * Initialise the root of the graph
//     */
//    public abstract void initialise(Graph graph);

    /**
     * Method to return the optimal solution for a given graph input
     * @param graph the input graph of tasks
     * @return a collection of scheduled tasks representing the optimal solution
     */
    public abstract PartialSolution getOptimalSchedule(Graph graph);

    /**
     * Method to return the optimal solution for a given graph input for multiple cores
     * @param graph
     * @param nCores
     * @return
     */
    public abstract PartialSolution getOptimalScheduleParallel(Graph graph, int nCores);

    /**
     * This method expands the state space tree getting children. It adds tasks to processors.
     *
     * @return A set of partial solutions are returns. These are the children of the current solution.
     */
    public Set<PartialSolution> expandSearch(PartialSolution partialSolution, Graph graph) {
        Set<PartialSolution> children = new HashSet<>();

        Set<Node> nodesInSchedule = new HashSet<>();
        for (ScheduledTask scheduledTask : partialSolution) {
            nodesInSchedule.add(scheduledTask.getNode());
        }

        AddNode:
        for (Node node : graph.getNodeList()) {
            // If node is already in schedule cant add
            if (nodesInSchedule.contains(node)) {
                continue;
            }
            //if dependency not in schedule
            for (Node dependency : node.getDependencies()) {
                if (!nodesInSchedule.contains(dependency)) {
                    continue AddNode;
                }
            }

            //Node can be placed on Processor now
            for (int i = 1; i < graph.getNumOfProcessors() + 1; i++) {
                int eligibleStartime = 0;
                // Start time based on  last task on this processor
                for (ScheduledTask scheduledTask : partialSolution) {
                    if (scheduledTask.getProcessorNum() == i) {
                        eligibleStartime = scheduledTask.getFinishTime();
                        break;
                    }
                }
                //Start time based on dependencies on  OtherProcessors
                for (ScheduledTask scheduledTask : partialSolution) {
                    if (scheduledTask.getProcessorNum() != i) {
                        boolean dependantFound = false;
                        int communicationTime = 0;
                        for (Node edge : node.getIncomingEdges().keySet()) {
                            if (edge.equals(scheduledTask.getNode())) {
                                dependantFound = true;
                                communicationTime = node.getIncomingEdges().get(edge);
                            }
                        }
                        if (!dependantFound) {
                            continue;
                        }
                        eligibleStartime = Math.max(eligibleStartime, scheduledTask.getFinishTime() + communicationTime);
                    }
                }
                children.add(new PartialSolution(partialSolution, new ScheduledTask(i, node, eligibleStartime)));
            }
        }
        return children;
    }

}
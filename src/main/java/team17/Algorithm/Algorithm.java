package team17.Algorithm;

import team17.DAG.Graph;
import team17.DAG.Node;

import java.util.*;

public abstract class Algorithm {

//    /**
//     * Initialise the root of the graph
//     */
//    public abstract void initialise(Graph graph);

    /**
     * Method to return the optimal solution for a given graph input
     *
     * @param graph the input graph of tasks
     * @return a collection of scheduled tasks representing the optimal solution
     */
    public abstract PartialSolution getOptimalSchedule(Graph graph);

    /**
     * Method to return the optimal solution for a given graph input for multiple cores
     *
     * @param graph
     * @param nCores
     * @return
     */
    public abstract PartialSolution getOptimalScheduleParallel(Graph graph, int nCores);

    /**
     * This expands the root and assigns the first task to the first processor
     *
     * @return Set of Partial solutions where first node is placed on first processor
     */
    public Set<PartialSolution> expandRoot(PartialSolution partialSolution, Graph graph) {
        Set<PartialSolution> children = new HashSet<>();
        Set<Node> notEligible = new HashSet<>();
        List<Node> freeNodes = new ArrayList<>();
        for (Node node : graph.getNodeList()) {
            if (node.getDependencies().size() == 0) {
                freeNodes.add(node);
            }
        }
        /********************This line implements fork-join*************************/
        FixedTaskOrder(partialSolution, notEligible, freeNodes);
        /********************This line implements fork-join*************************/
        for (Node node: freeNodes) {
            children.add(new PartialSolution(partialSolution, new ScheduledTask(1, node, 0)));
        }
        return children;
    }

    /**
     * This is used to check if fork-join/independent task criteria is met. if yes we fix schedule task.
     * @param partialSolution This instance of Partial solution
     * @param notEligible These are the nodes in schedule currently
     * @param freeNodes Nodes that can be scheduled
     */
    private void FixedTaskOrder(PartialSolution partialSolution, Set<Node> notEligible, List<Node> freeNodes) {
        Node fixedTask = forkJoin(freeNodes, partialSolution);
        if (fixedTask != null) {
            for (Node notFixedNode : freeNodes) {
                if (!notFixedNode.equals(fixedTask)) {
                    notEligible.add(notFixedNode);
                }
            }
            freeNodes.removeAll(notEligible); //remove all expect node to be scheduled
            notEligible.clear();
        }
    }

    /**
     * This method expands the state space tree getting children. It adds tasks to processors.
     *
     * @return A set of partial solutions are returns. These are the children of the current solution.
     */
    public Set<PartialSolution> expandSearch(PartialSolution partialSolution, Graph graph) {
        Set<PartialSolution> children = new HashSet<>();
        Set<Node> nodesInSchedule = new HashSet<>();
        List<Node> freeNodes = new ArrayList<>(graph.getNodeList()); //nodes that are `eligible to be scheduled
        Set<Node> notEligible = new HashSet<>();
        //Go through and remove indelible nodes
        for (ScheduledTask scheduledTask : partialSolution) {
            nodesInSchedule.add(scheduledTask.getNode());
            freeNodes.remove(scheduledTask.getNode());
        }
        for (Node node : freeNodes) {
            for (Node dependency : node.getDependencies()) {
                if (!nodesInSchedule.contains(dependency)) {
                    notEligible.add(node);
                }
            }
        }
        freeNodes.removeAll(notEligible);
        // Check if free tasks meet criteria. IF yes return node to be ordered next.
        /********************This line implements fork-join*************************/
        FixedTaskOrder(partialSolution, notEligible, freeNodes);
        /********************This line implements fork-join*************************/

        AddNode:
        for (Node node : freeNodes) {
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

    protected static Node forkJoin(List<Node> freeNodes, PartialSolution partialSolution) {
        Set<Node> sameChild = new HashSet<>();
        Set<Integer> sameParentProcessor = new HashSet<>();
        Map<Node, Integer> dataReadyMap = new HashMap<>();
        for (Node node : freeNodes) {
            Set<Node> dependencies = node.getDependencies();
            Set<Node> dependents = node.getDependants();
            // check if node has at max 1 parent and 1 child
            if (dependencies.size() > 1 || dependents.size() > 1) {
                return null;
            }
            // checks to make sure all children of nodes are the same
            if (dependents.size() == 1) {
                sameChild.addAll(dependents);
            }

            if (sameChild.size() > 1) {
                return null;
            }
            //checks if node has parent, they are all scheduled on same processor
            if (dependencies.size() == 1) {
                for (ScheduledTask task : partialSolution) {
                    if (dependencies.contains(task.getNode())) {
                        sameParentProcessor.add(task.getProcessorNum());
                        int dataReadyTime = task.getFinishTime() + node.getIncomingEdges().get(task.getNode());
                        dataReadyMap.put(node,dataReadyTime);
                    }
                }
            } else {
                dataReadyMap.put(node, 0);
            }
            if (sameParentProcessor.size() > 1) {
                return null;
            }
        }
        //now we can try order the nodes!!! as it fits fixed order Condition
        freeNodes.sort((o1, o2) -> {
            //sort by increasing data ready time
            int incomingData = dataReadyMap.get(o1) - dataReadyMap.get(o2);
            //tie breaker: sort by decreasing outgoing edges
            if(incomingData == 0){
                int firstNodeOutgoing = 0;
                int secondNodeOutgoing = 0;
                //if has child, then order by decreasing communication cost. Else =0.
                if(o1.getDependants() != null){
                    for (Map.Entry<Node,Integer> entry : o1.getOutgoingEdges().entrySet()) {
                        firstNodeOutgoing = entry.getValue();
                    }
                }
                if(o2.getDependants() != null){
                    for (Map.Entry<Node,Integer> entry : o2.getOutgoingEdges().entrySet()) {
                        secondNodeOutgoing = entry.getValue();
                    }
                }
                return secondNodeOutgoing - firstNodeOutgoing;
            } else {
                return incomingData;
            }
        });
        //check if free nodes sorted by decreasing outgoing edge weights
        int maxValue = Integer.MAX_VALUE;
        for (Node node: freeNodes) {
            for (Map.Entry<Node,Integer> entry : node.getOutgoingEdges().entrySet()) {
                int value = entry.getValue();
                if (value <= maxValue){
                    maxValue = value;
                } else {
                    return null;
                }
            }
        }
        return freeNodes.get(0);
    }

}

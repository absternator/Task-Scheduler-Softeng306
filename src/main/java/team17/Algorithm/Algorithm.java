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
    public PartialSolution getOptimalSchedule(Graph graph) {
        int count = 0;
        while (true) {
            PartialSolution partialSolution = this.getNextPartialSolution();
            if (partialSolution == null) {
                break;
            } else {
                Set<PartialSolution> children = expandSearch(partialSolution, graph);
                this.openAddChildren(children);
                count ++;
//                if(count%1000==0){
//                    System.out.print("\r dfs popped solutions:" + count);
//                }
            }
        }
        System.out.print("DFS: popped solutions:" + count);
        return getSolution();
    }

    /**
     * Method to return the optimal solution for a given graph input for multiple cores
     *
     * @param graph  The whole graph
     * @param nCores Number of cores specified for parallelisation
     * @return The complete partial solution which is optimal
     */
    public PartialSolution getOptimalScheduleParallel(Graph graph, int nCores) {
        List<NThreads> nThreads = new ArrayList<>();
        for (int i = 0; i < nCores; i++) {
            NThreads thread = new NThreads(this, graph);
            thread.start();
            nThreads.add(thread);
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        this.getOptimalSchedule(graph);
        for (NThreads thr : nThreads) {
            try {
                thr.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return this.getSolution();
    }

    /**
     * Returns the completed solution stored in the field
     *
     * @return The complete partial solution
     */
    protected abstract PartialSolution getSolution();

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
        // Check if free tasks meet criteria. IF yes return node to be ordered next.
        fixedTaskOrder(partialSolution, notEligible, freeNodes);

        for (Node node : freeNodes) {
            children.add(new PartialSolution(partialSolution, new ScheduledTask(1, node, 0)));
        }
        return children;
    }

    /**
     * This is used to check if fork-join/independent task criteria is met. if yes we fix schedule task.
     *
     * @param partialSolution This instance of Partial solution
     * @param notEligible     null set used to transfer contents from freeNodes and then remove later in method
     * @param freeNodes       Nodes that are eligible to  be scheduled
     */
    protected void fixedTaskOrder(PartialSolution partialSolution, Set<Node> notEligible, List<Node> freeNodes) {
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
        List<Node> freeNodes = new ArrayList<>(graph.getNodeList()); //nodes that are eligible to be scheduled
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
        notEligible.clear();
        // Check if free tasks meet criteria. IF yes return node to be ordered next.
        fixedTaskOrder(partialSolution, notEligible, freeNodes);

        AddNode:
        for (Node node : freeNodes) {

            // if a sibling has already scheduled an equivalent node
            for (PartialSolution child:children){
                if(child.getScheduledTask().getNode().isEquivalent(node)){
                    continue AddNode;
                }
            }
            //Node can be placed on Processor now
            for (int i = 1; i < AlgorithmConfig.getNumOfProcessors() + 1; i++) {
                int eligibleStartTime = 0;
                // Start time based on  last task on this processor
                for (ScheduledTask scheduledTask : partialSolution) {
                    if (scheduledTask.getProcessorNum() == i) {
                        eligibleStartTime = scheduledTask.getFinishTime();
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
                        eligibleStartTime = Math.max(eligibleStartTime, scheduledTask.getFinishTime() + communicationTime);
                    }
                }
                children.add(new PartialSolution(partialSolution, new ScheduledTask(i, node, eligibleStartTime)));
            }
        }
        return children;
    }

    protected static Node forkJoin(List<Node> freeNodes, PartialSolution partialSolution) {
        if (freeNodes.isEmpty()) return null;
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
                        dataReadyMap.put(node, dataReadyTime);
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
            if (incomingData == 0) {
                int firstNodeOutgoing = 0;
                int secondNodeOutgoing = 0;
                //if has child, then order by decreasing communication cost. Else =0.
                if (!o1.getDependants().isEmpty()) {
                    Node first = o1.getDependants().iterator().next();
                    firstNodeOutgoing = first.getIncomingEdges().get(o1);
                }
                if (!o2.getDependants().isEmpty()) {
                    Node second = o2.getDependants().iterator().next();
                    secondNodeOutgoing = second.getIncomingEdges().get(o2);
                }
                return secondNodeOutgoing - firstNodeOutgoing;
            } else {
                return incomingData;
            }
        });
        //check if free nodes sorted by decreasing outgoing edge weights
        int maxValue = Integer.MAX_VALUE;
        for (Node node : freeNodes) {
            int value = 0;
            for (Node item : node.getDependants()) {
                value = item.getIncomingEdges().get(node);
            }
            if (value <= maxValue) {
                maxValue = value;
            } else {
                return null;
            }
        }
        return freeNodes.get(0);
    }

    /**
     * Gets the next partial solution, this is a synchronised method
     *
     * @return The next partial solution in the queue/stack, or null if there is none
     */
    public abstract PartialSolution getNextPartialSolution();

    /**
     * Adds children to open, this is a synchronised method
     *
     * @param children The children needed to be added to open
     */
    public abstract void openAddChildren(Set<PartialSolution> children);
}

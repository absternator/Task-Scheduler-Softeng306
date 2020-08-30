package team17.Algorithm;

import team17.DAG.DAGGraph;
import team17.DAG.DAGNode;

import java.util.*;

public abstract class Algorithm {

    protected AlgorithmState _algorithmState;
    protected PartialSolution _bestCompletePartialSolution;

    public Algorithm(AlgorithmState algorithmState) {
        _algorithmState = algorithmState;
    }

    /**
     * Method to return the optimal solution for a given graph input
     *
     * @param graph the input graph of tasks
     * @return a collection of scheduled tasks representing the optimal solution
     */
    public abstract PartialSolution getOptimalSchedule(DAGGraph graph);

    /**
     * This method expands the state space tree getting children. It adds tasks to processors.
     *
     * @return A set of partial solutions are returns. These are the children of the current solution.
     */
    protected abstract Set<PartialSolution> expandSearch(PartialSolution partialSolution, DAGGraph graph);

    /**
     * Gets the next partial solution, this is a synchronised method
     *
     * @return The next partial solution in the queue/stack, or null if there is none
     */
    protected abstract PartialSolution getNextPartialSolution();

    /**
     * Adds children to open, this is a synchronised method
     *
     * @param children The children needed to be added to open
     */
    protected abstract void openAddChildren(Set<PartialSolution> children);

    /**
     * Method to return the optimal solution for a given graph input for multiple cores
     *
     * @param graph  The whole graph
     * @param nCores Number of cores specified for parallelisation
     * @return The complete partial solution which is optimal
     */
    public PartialSolution getOptimalScheduleParallel(DAGGraph graph, int nCores) {
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
    protected PartialSolution getSolution() {
        return _bestCompletePartialSolution;
    }

    /**
     * This expands the root and assigns the first task to the first processor
     *
     * @return Set of Partial solutions where first node is placed on first processor
     */
    protected Set<PartialSolution> expandRoot(PartialSolution partialSolution, DAGGraph graph) {
        Set<PartialSolution> children = new HashSet<>();
        Set<DAGNode> notEligible = new HashSet<>();
        List<DAGNode> freeNodes = new ArrayList<>();
        for (DAGNode node : graph.getNodeList()) {
            if (node.getDependencies().size() == 0) {
                freeNodes.add(node);
            }
        }
        // Check if free tasks meet criteria. IF yes return node to be ordered next.
        fixedTaskOrder(partialSolution, notEligible, freeNodes);

        for (DAGNode node : freeNodes) {
            children.add(new PartialSolution(partialSolution, new ScheduledTask(1, node, 0)));
        }
        if (_algorithmState != null) {
            _algorithmState.updateNumUnexpandedPartialSolutions(children.size());
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
    protected void fixedTaskOrder(PartialSolution partialSolution, Set<DAGNode> notEligible, List<DAGNode> freeNodes) {
        DAGNode fixedTask = forkJoin(freeNodes, partialSolution);
        if (fixedTask != null) {
            for (DAGNode notFixedNode : freeNodes) {
                if (!notFixedNode.equals(fixedTask)) {
                    notEligible.add(notFixedNode);
                }
            }
            freeNodes.removeAll(notEligible); //remove all except node to be scheduled
            if(_algorithmState != null) {
                _algorithmState.updateNumPruned(notEligible.size());
            }
            notEligible.clear();
        }
    }

    protected static DAGNode forkJoin(List<DAGNode> freeNodes, PartialSolution partialSolution) {
        if (freeNodes.isEmpty()) return null;
        Set<DAGNode> sameChild = new HashSet<>();
        Set<Integer> sameParentProcessor = new HashSet<>();
        Map<DAGNode, Integer> dataReadyMap = new HashMap<>();
        for (DAGNode node : freeNodes) {
            Set<DAGNode> dependencies = node.getDependencies();
            Set<DAGNode> dependents = node.getDependants();
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
                    DAGNode first = o1.getDependants().iterator().next();
                    firstNodeOutgoing = first.getIncomingEdges().get(o1);
                }
                if (!o2.getDependants().isEmpty()) {
                    DAGNode second = o2.getDependants().iterator().next();
                    secondNodeOutgoing = second.getIncomingEdges().get(o2);
                }
                return secondNodeOutgoing - firstNodeOutgoing;
            } else {
                return incomingData;
            }
        });
        //check if free nodes sorted by decreasing outgoing edge weights
        int maxValue = Integer.MAX_VALUE;
        for (DAGNode node : freeNodes) {
            int value = 0;
            for (DAGNode item : node.getDependants()) {
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

}

package team17.Algorithm;

import team17.DAG.Graph;
import team17.DAG.Node;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
        while (true) {
            PartialSolution partialSolution = this.getNextPartialSolution();
            if (partialSolution == null) {
                break;
            } else {
                Set<PartialSolution> children = expandSearch(partialSolution,graph);
                this.openAddChildren(children);
            }
        }
        return getSolution();
    }

    /**
     * Method to return the optimal solution for a given graph input for multiple cores
     *
     * @param graph The whole graph
     * @param nCores Number of cores specified for parallelisation
     * @return The complete partial solution which is optimal
     */
    public PartialSolution getOptimalScheduleParallel(Graph graph, int nCores) {
        List<NThreads> nThreads = new ArrayList<>();
        for(int i=0; i<nCores; i++){
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
        for(NThreads thr : nThreads){
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

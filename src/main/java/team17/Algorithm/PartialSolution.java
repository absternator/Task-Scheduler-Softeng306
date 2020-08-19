package team17.Algorithm;

import team17.DAG.Graph;
import team17.DAG.Node;

import java.util.*;

/**
 * This represents each partial solution created for the state space search.
 */
public class PartialSolution implements Iterable<ScheduledTask>, Comparable<PartialSolution> {
    private final PartialSolution _parent;
    private final Graph _graph; // TODO: 12/08/20 not sure if we need can we move?(Refactoring)
    private final ScheduledTask _scheduledTask;

    public PartialSolution(PartialSolution parent, Graph graph, ScheduledTask scheduledTask) {
        _parent = parent;
        _graph = graph;
        _scheduledTask = scheduledTask;

    }

    /**
     * This method gets this instances parent
     *
     * @return Parent partial Solution
     */
    public PartialSolution getParent() {
        return _parent;
    }

    public Graph getGraph() {
        return _graph;
    }

    public ScheduledTask getScheduledTask() {
        return _scheduledTask;
    }

    /**
     * This method returns the underestimate cost to finish schedule from a partial schedule
     *
     * @return The underestimate cost to finish the schedule
     */
    public int getCostUnderestimate() {
        int costUnderestimate = 0;
        for (ScheduledTask scheduledTask : this) {
            costUnderestimate = Math.max(costUnderestimate, scheduledTask.getStartTime() + scheduledTask.getNode().getBottomLevel());
        }
        return costUnderestimate;
    }

    /**
     * This expands the root and assigns the first task to the first processor
     * @return Set of Partial solutions where first node is placed on first processor
     */
    public Set<PartialSolution> expandRoot(){
        Set<PartialSolution> children = new HashSet<>();
        for (Node node:_graph.getNodeList()) {
            if(node.getDependencies().size() == 0){
                children.add(new PartialSolution(this,_graph,new ScheduledTask(1,node,0)));
            }
        }
        return children;
    }
    /**
     * This method expands the state space tree getting children. It adds tasks to processors.
     *
     * @return A set of partial solutions are returns. These are the children of the current solution.
     */
    public Set<PartialSolution> expandSearch() {
        Set<PartialSolution> children = new HashSet<>();

        Set<Node> nodesInSchedule = new HashSet<>();
        for (ScheduledTask scheduledTask : this) {
            nodesInSchedule.add(scheduledTask.getNode());
        }

        AddNode:
        for (Node node : _graph.getNodeList()) {
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
            for (int i = 1; i < _graph.getNumOfProcessors() + 1; i++) {
                int eligibleStartime = 0;
                // Start time based on  last task on this processor
                for (ScheduledTask scheduledTask : this) {
                    if (scheduledTask.getProcessorNum() == i) {
                        eligibleStartime = scheduledTask.getFinishTime();
                        break;
                    }
                }
                //Start time based on dependencies on  OtherProcessors
                for (ScheduledTask scheduledTask : this) {
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
                children.add(new PartialSolution(this, _graph, new ScheduledTask(i, node, eligibleStartime)));
            }
        }
        return children;
    }

    /**
     * This method checks if a complete schedule is return
     *
     * @return Returns true if full schedule, else false
     */
    public boolean isCompleteSchedule() {
        if (this.getScheduledTask() != null) {
            return this.getScheduledTask().getNode().getId().equals("end");
        } else return false;
    }

    /**
     * This constructs the full schedule
     *
     * @return Returns the full schedule
     */
    public List<ScheduledTask> fullSchedule() {
        List<ScheduledTask> scheduledTaskList = new LinkedList<>();
        for (ScheduledTask task : this) {
            if (!task.getNode().getId().equals("end")) {
                scheduledTaskList.add(task);
            }
        }
        return scheduledTaskList;
    }

    /**
     * This iterator iterates from the current partial solution to the root partial solution to get all tasks
     *
     * @return This returns new Iterator instance.
     */
    @Override
    public Iterator<ScheduledTask> iterator() {
        return new Iterator<ScheduledTask>() {
            private PartialSolution current = PartialSolution.this;

            @Override
            public boolean hasNext() {
                return current.getScheduledTask() != null;
            }

            @Override
            public ScheduledTask next() {
                ScheduledTask thisTask = current.getScheduledTask();
                if (current.getParent() != null) {
                    current = current.getParent();
                }
                return thisTask;
            }
        };
    }

    /**
     * This metod compares partial solutions dependant on cost underestimate.This is used to order the priority queue in the A* Algorithm.
     *
     * @param other Partial solution being compared to.
     * @return Int value to indicate which partial solution has has a lower cost underestimate.
     */
    @Override
    public int compareTo(PartialSolution other) {
        return this.getCostUnderestimate() - other.getCostUnderestimate();
    }

// TODO: 12/08/20 come back and do equals method & hashcode again because still slow?
//test if work
    /**
     * This method checks if two partial solutions are equal
     * @param other The other partial solution being checked
     * @return Boolean to indicate if both partial solutions are equal
     */
    // go through each one and check
    @Override
    public boolean equals(Object other) {
        Set<ScheduledTask> thisSolution = new HashSet<>();
        this.forEach(thisSolution::add);
        //while building other solution if if adding task is in THIS sol,return false if not else keep adding.
        for (ScheduledTask scheduledTask: (PartialSolution)other) {
            if (!thisSolution.contains(scheduledTask)){
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        return _scheduledTask.hashCode();
    }
}

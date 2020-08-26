package team17.Algorithm;

import java.util.*;

/**
 * This represents each partial solution created for the state space search.
 */
public class PartialSolution implements Iterable<ScheduledTask>, Comparable<PartialSolution> {
    private final PartialSolution _parent;
    private final ScheduledTask _scheduledTask;

    private String _lastPartialExpansionNodeId;
    private int _lastPartialExpansionProcessor;

    public PartialSolution(PartialSolution parent, ScheduledTask scheduledTask) {
        _parent = parent;
        _scheduledTask = scheduledTask;
        _lastPartialExpansionNodeId = "";
        _lastPartialExpansionProcessor = 0;
    }

    /**
     * This method gets this instances parent
     *
     * @return Parent partial Solution
     */
    public PartialSolution getParent() {
        return _parent;
    }

    public ScheduledTask getScheduledTask() {
        return _scheduledTask;
    }

    public String getLastPartialExpansionNodeId() {
        return _lastPartialExpansionNodeId;
    }

    public void setLastPartialExpansionNodeId(String id) {
        _lastPartialExpansionNodeId = id;
    }

    public int getLastPartialExpansionProcessor() {
        return _lastPartialExpansionProcessor;
    }

    public void setLastPartialExpansionProcessor(int processor) {
        _lastPartialExpansionProcessor = processor;
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
     * This method compares partial solutions dependant on cost underestimate.This is used to order the priority queue in the A* Algorithm.
     *
     * @param other Partial solution being compared to.
     * @return Int value to indicate which partial solution has a lower cost underestimate.
     *          If cost underestimates are the same, it returns a value to indicate which one has
     *          more tasks scheduled on it.
     */
    @Override
    public int compareTo(PartialSolution other) {
        if (this.getCostUnderestimate() == other.getCostUnderestimate()) {
            int numTasksThis = 0;
            int numTasksOther = 0;
            for (ScheduledTask task : this) {
                numTasksThis++;
            }
            for (ScheduledTask task : other) {
                numTasksOther++;
            }
            return numTasksOther - numTasksThis;
        }
        return this.getCostUnderestimate() - other.getCostUnderestimate();
    }
// TODO: 26/08/20 Still will update futher 

    /**
     * This method checks if two partial solutions are equal
     *
     * @param other The other partial solution being checked
     * @return Boolean to indicate if both partial solutions are equal
     */
    // go through each one and check
    @Override
    public boolean equals(Object other) {
        Set<ScheduledTask> thisSolution = new HashSet<>();

        for (ScheduledTask task : this) {
            thisSolution.add(task);
        }
        //while building other solution if if adding task is in THIS solution,return false if not else keep adding.
        for (ScheduledTask scheduledTask : (PartialSolution) other) {
            if (!thisSolution.contains(scheduledTask)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(_scheduledTask, _parent);
    }
}

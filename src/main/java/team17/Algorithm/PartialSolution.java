package team17.Algorithm;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * This represents each partial solution created for the state space search.
 */
public class PartialSolution implements Iterable<ScheduledTask>, Comparable<PartialSolution> {
    private final PartialSolution _parent;
    private final ScheduledTask _scheduledTask;

    public PartialSolution(PartialSolution parent, ScheduledTask scheduledTask) {
        _parent = parent;
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
     * This returns the current end time of a partial solution
     *
     * @return Returns the end time
     */
    public int getEndTime() {
        int end = 0;
        for (ScheduledTask task : this) {
            if (task.getFinishTime() > end) {
                end = task.getFinishTime();
            }
        }
        return end;
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
// TODO: 12/08/20 come back and do equals method & hashcode !!!! how?

//    @Override
//    public boolean equals(Object other) {
//        for (ScheduledTask scheduledTask : this) {
//            if(!scheduledTask.equals(other)){
//                return false;
//            }
//        }
//
//        return true;
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(_parent, _graph, _scheduledTask);
//    }
}

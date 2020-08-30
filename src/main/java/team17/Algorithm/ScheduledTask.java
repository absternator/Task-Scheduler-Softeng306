package team17.Algorithm;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import team17.DAG.DAGNode;

import java.util.Objects;

/**
 * This Represents each task scheduled on a processor at a particular start time.
 */
public class ScheduledTask implements Comparable<ScheduledTask> {
    private final int _processorNum;
    private final DAGNode _node;
    private final int _startTime;

    public ScheduledTask(int processorNum, DAGNode node, int startTime){
        _processorNum = processorNum;
        _node = node;
        _startTime = startTime;
    }

    public int getStartTime() {
        return _startTime;
    }

    public int getProcessorNum() {
        return _processorNum;
    }

    public DAGNode getNode() {
        return _node;
    }

    /**
     * Gets the finish time for the scheduled task for this particular partial solution
     * NOTE: This is not the finish time of the partial solution, unless the partial solution is complete
     */
    public int getFinishTime(){
        return _startTime + _node.getWeight();
    }

    @Override
    public boolean equals(Object other) {
        ScheduledTask that = (ScheduledTask) other;
        return _startTime == that._startTime &&
                Objects.equals(_node, that._node);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(_node).append(_startTime).toHashCode();
    }

    @Override
    public String toString() {
        return "node=" + _node.getId() +
                ", startTime=" + _startTime +
                "procNum=" + _processorNum;
    }

    @Override
    public int compareTo(ScheduledTask other) {
        return _node.getId().compareTo(other._node.getId());
    }
}

package team17.Algorithm;

import team17.DAG.Node;

import java.util.Objects;

/**
 * This Represents each task scheduled on a processor.
 */
public class ScheduledTask implements Comparable<ScheduledTask> {
    private final int _processorNum;
    private final Node _node;
    private final int _startTime;

    public ScheduledTask(int processorNum, Node node, int startTime){
        _processorNum = processorNum;
        _node= node;
        _startTime= startTime;
    }

    public int getStartTime() {
        return _startTime;
    }

    public int getProcessorNum() {
        return _processorNum;
    }

    public Node getNode() {
        return _node;
    }

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
        return Objects.hash(_node, _startTime);
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

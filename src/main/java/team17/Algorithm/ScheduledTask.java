package team17.Algorithm;

import team17.DAG.Node;

import java.util.Objects;

public class ScheduledTask {
    private int _processorNum;
    private final Node _node;
    private final int _startTime;

    public ScheduledTask(int processorNum, Node node, int startTime){
        _processorNum = processorNum;
        _node= node;
        _startTime= startTime;
    }

    public int get_startTime() {
        return _startTime;
    }

    public int get_processorNum() {
        return _processorNum;
    }

    public Node get_node() {
        return _node;
    }

    public int getFinishTime(){
        return _startTime + _node.get_weight();
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;
        ScheduledTask that = (ScheduledTask) other;
        return _processorNum == that._processorNum &&
                _startTime == that._startTime &&
                Objects.equals(_node, that._node);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_processorNum, _node, _startTime);
    }

    @Override
    public String toString() {
        return "{" +
                "_processorNum=" + _processorNum +
                ", _node=" + _node.get_id() +
                ", _startTime=" + _startTime +
                '}';
    }
}

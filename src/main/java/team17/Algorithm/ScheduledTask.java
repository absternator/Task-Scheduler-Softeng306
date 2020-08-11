package team17.Algorithm;

import team17.DAG.Node;

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
}

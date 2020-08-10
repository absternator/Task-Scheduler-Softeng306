package team17.Algorithm;

import team17.DAG.Node;

public class ScheduledTask {
    private int _processorNum;
    private Node _node;
    private int _startTime;

    public ScheduledTask(int processorNum, Node node, int startTime){
        _processorNum=processorNum;
        _node=node;
        _startTime=startTime;
    }

    public Node getNode() {
        return _node;
    }
}

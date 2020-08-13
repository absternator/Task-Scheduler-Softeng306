package team17.DAG;

public class DagNode {
    private String _id;
    private int _weight;
    private int _startTime;
    private int _processorNum;

    public DagNode(String task, int weight) {
        _id = task;
        _weight = weight;
    }

    @Override
    public boolean equals(Object node) {
        return this._id.equals(((DagNode)node).getTask());
    }

    @Override
    public int hashCode() {
        return _id.hashCode();
    }

    public String getTask() {
        return _id;
    }

    public int getWeight() {
        return _weight;
    }

    public int getStartTime() {
        return _startTime;
    }

    public int getProcessorNum() {
        return _processorNum;
    }

    public void setProcessorNum(int _processorNum) {
        this._processorNum = _processorNum;
    }

    public void setStartTime(int _startTime) {
        this._startTime = _startTime;
    }

    @Override
    public String toString() {
        return "{" +
                "task='" + _id + '\'' +
                ", weight=" + _weight +
                '}';
    }
}

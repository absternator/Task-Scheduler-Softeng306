package team17.DAG;

public class DagNode {
    private String _task;
    private int _weight;
    private int _startTime;
    private int _processorNum;

    public DagNode(String task, int weight) {
        _task = task;
        _weight = weight;
    }

    @Override
    public boolean equals(Object node) {
        return this._task.equals(((DagNode)node).get_task());
    }

    @Override
    public int hashCode() {
        return _task.hashCode();
    }

    public String get_task() {
        return _task;
    }

    public int get_weight() {
        return _weight;
    }

    public int get_startTime() {
        return _startTime;
    }

    public int get_processorNum() {
        return _processorNum;
    }

    public void set_processorNum(int _processorNum) {
        this._processorNum = _processorNum;
    }

    public void set_startTime(int _startTime) {
        this._startTime = _startTime;
    }

    @Override
    public String toString() {
        return "{" +
                "task='" + _task + '\'' +
                ", weight=" + _weight +
                '}';
    }
}

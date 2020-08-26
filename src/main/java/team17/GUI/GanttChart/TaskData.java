package team17.GUI.GanttChart;

public class TaskData {

    private int _length;
    private String _id;
    private String _styleClass;

    public TaskData(String id, int weight, String styleClass) {
        super();
        _id = id;
        _length = weight;
        _styleClass = styleClass;
    }


    public String getId() {
        return _id;
    }

    public int getLength() {
        return _length;
    }

    public void setLength(int length) {
        _length = length;
    }

    public String getStyleClass() {
        return _styleClass;
    }

    public void setStyleClass(String styleClass) {
        _styleClass = styleClass;
    }
}

package com.company.DAG;

import java.util.Objects;

public class DagNode {
    private String task;
    private int weight;
    private int startTime;

    public DagNode(String task, int weight) {
        this.task = task;
        this.weight = weight;
    }

    @Override
    public boolean equals(Object node) {
        return this.task.equals(((DagNode)node).getTask());
    }

    @Override
    public int hashCode() {
        return task.hashCode();
    }

    public String getTask() {
        return task;
    }

    public int getWeight() {
        return weight;
    }

    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    @Override
    public String toString() {
        return "{" +
                "task='" + task + '\'' +
                ", weight=" + weight +
                '}';
    }
}

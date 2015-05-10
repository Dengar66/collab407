package com.DPAC.collabormate;

public class Task {
    private long id;
    private String task;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    // Will be used by the ArrayAdapter in the ListView
    @Override
    public String toString() {
        return task;
    }
}
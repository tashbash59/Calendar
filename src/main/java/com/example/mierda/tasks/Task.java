package com.example.mierda.tasks;

public class Task {
    private String name;
    private String description;
    private TaskPriority priority;
    private Boolean isCompleted = false;

    Task(String name, String description, TaskPriority priority) {
        this.name = name;
        this.description = description;
        this.priority = priority;
    }
    public String getName() { return this.name; }
    public TaskPriority getPriority() { return this.priority; }
    public void setIsCompleted(boolean isCompleted) {
        this.isCompleted = isCompleted;
    }
    public boolean getIsCompleted() { return this.isCompleted; }
}

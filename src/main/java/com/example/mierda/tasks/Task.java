package com.example.mierda.tasks;

public class Task {
    String name;
    String description;
    TaskPriority priority;
    Boolean isCompleted = false;

    Task(String name, String description, TaskPriority priority) {
        this.name = name;
        this.description = description;
        this.priority = priority;
    }
}

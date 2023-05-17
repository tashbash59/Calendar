package com.example.mierda.tasks;

import java.util.ArrayList;
import javafx.scene.layout.AnchorPane;

public class TaskModel {
    ArrayList<Task> tasks;
    TaskpaneController controller;

    public TaskModel(AnchorPane taskPane) {
        this.tasks = new ArrayList<>();
        this.controller = new TaskpaneController(taskPane, this);
    }
    public ArrayList<Task> getTasks() { return this.tasks; }
    public void addTask(Task task) { this.tasks.add(task); }
    public void removeTask(Task task) { this.tasks.remove(task); }
    public TaskpaneController getController() { return this.controller; };
}

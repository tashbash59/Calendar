package com.example.mierda.tasks;

import java.util.ArrayList;
import javafx.scene.layout.AnchorPane;

public class TaskModel {
    ArrayList<Task> tasks;
    ArrayList<TaskObserver> observers;
    TaskpaneController controller;

    public TaskModel(AnchorPane taskPane) {
        this.controller = new TaskpaneController(taskPane, this);
        this.tasks = new ArrayList<>();
        this.observers = new ArrayList<>();
        this.addObserver(this.controller);
    }
    public ArrayList<Task> getTasks() { return this.tasks; }
    public void addTask(Task task) {
        this.tasks.add(task);
        this.notifyObservers();
    }
    public void removeTask(Task task) {
        this.tasks.remove(task);
        this.notifyObservers();
    }
    public TaskpaneController getController() { return this.controller; };
    private void addObserver(TaskObserver observer) {
        this.observers.add(observer);
    }
    private void notifyObservers() {
        this.observers.forEach(observer -> { observer.update(this.tasks); });
    }
}

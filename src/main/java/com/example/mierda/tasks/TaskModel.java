package com.example.mierda.tasks;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import org.json.JSONArray;
import org.json.JSONObject;

public class TaskModel {
    final String jsonLocation =
            System.getProperty("user.dir") +
                    "/src/main/resources/com/example/mierda/tasks.json";
    private final InputStream file = getClass().getResourceAsStream("/tasks.json");
    private ArrayList<Task> tasks;
    private ArrayList<TaskObserver> observers;
    private TaskpaneController controller;
    private Class parentClass;

    public TaskModel(AnchorPane taskPane, Class parentClass) {
        this.parentClass = parentClass;
        this.controller = new TaskpaneController(taskPane, this, this.parentClass);
        this.tasks = new ArrayList<>();
        this.observers = new ArrayList<>();
        this.addObserver(this.controller);
        this.parseJSON();
    }
    public ArrayList<Task> getTasks() { return this.tasks; }
    public void addTask(Task task) {
        this.tasks.add(task);
        this.saveJSON();
        this.notifyObservers();
    }
    public void removeTask(Task task) {
        this.tasks.remove(task);
        this.saveJSON();
        this.notifyObservers();
    }
    public void changeTask(Task oldTask, Task newTask) {
        int oldTaskIndex = this.tasks.indexOf(oldTask);
        this.tasks.remove(oldTask);
        this.tasks.add(oldTaskIndex, newTask);
        this.saveJSON();
        this.notifyObservers();
    }
    public int revmoveCompletedTasks() {
        int result = 0;
        for (int i = 0; i < this.tasks.size(); ++i) {
            if (tasks.get(i).getIsCompleted()) {
                this.removeTask(tasks.get(i));
                result += 1;
                i -= 1;
            }
        }
        this.notifyObservers();
        return result;
    }
    public TaskpaneController getController() { return this.controller; };
    private void addObserver(TaskObserver observer) {
        this.observers.add(observer);
    }
    private void notifyObservers() {
        this.observers.forEach(observer -> { observer.update(this.tasks); });
    }

    private void parseJSON() {
        String jsonString = "[]";
        try {
            jsonString =
                    new String(Files.readAllBytes(Paths.get(this.jsonLocation)));
        } catch (Exception e) {
            return;
        }

        JSONArray content = new JSONArray(jsonString);
        Iterator<Object> iter = content.iterator();
        while (iter.hasNext()) {
            JSONObject taskJson = (JSONObject)iter.next();
            this.addTask(new Task(taskJson));
        }
    }
    private JSONArray toJSONArray() {
        return new JSONArray(
            this.tasks.stream().map(task -> task.toJSONObject()).toArray());
    }
    private void saveJSON() {
        JSONArray object = this.toJSONArray();
        try {
            BufferedWriter writer =
                new BufferedWriter(new FileWriter(this.jsonLocation));
            writer.write(object.toString(4));
            writer.close();
        } catch (Exception e) {
            System.out.println("Could not save the file");
        }
    }
}

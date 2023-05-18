package com.example.mierda.tasks;

import org.json.JSONObject;

public class Task {
    private String name;
    private String description;
    private TaskPriority priority;
    private Boolean isCompleted = false;

    public Task(String name, String description, TaskPriority priority) {
        this.name = name;
        this.description = description;
        this.priority = priority;
    }
    public Task(String name, String description, TaskPriority priority,
                boolean isCompleted) {
        this(name, description, priority);
        this.isCompleted = isCompleted;
    }
    public Task(JSONObject jsonObject) {
        this.name = (String)jsonObject.get("name");
        this.description = (String)jsonObject.get("name");
        switch ((int)jsonObject.get("priority")) {
        case 0:
            this.priority = TaskPriority.HIGH;
            break;
        case 1:
            this.priority = TaskPriority.MEDIUM;
            break;
        case 2:
            this.priority = TaskPriority.LOW;
            break;
        default:
            this.priority = TaskPriority.LOW;
            break;
        }
        this.isCompleted = (boolean)jsonObject.get("isCompleted");
    }
    public Task(Task task) {
        this(task.name, task.description, task.priority, task.isCompleted);
    }
    public String getName() { return this.name; }
    public TaskPriority getPriority() { return this.priority; }
    public void setIsCompleted(boolean isCompleted) {
        this.isCompleted = isCompleted;
    }
    public boolean getIsCompleted() { return this.isCompleted; }
    public JSONObject toJSONObject() {
        JSONObject result = new JSONObject();
        result.put("name", this.name);
        result.put("description", this.description);
        result.put("priority", this.priority.getValue());
        result.put("isCompleted", this.isCompleted);
        return result;
    }
}

package com.example.mierda.calendar;

import java.util.Iterator;
import java.util.Vector;
import org.json.JSONArray;
import org.json.JSONObject;

public class TaskLink {
    final String day;
    final Vector<String> tasks;

    public TaskLink(String day, Vector<String> tasks) {
        this.day = day;
        this.tasks = tasks;
    }

    public static TaskLink fromJSONObject(JSONObject object) {
        if (object.keySet().size() != 1)
            return null;
        String key = object.keys().next();
        if (!key.getClass().getSimpleName().equals("String"))
            return null;
        Object value = object.get(key);
        if (!value.getClass().getSimpleName().equals("JSONArray"))
            return null;
        Vector<String> tasks = new Vector<>();
        Iterator<Object> tasksIterator = ((JSONArray)value).iterator();
        while (tasksIterator.hasNext()) {
            Object task = tasksIterator.next();
            if (!key.getClass().getSimpleName().equals("String"))
                continue;
            tasks.add((String)task);
        }
        return new TaskLink(key, tasks);
    }

    public JSONObject toJSONObject() {
        JSONObject result = new JSONObject();
        JSONArray jsonTasks = new JSONArray(this.tasks.toArray());
        result.put(this.day, jsonTasks);
        return result;
    }

    public Vector<String> getTasks() { return this.tasks; }
    public String getDay() { return this.day; }
}

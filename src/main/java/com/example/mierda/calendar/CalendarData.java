package com.example.mierda.calendar;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Vector;
import org.json.JSONArray;
import org.json.JSONObject;

public class CalendarData {
    private String filepath;
    private String name;
    private HashMap<String, Vector<TaskLink>> monthToTasks;
    private Integer money;

    private CalendarData(String filepath) { this.filepath = filepath; }

    public static CalendarData getDefaultData() {
        CalendarData data = new CalendarData(
            System.getProperty("user.dir") +
            "/src/main/resources/com/example/mierda/calendarData_default.json");
        data.money = 1000;
        data.name = "MyMierda";
        HashMap<String, Vector<TaskLink>> monthToTasks = new HashMap<>();
        TaskLink link1 = new TaskLink(
            "21",
            new Vector<String>(Arrays.asList((new String[] {"First task!"}))));
        TaskLink link2 = new TaskLink(
            "22",
            new Vector<String>(Arrays.asList(new String[] {"Second task!"})));
        TaskLink link3 =
            new TaskLink("23", new Vector<String>(Arrays.asList(new String[] {
                                   "Third task!", "HelloWorld2!"})));
        Vector<TaskLink> aprilTasks = new Vector<>(3);
        aprilTasks.add(link1);
        aprilTasks.add(link2);
        aprilTasks.add(link3);
        TaskLink link4 = new TaskLink("22", new Vector<String>(Arrays.asList(
                                                new String[] {"Hello world"})));
        Vector<TaskLink> mayTasks = new Vector<>(1);
        mayTasks.add(link4);
        monthToTasks.put("04.2023", aprilTasks);
        monthToTasks.put("05.2023", mayTasks);
        data.monthToTasks = monthToTasks;

        return data;
    }

    public JSONObject toJSONObect() {
        JSONObject object = new JSONObject();
        object.put("name", this.name);
        object.put("money", this.money);
        JSONObject tasks = new JSONObject();
        for (String key : this.monthToTasks.keySet()) {
            Vector<TaskLink> taskLinks = this.monthToTasks.get(key);
            JSONArray taskLinksJsonArray = new JSONArray();
            taskLinks.stream().forEach(taskLink -> {
                taskLinksJsonArray.put(taskLink.toJSONObect());
            });
            tasks.put(key, taskLinksJsonArray);
        }
        object.put("tasks", tasks);

        System.out.println("This is my object: " + object);

        return object;
    }

    public void save() {
        JSONObject object = this.toJSONObect();
        try {
            BufferedWriter writer =
                new BufferedWriter(new FileWriter(this.filepath));
            writer.write(object.toString(4));

            writer.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public int getMoney() { return this.money; }

    public void addMoney(int amount) { this.money += amount; }
}

class TaskLink {
    String day;
    Vector<String> tasks;

    public TaskLink(String day, Vector<String> tasks) {
        this.day = day;
        this.tasks = tasks;
    }

    public JSONObject toJSONObect() {
        JSONObject result = new JSONObject();
        JSONArray jsonTasks = new JSONArray(this.tasks.toArray());
        result.put(this.day, jsonTasks);
        return result;
    }
}

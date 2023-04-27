package com.example.mierda.calendar;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;
import org.json.JSONArray;
import org.json.JSONObject;

public class CalendarData {
    private String filepath;
    private String name;
    private HashMap<String, Vector<TaskLink>> monthToTasks;
    private Integer money;

    private CalendarData(String filepath) { this.filepath = filepath; }

    public static CalendarData fromFilepath(String filepath) {
        CalendarData data = new CalendarData(filepath);
        String content = null;
        try {
            content = new String(Files.readAllBytes(Paths.get(filepath)));
        } catch (Exception e) {
            return data;
        }
        JSONObject object = new JSONObject(content);
        Object name = object.get("name");
        if (name != null) {
            if (name.getClass().equals(new String().getClass()))
                data.name = (String)name;
        }

        Object money = object.get("money");
        if (money != null) {
            if (money.getClass().equals(Integer.valueOf(0).getClass()))
                data.money = (Integer)money;
        }

        Object tasks = object.get("tasks");
        if (!(tasks.getClass().getSimpleName().equals("JSONObject")))
            return data;
        JSONObject tasksJson = (JSONObject)tasks;
        data.monthToTasks = new HashMap<>();
        Iterator<String> taskMontsIterator = tasksJson.keys();
        while (taskMontsIterator.hasNext()) {
            String key = taskMontsIterator.next();
            Vector<TaskLink> taskLinks = new Vector<>();
            if (!tasksJson.get(key).getClass().getSimpleName().equals(
                    "JSONArray"))
                continue;
            JSONArray taskLinkArray = (JSONArray)tasksJson.get(key);
            Iterator<Object> taskLinkArrayIter = taskLinkArray.iterator();
            while (taskLinkArrayIter.hasNext()) {
                Object taskLinkArrayObj = taskLinkArrayIter.next();
                if (!taskLinkArrayObj.getClass().getSimpleName().equals(
                        "JSONObject"))
                    continue;
                TaskLink link =
                    TaskLink.fromJSONObject((JSONObject)taskLinkArrayObj);
                if (link != null)
                    taskLinks.add(link);
            }
            data.monthToTasks.put(key, taskLinks);
        }

        return data;
    };

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
        if (this.monthToTasks == null)
            return object;
        for (String key : this.monthToTasks.keySet()) {
            Vector<TaskLink> taskLinks = this.monthToTasks.get(key);
            JSONArray taskLinksJsonArray = new JSONArray();
            taskLinks.stream().forEach(taskLink -> {
                taskLinksJsonArray.put(taskLink.toJSONObect());
            });
            tasks.put(key, taskLinksJsonArray);
        }
        object.put("tasks", tasks);

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

    public static TaskLink fromJSONObject(JSONObject object) {
        if (object.keySet().size() != 1)
            return null;
        Object key = object.keys().next();
        if (!key.getClass().getSimpleName().equals("String"))
            return null;
        Object value = object.get((String)key);
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
        return new TaskLink((String)key, tasks);
    }

    public JSONObject toJSONObect() {
        JSONObject result = new JSONObject();
        JSONArray jsonTasks = new JSONArray(this.tasks.toArray());
        result.put(this.day, jsonTasks);
        return result;
    }
}

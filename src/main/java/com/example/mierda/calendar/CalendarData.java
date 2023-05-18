package com.example.mierda.calendar;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;
import org.json.JSONArray;
import org.json.JSONObject;

public class CalendarData {
    private final String filepath;
    private Integer money;
    private Integer healthM;
    private Integer happyM;
    private Integer hungryM;

    private CalendarData(String filepath) { this.filepath = filepath; }

    public static CalendarData fromFilepath(String filepath) {
        CalendarData data = new CalendarData(filepath);
        String content;
        try {
            content = new String(Files.readAllBytes(Paths.get(filepath)));
        } catch (Exception e) {
            return data;
        }
        JSONObject object = new JSONObject(content);

        Object money = object.get("money");
        if (money != null) {
            if (money.getClass().equals(Integer.valueOf(0).getClass()))
                data.money = (Integer)money;
        }

        return data;
    }

    public static CalendarData getDefaultData() {
        CalendarData data = new CalendarData(
            System.getProperty("user.dir") +
            "/src/main/resources/com/example/mierda/calendarData_default.json");
        data.money = 1000;
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

        return data;
    }

    public JSONObject toJSONObect() {
        JSONObject object = new JSONObject();
        object.put("money", this.money);
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
            System.out.println("Could not save the file due to: " + e);
        }
    }

    public Vector<TaskLink> getMonthTasks(Calendar month) {
        SimpleDateFormat formatter = new SimpleDateFormat("MMyyyy");
        String filename = "t" + formatter.format(month.getTime()) + ".json";
        String tasksFilepath = System.getProperty("user.dir") +
                               "/src/main/resources/com/example/mierda/" +
                               filename;
        String content;
        try {
            content = new String(Files.readAllBytes(Paths.get(tasksFilepath)));
        } catch (Exception e) {
            return null;
        }
        JSONArray root = new JSONArray(content);
        Iterator<Object> monthsTasksIterator = root.iterator();
        Vector<TaskLink> taskLinks = new Vector<>();
        while (monthsTasksIterator.hasNext()) {
            Object tasks = monthsTasksIterator.next();
            if (!tasks.getClass().getSimpleName().equals("JSONObject"))
                continue;
            TaskLink link = TaskLink.fromJSONObject((JSONObject)tasks);
            if (link != null)
                taskLinks.add(link);
        }
        return taskLinks;
    }

    public int getMoney() { return this.money; }

    public void addMoney(int amount) { this.money += amount; }
}

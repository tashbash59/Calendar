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
    private Integer health;
    private Integer happy;
    private Integer hungry;

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
        Object health = object.get("health");
        if (health != null) {
            if (health.getClass().equals(Integer.valueOf(0).getClass()))
                data.health = (Integer) health;
        }
        Object hungry = object.get("hungry");
        if (hungry != null) {
            if (health.getClass().equals(Integer.valueOf(0).getClass()))
                data.hungry = (Integer) hungry;
        }
        Object happy = object.get("happy");
        if (happy != null) {
            if (health.getClass().equals(Integer.valueOf(0).getClass()))
                data.happy = (Integer) happy;
        }


        return data;
    }

    public JSONObject toJSONObect() {
        JSONObject object = new JSONObject();
        object.put("money", this.money);
        object.put("health", this.health);
        object.put("happy", this.happy);
        object.put("hungry", this.hungry);
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

    public Vector<CalendarEvent> getMonthEvents(Calendar month) {
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
        Iterator<Object> monthsEventsIterator = root.iterator();
        Vector<CalendarEvent> events = new Vector<>();
        while (monthsEventsIterator.hasNext()) {
            Object currentEvent = monthsEventsIterator.next();
            if (!currentEvent.getClass().getSimpleName().equals("JSONObject"))
                continue;
            CalendarEvent event = new CalendarEvent((JSONObject)currentEvent);
            if (event != null)
                events.add(event);
        }
        return events;
    }

    public int getMoney() { return this.money; }
    public int getHealth() { return this.health; }
    public int getHappy() {return this.happy;}
    public int getHungry() {return this.hungry;}

    public void addMoney(int amount) {
        this.money += amount;
        this.save();
    }
    public void setHealth(int health) {
        this.health = health;
        this.save();
    }
    public void setHappy(int happy) {
        this.happy = happy;
        this.save();
    }

    public void setHungry(int hungry) {
        this.hungry = hungry;
        this.save();
    }
}

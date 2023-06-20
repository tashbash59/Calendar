package com.example.mierda.calendar;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Vector;
import org.json.JSONArray;
import org.json.JSONObject;

public class CalendarData {
    final static SimpleDateFormat formatter = new SimpleDateFormat("ddMMyyyy");
    final static int maxStat = 434;

    private final String filepath;
    private Integer money;
    private Integer health;
    private Integer happy;
    private Integer hungry;
    private Calendar lastLoginDate;

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
                data.health = (Integer)health;
        }
        Object hungry = object.get("hungry");
        if (hungry != null) {
            if (health.getClass().equals(Integer.valueOf(0).getClass()))
                data.hungry = (Integer)hungry;
        }
        Object happy = object.get("happy");
        if (happy != null) {
            if (health.getClass().equals(Integer.valueOf(0).getClass()))
                data.happy = (Integer)happy;
        }

        Object lastLoginDate = object.get("lastLoginDate");
        if (lastLoginDate != null) {
            if (lastLoginDate.getClass().equals(
                    String.valueOf("").getClass())) {
                Calendar calendar = Calendar.getInstance();
                try {
                    calendar.setTime(formatter.parse((String)lastLoginDate));
                    data.lastLoginDate = calendar;
                } catch (Exception e) {
                    System.out.println("Could not parse the last login date");
                }
            }
        }

        return data;
    }

    public JSONObject toJSONObect() {
        JSONObject object = new JSONObject();
        object.put("money", this.money);
        object.put("health", this.health);
        object.put("happy", this.happy);
        object.put("hungry", this.hungry);
        object.put("lastLoginDate", formatter.format(new Date()));
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
            System.out.println("Could not save the file");
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
    public int getHappy() { return this.happy; }
    public int getHungry() { return this.hungry; }
    public Calendar getLastLoginDate() { return this.lastLoginDate; }

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

    public void skipDays(int completedTasks) {
        for (int i = 0; i < this.getDaysSinceLogin(); ++i) {
            this.health -= maxStat / 3 + 1;
            if (this.hungry == 0) {
                this.health -= maxStat / 3 + 1;
            }
            if (this.happy == 0) {
                this.health -= maxStat / 3 + 1;
            }
            this.happy -= maxStat / 4 + 1;
            this.hungry -= maxStat / 4 + 1;
        }
        this.money += completedTasks * 3;
        if (this.health < 0) {
            this.health = 0;
            this.hungry = 0;
            this.happy = 0;
        }
        if (this.happy < 0) {
            this.happy = 0;
        }
        if (this.hungry < 0) {
            this.hungry = 0;
        }
        this.save();
    }

    public boolean isLoginToday() {
        Calendar calendar = Calendar.getInstance();
        return this.lastLoginDate.get(Calendar.DAY_OF_YEAR) ==
            calendar.get(Calendar.DAY_OF_YEAR) &&
            this.lastLoginDate.get(Calendar.YEAR) ==
                calendar.get(Calendar.YEAR);
    }

    public int getDaysSinceLogin() {
        Calendar calendar = Calendar.getInstance();
        long daysSinceLogin = ChronoUnit.DAYS.between(
            calendar.toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
            this.lastLoginDate.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate());
        return (int)Math.abs(daysSinceLogin);
    }
}

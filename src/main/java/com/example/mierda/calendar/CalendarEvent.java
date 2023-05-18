package com.example.mierda.calendar;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import org.json.JSONObject;

public class CalendarEvent {
    Calendar date;
    String name;
    String description;
    public CalendarEvent(Calendar date, String name, String description) {
        this.date = date;
        this.name = name;
        this.description = description;
    }
    public CalendarEvent(JSONObject jsonObject) {
        String dateString = (String)jsonObject.get("date");
        SimpleDateFormat formatter = new SimpleDateFormat("MMyyyydd");
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(formatter.parse(dateString));
        } catch (Exception e) {
        }
        this.description = (String)jsonObject.get("description");
        this.date = calendar;
        this.name = (String)jsonObject.get("name");
        this.description = (String)jsonObject.get("description");
    }
    public JSONObject toJSONObject() {
        JSONObject result = new JSONObject();
        result.put("name", this.name);
        result.put("description", this.description);
        SimpleDateFormat formatter = new SimpleDateFormat("MMyyyydd");
        result.put("date", formatter.format(this.date.getTime()));
        return result;
    }
    public Calendar getDate() { return this.date; }
    public String getName() { return this.name; }
    public String getDescription() { return this.description; }
}

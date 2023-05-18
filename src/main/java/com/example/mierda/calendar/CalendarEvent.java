package com.example.mierda.calendar;

import java.util.Calendar;

public class CalendarEvent {
    Calendar date;
    String name;
    String description;
    public CalendarEvent(Calendar date, String name, String description) {
        this.date = date;
        this.name = name;
        this.description = description;
    }
}

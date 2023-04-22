package com.example.mierda.calendar;

import java.util.Calendar;

public class CalendarEntry {
    private Calendar date;
    private String task;

    CalendarEntry(Calendar date, String task) {
        this.date = date;
        this.task = task;
    }

    public String toString() {
        return Integer.toString(this.date.get(Calendar.DAY_OF_MONTH));
    }
    public String getTask() { return task; }

    public void setTask(String task) { this.task = task; }
}

package com.example.mierda.calendar;

import java.util.Calendar;
import java.util.Vector;

public class CalendarEntry {
    private final Calendar date;
    private Vector<String> tasks;

    CalendarEntry(Calendar date, Vector<String> tasks) {
        this.date = date;
        this.tasks = tasks;
    }

    public String toString() {
        return Integer.toString(this.date.get(Calendar.DAY_OF_MONTH));
    }

    public Vector<String> getTask() { return this.tasks; }

    public Calendar getDate() { return date; }

    public void setTask(Vector<String> tasks) { this.tasks = tasks; }
    public void addTask(String task) { this.tasks.add(task); }
}

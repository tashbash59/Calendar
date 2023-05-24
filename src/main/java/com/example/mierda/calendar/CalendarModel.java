package com.example.mierda.calendar;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Vector;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import org.json.JSONArray;
import org.json.JSONObject;

public class CalendarModel {
    private static final int CALENDAR_COLUMNS = 7;
    private static final int CALENDAR_ROWS = 6;

    private final Calendar startOfDisplayedMonth;
    private Vector<CalendarEvent> events;
    private CalendarEntry[][] entriesOfDisplayedMonth;
    private CalendarData data;
    private CalendarController controller;
    private AnchorPane calendarPane;
    private Label monthLabel;

    public CalendarModel(CalendarData data, AnchorPane calendarPane,
                         Label monthLabel) {
        this.calendarPane = calendarPane;
        this.monthLabel = monthLabel;
        this.controller =
            new CalendarController(this.calendarPane, this, this.monthLabel);
        this.events = new Vector<>();
        this.data = data;
        Calendar startOfDisplayedMonth = Calendar.getInstance();
        startOfDisplayedMonth.set(Calendar.DAY_OF_MONTH, 1);
        startOfDisplayedMonth.set(Calendar.HOUR, 0);
        startOfDisplayedMonth.set(Calendar.MINUTE, 0);
        startOfDisplayedMonth.set(Calendar.SECOND, 0);
        startOfDisplayedMonth.set(Calendar.MILLISECOND, 0);
        this.startOfDisplayedMonth = startOfDisplayedMonth;
        this.entriesOfDisplayedMonth = this.calculateEntries();
        this.parseJSON();
    }

    public ArrayList<CalendarEvent> getEvents(Calendar calendar) {
        ArrayList<CalendarEvent> result = new ArrayList<>();
        for (int i = 0; i < this.events.size(); ++i) {
            CalendarEvent event = this.events.get(i);
            if (event.getDate().get(Calendar.DAY_OF_MONTH) ==
                calendar.get(Calendar.DAY_OF_MONTH)) {
                result.add(event);
            }
        }
        return result;
    }

    public void addEvent(CalendarEvent event) {
        this.events.add(event);
        this.saveJSON();
    }

    public void removeEvent(CalendarEvent event) {
        this.events.remove(event);
        this.saveJSON();
    }

    public void replaceEvent(CalendarEvent prevEvent, CalendarEvent currEvent) {
        this.events.remove(prevEvent);
        this.events.add(currEvent);
        this.saveJSON();
    }

    public void print() {
        Arrays.stream(this.entriesOfDisplayedMonth)
            .forEach(row -> System.out.println(Arrays.toString(row)));
    }

    public CalendarEntry getEntry(int row, int col) {
        return this.entriesOfDisplayedMonth[row][col];
    }

    public void addTask(int dateIndex, String task) {
        Arrays.stream(this.entriesOfDisplayedMonth)
            .forEach(row -> Arrays.stream(row).forEach(element -> {
                if (element == null)
                    return;
                if (element.getDate().get(Calendar.DAY_OF_MONTH) != dateIndex)
                    return;
                element.addTask(task);
            }));
    }

    public int getNumberOfRows() { return CALENDAR_ROWS; }

    public int getNumberOfColumns() { return CALENDAR_COLUMNS; }

    public CalendarController getController() { return this.controller; }

    public Calendar getStartOfDisplayedMonth() {
        return this.startOfDisplayedMonth;
    }

    public void selectNextMonth() {
        this.startOfDisplayedMonth.add(Calendar.MONTH, 1);
        this.entriesOfDisplayedMonth = this.calculateEntries();
        this.events.clear();
        this.parseJSON();
        this.getController().update();
    }

    public void selectPreviousMonth() {
        this.startOfDisplayedMonth.add(Calendar.MONTH, -1);
        this.entriesOfDisplayedMonth = this.calculateEntries();
        this.events.clear();
        this.parseJSON();
        this.getController().update();
    }

    public String getMonthYearString() {
        SimpleDateFormat formatter = new SimpleDateFormat("MM.yyyy");
        return formatter.format(this.startOfDisplayedMonth.getTime());
    }

    public CalendarData getData() { return this.data; }

    private CalendarEntry[][] calculateEntries() {
        Calendar currentDay = (Calendar)this.startOfDisplayedMonth.clone();
        int currentMonthIndex = currentDay.get(Calendar.MONTH);
        int dayOfWeekOffset = currentDay.get(Calendar.DAY_OF_WEEK) - 1;
        final int sundayIndex = 0;
        final int ruSundayIndex = 6;
        dayOfWeekOffset = dayOfWeekOffset == sundayIndex ? ruSundayIndex
                                                         : dayOfWeekOffset - 1;
        CalendarEntry[][] calendarEntries =
            new CalendarEntry[CALENDAR_ROWS][CALENDAR_COLUMNS];
    Outer:
        for (int row = 0; row < calendarEntries.length; ++row) {
            for (int column = row == 0 ? dayOfWeekOffset : 0;
                 column < calendarEntries[row].length; ++column) {
                CalendarEntry entry = new CalendarEntry(
                    (Calendar)currentDay.clone(), new Vector<String>());
                calendarEntries[row][column] = entry;
                currentDay.add(Calendar.DAY_OF_WEEK, 1);
                if (currentDay.get(Calendar.MONTH) != currentMonthIndex)
                    break Outer;
            }
        }
        return calendarEntries;
    }

    private String getEventsFilepath() {
        SimpleDateFormat formatter = new SimpleDateFormat("MMyyyy");
        String filename = null;
        try {
            filename = "e" +
                       formatter.format(this.startOfDisplayedMonth.getTime()) +
                       ".json";
        } catch (Exception e) {
            e.printStackTrace();
        }
        String eventsFilepath = System.getProperty("user.dir") +
                                "/src/main/resources/com/example/mierda/" +
                                filename;
        return eventsFilepath;
    }

    private void parseJSON() {
        String content;
        try {
            content = new String(
                Files.readAllBytes(Paths.get(this.getEventsFilepath())));
        } catch (Exception e) {
            return;
        }
        JSONArray root = new JSONArray(content);
        Iterator<Object> iter = root.iterator();
        while (iter.hasNext()) {
            JSONObject taskJson = (JSONObject)iter.next();
            this.addEvent(new CalendarEvent(taskJson));
        }
    }

    private JSONArray toJSONArray() {
        return new JSONArray(
            this.events.stream().map(event -> event.toJSONObject()).toArray());
    }
    private void saveJSON() {
        JSONArray object = this.toJSONArray();
        try {
            BufferedWriter writer =
                new BufferedWriter(new FileWriter(this.getEventsFilepath()));
            writer.write(object.toString(4));
            writer.close();
        } catch (Exception e) {
            System.out.println("Could not save the file due to: " + e);
        }
    }
}

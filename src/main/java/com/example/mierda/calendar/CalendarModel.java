package com.example.mierda.calendar;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Vector;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

public class CalendarModel {
    private static final int CALENDAR_COLUMNS = 7;
    private static final int CALENDAR_ROWS = 6;

    private final Calendar startOfDisplayedMonth;
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
            new CalendarController(calendarPane, this, monthLabel);
        this.data = data;
        Calendar startOfDisplayedMonth = Calendar.getInstance();
        startOfDisplayedMonth.set(Calendar.DAY_OF_MONTH, 1);
        startOfDisplayedMonth.set(Calendar.HOUR, 0);
        startOfDisplayedMonth.set(Calendar.MINUTE, 0);
        startOfDisplayedMonth.set(Calendar.SECOND, 0);
        startOfDisplayedMonth.set(Calendar.MILLISECOND, 0);
        this.startOfDisplayedMonth = startOfDisplayedMonth;
        this.entriesOfDisplayedMonth = this.calculateEntries();
    }

    public void print() {
        Arrays.stream(this.entriesOfDisplayedMonth)
            .forEach(row -> System.out.println(Arrays.toString(row)));
    }

    public CalendarEntry getEntry(int row, int col) {
        return this.entriesOfDisplayedMonth[row][col];
    }

    public void generateRandomTasks() {
        String[] tasks = {"Wash the dishes", "Mop the floor",
                          "Finish calendar"};
        Arrays.stream(this.entriesOfDisplayedMonth)
            .forEach(row -> Arrays.stream(row).forEach(element -> {
                if (element == null)
                    return;
                String task =
                    tasks[(int)Math.floor(Math.random() * tasks.length)];
                element.addTask(task);
            }));
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
        this.getController().update();
    }

    public void selectPreviousMonth() {
        this.startOfDisplayedMonth.add(Calendar.MONTH, -1);
        this.entriesOfDisplayedMonth = this.calculateEntries();
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
}

package com.example.mierda.calendar;

import java.util.Arrays;
import java.util.Calendar;

public class CalendarModel {
    private static final int CALENDAR_COLUMNS = 7;
    private static final int CALENDAR_ROWS = 6;

    private final Calendar startOfDisplayedMonth;
    private CalendarEntry[][] entriesOfDisplayedMonth;

    public CalendarModel() {
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
                element.setTask(task);
            }));
    }

    public int getNumberOfRows() { return CALENDAR_ROWS; }

    public int getNumberOfColumns() { return CALENDAR_COLUMNS; }

    public void selectNextMonth() {
        this.startOfDisplayedMonth.add(Calendar.MONTH, 1);
        this.entriesOfDisplayedMonth = this.calculateEntries();
    };

    public void selectPreviousMonth() {
        this.startOfDisplayedMonth.add(Calendar.MONTH, -1);
        this.entriesOfDisplayedMonth = this.calculateEntries();
    }

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
                CalendarEntry entry =
                    new CalendarEntry((Calendar)currentDay.clone(), "");
                calendarEntries[row][column] = entry;
                currentDay.add(Calendar.DAY_OF_WEEK, 1);
                if (currentDay.get(Calendar.MONTH) != currentMonthIndex)
                    break Outer;
            }
        }
        return calendarEntries;
    }
}

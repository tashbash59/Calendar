package com.example.mierda.calendar;

import java.util.Arrays;
import java.util.Calendar;

public class CalendarModel {
    private final int CALENDAR_COLUMNS = 7;
    private final int CALENDAR_ROWS = 6;

    private Calendar startOfDisplayedMonth;
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

    private CalendarEntry[][] calculateEntries() {
        Calendar currentDay = (Calendar)this.startOfDisplayedMonth.clone();
        int currentMonthIndex = currentDay.get(Calendar.MONTH);
        int dayOfWeekOffset = currentDay.get(Calendar.DAY_OF_WEEK);
        final int sundayIndex = 1;
        dayOfWeekOffset =
            dayOfWeekOffset == sundayIndex ? 6 : dayOfWeekOffset - 1;
        CalendarEntry[][] calendarEntries =
            new CalendarEntry[this.CALENDAR_ROWS][this.CALENDAR_COLUMNS];
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

package com.example.mierda.calendar;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.Scanner;
import org.json.JSONObject;

public class CalendarData {
    String filepath;
    File openedFile;
    int money;

    private CalendarData(String filepath) { this.filepath = filepath; }

    public static CalendarData fromPath(String filepath) {
        Scanner scanner = null;
        CalendarData calendarData = new CalendarData(filepath);
        try {
            scanner =
                new Scanner(Paths.get(filepath), StandardCharsets.UTF_8.name());
        } catch (IOException e) {
            return null;
        }
        String content = scanner.useDelimiter("\\A").next();
        if (scanner != null)
            scanner.close();

        JSONObject tomJsonObject = new JSONObject(content);

        if (tomJsonObject.get("money") != null) {
            calendarData.money = 0;
            try {
                calendarData.money = (Integer)tomJsonObject.get("money");
            } catch (Exception e) {
                System.out.println(
                    "Could not load money from configuration file");
            }
        }
        return calendarData;
    }

    public int getMoney() { return this.money; }

    public void addMoney(int amount) { this.money += amount; }
}

package com.example.mierda.calendar;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.Scanner;
import org.json.JSONObject;

public class CalendarDataLoader {
    String fileLink;
    File openedFile;
    int money;

    public CalendarDataLoader(String fileLink) {
        Scanner scanner = null;
        try {
            scanner =
                new Scanner(Paths.get(fileLink), StandardCharsets.UTF_8.name());
        } catch (IOException e) {
            return;
        }
        String content = scanner.useDelimiter("\\A").next();
        if (scanner != null)
            scanner.close();

        JSONObject tomJsonObject = new JSONObject(content);

        if (tomJsonObject.get("money") != null) {
            this.money = 0;
            try {
                this.money = (Integer)tomJsonObject.get("money");
            } catch (Exception e) {
                System.out.println(
                    "Could not load money from configuration file");
            }
        }

        System.out.println(this.money);

        // System.out.println(tomJsonObject.toString(4));
    }

    public int getMoney() { return this.money; }

    public void addMoney(int amount) { this.money += amount; }
}

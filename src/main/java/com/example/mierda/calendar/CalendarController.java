package com.example.mierda.calendar;

import java.util.ArrayList;
import java.util.Calendar;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

public class CalendarController {
    private AnchorPane calendarPane;
    private CalendarModel calendarModel;
    private Label monthLabel;
    private CalendarEntry today;

    public CalendarController(AnchorPane calendarPane,
                              CalendarModel calendarModel, Label monthLabel) {
        this.calendarPane = calendarPane;
        this.calendarModel = calendarModel;
        this.monthLabel = monthLabel;
    }

    public void invokeTodayEventWindow() {
        new EventWindow(calendarPane.getScene().getWindow(), calendarModel,
                        this.today.getDate(),
                        calendarModel.getEvents(this.today.getDate()));
    }

    public void clearCalendar() {
        var children = new ArrayList<>(this.calendarPane.getChildren());
        for (javafx.scene.Node child : children) {
            String id = child.getId();
            if (id == null)
                continue;
            if (!id.equals("CalendarEntry"))
                continue;
            this.calendarPane.getChildren().remove(child);
        }
    }
    public void update() {
        this.clearCalendar();
        this.monthLabel.setText(this.calendarModel.getMonthYearString());

        final double[] xOffsets = {22.0,  60.0,  104.0, 146.0,
                                   191.0, 235.0, 279.0};
        final double yCellWidth = 14.0;
        final double yBaseOffset = 10.0;

        for (int row = 0; row < this.calendarModel.getNumberOfRows(); ++row) {
            for (int column = 0;
                 column < this.calendarModel.getNumberOfColumns(); ++column) {
                CalendarEntry entry = this.calendarModel.getEntry(row, column);
                Label label = new Label(entry == null ? "" : entry.toString());
                label.setMinWidth(yCellWidth);
                boolean isToday =
                    (entry != null &&
                     entry.getDate().get(Calendar.DAY_OF_MONTH) ==
                         (Calendar.getInstance().get(Calendar.DAY_OF_MONTH)) &&
                     entry.getDate().get(Calendar.MONTH) ==
                         Calendar.getInstance().get(Calendar.MONTH) &&
                     entry.getDate().get(Calendar.YEAR) ==
                         Calendar.getInstance().get(Calendar.YEAR));
                String selectedStyle =
                    isToday
                        ? "-fx-background-color: #caef6d; -fx-background-radius: 30px 15px;"
                        : "";
                if (isToday) {
                    label.setMinWidth(yCellWidth * 1.75);
                    this.today = entry;
                }
                label.setLayoutX(
                    (isToday) ? (xOffsets[column] - yCellWidth / 1.75 / 2)
                              : xOffsets[column]);
                label.setLayoutY((yCellWidth + yBaseOffset) * (row + 2));
                label.setAlignment(Pos.CENTER);
                label.setId("CalendarEntry");
                label.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent e) {
                        new EventWindow(
                            calendarPane.getScene().getWindow(), calendarModel,
                            entry.getDate(),
                            calendarModel.getEvents(entry.getDate()));
                    }
                });
                label.setStyle("-fx-font-size: 14px; -fx-font-weight: 400; " +
                               selectedStyle);
                this.calendarPane.getChildren().add(label);
            }
        }
    }
}

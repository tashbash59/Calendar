package com.example.mierda;

import com.example.mierda.calendar.CalendarEntry;
import com.example.mierda.calendar.CalendarModel;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

public class HelloController implements Initializable {
    @FXML public AnchorPane calendarComponent;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initCalendarComponent();
    }

    private void initCalendarComponent() {
        CalendarModel calendarModel = new CalendarModel();
        calendarModel.generateRandomTasks();
        final double[] xOffsets = {22.0,  60.0,  104.0, 146.0,
                                   191.0, 235.0, 279.0};
        final double yCellWidth = 14.0;
        final double yBaseOffset = 10.0;
        for (int row = 0; row < calendarModel.getNumberOfRows(); ++row) {
            for (int column = 0; column < calendarModel.getNumberOfColumns();
                 ++column) {
                CalendarEntry entry = calendarModel.getEntry(row, column);
                Label label = new Label(entry == null ? "" : entry.toString());
                label.setLayoutX(xOffsets[column]);
                label.setLayoutY((yCellWidth + yBaseOffset) * (row + 2));
                label.setMinWidth(yCellWidth);
                label.setAlignment(Pos.CENTER);
                label.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        System.out.println(entry.getTask());
                    }
                });
                label.setStyle("-fx-font-size: 14px; -fx-font-weight: 400;");
                this.calendarComponent.getChildren().add(label);
            }
        }
    }
}

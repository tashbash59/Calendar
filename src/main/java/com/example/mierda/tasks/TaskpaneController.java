package com.example.mierda.tasks;

import com.example.mierda.calendar.CalendarEntry;
import java.util.ArrayList;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

public class TaskpaneController {
    int index;
    private AnchorPane taskPane;
    private VBox container;
    private TaskModel taskModel;

    public TaskpaneController(AnchorPane taskPane, TaskModel taskModel) {
        this.taskPane = taskPane;
        this.container = new VBox();
        this.taskModel = taskModel;
        this.taskPane.getChildren().add(this.container);
    }

    private void clearTaskPane() {
        var children = new ArrayList<>(this.container.getChildren());
        for (javafx.scene.Node child : children) {
            this.taskPane.getChildren().remove(child);
        }
    }

    public void update(CalendarEntry entry) {
        clearTaskPane();
        System.out.println(taskModel.getTasks());
    }
}

package com.example.mierda.tasks;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.VBox;

public class TaskComponent extends VBox {
    TaskComponent(Task task) {
        RadioButton taskName = new RadioButton(task.getName());
        taskName.getStyleClass().add("task-component");
        String style =
            taskName.getStyle() +

            "-fx-background-color: " + task.getPriority().getRespectiveColor() +
            "; ";
        taskName.setStyle(style);
        this.getChildren().add(taskName);
        taskName.setSelected(task.getIsCompleted());
        if (taskName.isSelected())
            taskName.getStyleClass().add("active");

        taskName.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                task.setIsCompleted(taskName.isSelected());
                if (taskName.isSelected())
                    taskName.getStyleClass().add("active");
                else {
                    taskName.getStyleClass().remove("active");
                }
            }
        });
    }
}

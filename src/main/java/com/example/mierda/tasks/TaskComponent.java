package com.example.mierda.tasks;

import com.example.mierda.calendar.CalendarData;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.VBox;

public class TaskComponent extends VBox {
    @FXML
    public Label moneyLabel;
    private final CalendarData calendarData = CalendarData.fromFilepath(
            System.getProperty("user.dir") +
                    "/src/main/resources/com/example/mierda/calendarData.json");
    TaskComponent(Task task, TaskModel taskModel) {
        RadioButton taskName = new RadioButton(task.getName());
        taskName.getStyleClass().add("task-component");
        String style = taskName.getStyle() + "-fx-background-color: " +
                       task.getPriority().getRespectiveColor() + "; ";
        taskName.setStyle(style);
        this.getChildren().add(taskName);
        taskName.setSelected(task.getIsCompleted());
        if (taskName.isSelected())
            taskName.getStyleClass().add("active");

        taskName.setOnMouseClicked(event -> {
            new TaskCreationWindow(getScene().getWindow(), taskModel, task);
        });

        taskName.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Task newTask = new Task(task);
                // я пытался сделать добавление монеток за сделанное задание, пока ничего не получилось
                /*if (taskName.isSelected()) {
                    System.out.println("1");
                    calendarData.addMoney(10);
                    calendarData.save();
                    moneyLabel.setText(
                            Integer.toString(calendarData.getMoney()));
                } else {
                    calendarData.addMoney(-10);
                    calendarData.save();
                    moneyLabel.setText(
                            Integer.toString(calendarData.getMoney()));
                }*/
                newTask.setIsCompleted(taskName.isSelected());
                taskModel.changeTask(task, newTask);
            }
        });
    }
}

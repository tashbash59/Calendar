package com.example.mierda.tasks;

import java.util.Arrays;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

public class TaskCreationWindow {
    private VBox container;
    private TextField taskNameTextField;
    private TextField taskDescriptionTextField;
    private RadioButton[] priorityButtons;
    private Button saveButton;
    private TaskModel taskModel;

    public TaskCreationWindow(Window window, TaskModel taskModel) {
        this(window, taskModel, "");
    }
    public TaskCreationWindow(Window window, TaskModel taskModel,
                              String initialText) {
        final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(window);
        this.container = new VBox(20);
        this.taskModel = taskModel;
        Scene dialogScene = new Scene(this.container, 500, 500);
        this.taskNameTextField =
            this.addLabeledTextField("Задача:", initialText);
        this.taskDescriptionTextField =
            this.addLabeledTextField("Описание:", "");
        this.container.getChildren().add(new Label("Приоритетность: "));
        this.priorityButtons = addPriorityButtons();
        this.saveButton = addSaveButton();
        dialog.setResizable(false);
        dialog.setScene(dialogScene);
        dialog.show();
    }

    private TextField addLabeledTextField(String labelText,
                                          String textFieldText) {
        this.container.getChildren().add(new Label(labelText));
        TextField field = new TextField(textFieldText);
        this.container.getChildren().add(field);
        return field;
    }

    private RadioButton[] addPriorityButtons() {
        RadioButton high = new RadioButton("Высокая");
        high.setStyle("-fx-color: " + TaskPriority.HIGH.getRespectiveColor());
        RadioButton medium = new RadioButton("Средняя");
        medium.setStyle("-fx-color: " +
                        TaskPriority.MEDIUM.getRespectiveColor());
        RadioButton low = new RadioButton("Низкая");
        low.setStyle("-fx-color: " + TaskPriority.LOW.getRespectiveColor());
        RadioButton[] radioButtons = new RadioButton[] {high, medium, low};
        Arrays.stream(radioButtons).forEach(button -> {
            button.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent arg0) {
                    Arrays.stream(radioButtons).forEach(buttonB -> {
                        buttonB.setSelected(buttonB == button);
                    });
                }
            });
            this.container.getChildren().add(button);
        });

        return radioButtons;
    }

    private Button addSaveButton() {
        Button saveButton = new Button("Сохранить");
        this.container.getChildren().add(saveButton);
        saveButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {
                saveTask();
            }
        });
        return saveButton;
    }

    private void saveTask() {
        String name = taskNameTextField.getText();
        String description = taskDescriptionTextField.getText();
        TaskPriority priority = TaskPriority.LOW;
        for (int i = 0; i < priorityButtons.length; ++i) {
            if (!priorityButtons[i].isSelected())
                continue;
            switch (i) {
            case 0:
                priority = TaskPriority.HIGH;
                break;
            case 1:
                priority = TaskPriority.MEDIUM;
                break;
            case 2:
                priority = TaskPriority.LOW;
                break;
            }
            break;
        }
        Task task = new Task(name, description, priority);
        this.taskModel.addTask(task);
    }
}

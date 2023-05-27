package com.example.mierda.tasks;

import com.example.mierda.HelloApplication;
import java.io.File;
import java.util.Arrays;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

public class TaskCreationWindow {
    final static String BUTTON_STYLE =
        "-fx-background-color: #f2f2f2; -fx-background-radius: 20; -fx-font-weight: 700";

    private VBox containerLeft;
    private AnchorPane containerRight;
    private HBox mainConatiner;
    private TextField taskNameTextField;
    private TextField taskDescriptionTextField;
    private RadioButton[] priorityButtons;
    private Button saveButton;
    private TaskModel taskModel;
    private Class parentClass;

    public TaskCreationWindow(Window window, TaskModel taskModel, Class parentClass) {
        this(window, taskModel, "", parentClass);
    }
    public TaskCreationWindow(Window window, TaskModel taskModel,
                              Task attachedTask, Class parentClass) {
        this(window, taskModel, attachedTask.getName(), parentClass);
        this.containerRight.getChildren().remove(saveButton);
        this.taskDescriptionTextField.setText(attachedTask.getDescritpion());
        this.priorityButtons[attachedTask.getPriority().getValue()].setSelected(
            true);
        HBox controlsContainer = new HBox(10);
        Button editButton = new Button("Изменить");
        editButton.setStyle(BUTTON_STYLE);
        editButton.setOnMouseClicked(event -> {
            Task newTask = this.createTask();
            newTask.setIsCompleted(attachedTask.getIsCompleted());
            this.taskModel.changeTask(attachedTask, newTask);
            ((Stage)controlsContainer.getScene().getWindow()).close();
        });
        controlsContainer.getChildren().add(editButton);
        Button deleteButton = new Button("Удалить");
        deleteButton.setStyle(BUTTON_STYLE);
        deleteButton.setOnMouseClicked(event -> {
            taskModel.removeTask(attachedTask);
            ((Stage)controlsContainer.getScene().getWindow()).close();
        });
        controlsContainer.getChildren().add(deleteButton);
        controlsContainer.setLayoutX(110);
        controlsContainer.setLayoutY(400);
        this.containerRight.getChildren().add(controlsContainer);
    }
    public TaskCreationWindow(Window window, TaskModel taskModel,
                              String initialText, Class parentClass) {
        final Stage dialog = new Stage();
        this.mainConatiner = new HBox();
        this.parentClass = parentClass;
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(window);
        this.containerLeft = new VBox(20);
        Label label = new Label("Задача");
        label.setStyle("-fx-font-size: 24; -fx-font-weight: 700");
        this.containerLeft.setPadding(new Insets(30, 60, 60, 60));
        this.containerLeft.getChildren().add(label);
        this.containerRight = new AnchorPane();
        this.containerRight.setPadding(new Insets(30));
        ImageView imgView = this.getImage();
        imgView.setLayoutX(50);
        imgView.setLayoutY(120);
        this.containerRight.getChildren().add(imgView);
        this.taskModel = taskModel;
        this.mainConatiner = new HBox(0);
        this.mainConatiner.getChildren().add(this.containerLeft);
        this.mainConatiner.getChildren().add(this.containerRight);
        this.containerRight.setStyle("-fx-background-color: white");
        this.containerLeft.setStyle("-fx-background-color: white");
        this.mainConatiner.setStyle("-fx-background-color: white");
        Scene dialogScene = new Scene(this.mainConatiner, 700, 500);
        dialogScene.getStylesheets().add(
            HelloApplication.class.getResource("style.css").toExternalForm());
        this.taskNameTextField =
            this.addLabeledTextField("Задача:", initialText);
        this.taskDescriptionTextField =
            this.addLabeledTextField("Описание:", "");
        this.containerLeft.getChildren().add(new Label("Приоритетность: "));
        this.priorityButtons = addPriorityButtons();
        this.saveButton = addSaveButton();
        dialog.setResizable(false);
        dialog.setScene(dialogScene);
        dialog.show();
    }

    private ImageView getImage() {
        Image image = new Image(parentClass.getResourceAsStream("/images/taskCreation.png"));
        ImageView imageView = new ImageView(image);
        return imageView;
    }

    private TextField addLabeledTextField(String labelText,
                                          String textFieldText) {
        Label label = new Label(labelText);
        label.setStyle("-fx-font-weight: 700");
        this.containerLeft.getChildren().add(label);
        TextField field = new TextField(textFieldText);
        this.containerLeft.getChildren().add(field);
        return field;
    }

    private RadioButton[] addPriorityButtons() {
        RadioButton high = new RadioButton("Высокая");
        high.setStyle(high.getStyle() +
                      "-fx-color: " + TaskPriority.HIGH.getRespectiveColor());
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
            this.containerLeft.getChildren().add(button);
        });

        return radioButtons;
    }

    private Button addSaveButton() {
        Button saveButton = new Button("Сохранить");
        saveButton.setStyle(BUTTON_STYLE);
        this.containerRight.getChildren().add(saveButton);
        saveButton.setLayoutX(170);
        saveButton.setLayoutY(400);
        saveButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {
                saveTask();
            }
        });
        return saveButton;
    }

    private Task createTask() {
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
        return new Task(name, description, priority);
    }

    private void saveTask() { this.taskModel.addTask(this.createTask()); }
}

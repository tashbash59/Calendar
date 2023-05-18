package com.example.mierda.tasks;

import java.util.ArrayList;
import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

public class TaskpaneController implements TaskObserver {
    private final int PADDING_INSET_SIZE = 10;
    private final int SPACING_SIZE = 5;

    private AnchorPane taskPane;
    private VBox taskContainer;
    private ScrollPane scrollContainer;
    private TaskModel taskModel;

    public TaskpaneController(AnchorPane taskPane, TaskModel taskModel) {
        while (taskPane.getChildren().size() != 0) {
            taskPane.getChildren().remove(taskPane.getChildren().get(0));
        }
        this.taskPane = taskPane;
        this.taskContainer = new VBox(SPACING_SIZE);
        this.scrollContainer = new ScrollPane(this.taskContainer);
        this.scrollContainer.setCache(false);
        this.scrollContainer.setPrefSize(325, 180);
        this.scrollContainer.setBackground(null);
        this.scrollContainer.setStyle(
            "-fx-background-color: transparent; -fx-background: transparent; -fx-text-fill: black");
        this.taskContainer.setPadding(new Insets(this.PADDING_INSET_SIZE));
        this.taskModel = taskModel;
        this.taskPane.getChildren().add(this.scrollContainer);
    }

    private void clearTaskPane() {
        var children = new ArrayList<>(this.taskContainer.getChildren());
        for (javafx.scene.Node child : children) {
            this.taskContainer.getChildren().remove(child);
        }
    }

    public void update(ArrayList<Task> tasks) {
        clearTaskPane();
        tasks.forEach(task -> {
            this.taskContainer.getChildren().add(new TaskComponent(task));
        });
    }
}

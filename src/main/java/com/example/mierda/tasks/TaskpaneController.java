package com.example.mierda.tasks;

import java.util.ArrayList;
import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class TaskpaneController implements TaskObserver {
    private final int PADDING_INSET_SIZE = 10;
    private final int SPACING_SIZE = 5;

    private AnchorPane taskPane;
    private VBox taskContainer;
    private ScrollPane scrollContainer;
    private TaskModel taskModel;

    public TaskpaneController(AnchorPane taskPane, TaskModel taskModel) {
        taskPane.getChildren().clear();
        this.taskModel = taskModel;
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
            Region region = new Region();
            HBox.setHgrow(region, Priority.ALWAYS);
            TaskComponent taskComponent =
                new TaskComponent(task, this.taskModel);
            HBox hbox = new HBox(region, taskComponent);
            this.taskContainer.getChildren().add(hbox);
        });
    }
}

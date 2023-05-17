package com.example.mierda;

import com.example.mierda.calendar.CalendarEntry;
import java.util.ArrayList;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.AnchorPane;

public class TaskpaneController {
    int index;
    private final double INITIAL_X = 33.0;
    private final double INITIAL_Y = 32.0;
    private AnchorPane taskPane;

    private void initTaskPane() {
        var children = new ArrayList<>(this.taskPane.getChildren());
        for (javafx.scene.Node child : children) {
            this.taskPane.getChildren().remove(child);
        }
    }

    public void update(CalendarEntry entry) {
        initTaskPane();
        if (entry == null)
            return;
        var tasks = entry.getTask();
        this.index = 0;
        tasks.forEach(task -> {
            if (this.index > 4)
                return;
            RadioButton taskRadioButton = new RadioButton();
            taskRadioButton.setText(task);
            taskRadioButton.setMnemonicParsing(false);
            taskRadioButton.setLayoutX(INITIAL_X);
            taskRadioButton.setLayoutY(INITIAL_Y * (this.index + 1));
            taskPane.getChildren().add(taskRadioButton);
            ++this.index;
        });
    }
    public TaskpaneController(AnchorPane taskPane) { this.taskPane = taskPane; }
}

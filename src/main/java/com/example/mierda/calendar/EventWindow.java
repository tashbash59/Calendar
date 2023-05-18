package com.example.mierda.calendar;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.stage.WindowEvent;

public class EventWindow {
    private VBox container;
    private CalendarModel calendarModel;
    private Button saveButton;
    private TextField eventNameTextField;
    private TextField eventDescriptionTextField;

    public EventWindow(Window window, CalendarModel calendarModel) {
        final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(window);
        this.container = new VBox(20);
        this.calendarModel = calendarModel;
        Scene dialogScene = new Scene(this.container, 500, 500);
        this.eventNameTextField = this.addLabeledTextField("Событие:", "");
        this.eventDescriptionTextField =
            this.addLabeledTextField("Описание:", "");
        this.saveButton = this.addSaveButton();
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

    private void saveEvent() { System.out.println("Saving... i guess"); }

    private Button addSaveButton() {
        Button saveButton = new Button("Сохранить");
        this.container.getChildren().add(saveButton);
        saveButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {
                saveEvent();
            }
        });
        return saveButton;
    }
}

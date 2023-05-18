package com.example.mierda.calendar;

import java.util.ArrayList;
import java.util.Calendar;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.stage.WindowEvent;

public class EventWindow {
    private VBox container;
    private VBox displayWindowContainer;
    private CalendarModel calendarModel;
    private Button saveButton;
    private Button deleteButton;
    private Calendar eventDate;
    private TextField eventNameTextField;
    private TextField eventDescriptionTextField;
    private ArrayList<CalendarEvent> attachedEvents;
    private TextField eventCreationTextField;
    private Button eventCreationButton;
    private boolean isDisplayWindow;
    private Scene dialogScene;
    private CalendarEvent attachedEvent;

    public EventWindow(Window window, CalendarModel calendarModel,
                       Calendar eventDate,
                       ArrayList<CalendarEvent> attachedEvents) {
        final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(window);
        this.attachedEvents = attachedEvents;
        this.eventDate = eventDate;
        this.container = new VBox(20);
        this.generateEventCreationScene();
        this.isDisplayWindow = false;
        this.calendarModel = calendarModel;
        this.dialogScene = new Scene(this.container, 500, 500);
        this.eventNameTextField = this.addLabeledTextField("Событие:", "");
        this.eventDescriptionTextField =
            this.addLabeledTextField("Описание:", "");
        this.saveButton = this.addSaveButton();
        this.deleteButton = this.addDeleteButton();
        dialog.setResizable(false);
        dialog.setScene(dialogScene);
        this.swapWindows();
        dialog.show();
    }

    private void swapWindows() {
        this.isDisplayWindow = !this.isDisplayWindow;
        if (this.isDisplayWindow) {
            this.generateEventCreationScene();
            this.dialogScene.setRoot(this.displayWindowContainer);
        } else {
            this.generateEventCreationScene();
            this.deleteButton.setVisible(this.attachedEvent != null);
            this.dialogScene.setRoot(this.container);
        }
    }

    private HBox addEventCreationComponent() {
        HBox container = new HBox();
        this.eventCreationTextField = new TextField();
        container.getChildren().add(this.eventCreationTextField);
        this.eventCreationButton = new Button("+");
        this.eventCreationButton.setOnMouseClicked(event -> {
            this.clearCreationScene();
            this.eventNameTextField.setText(
                this.eventCreationTextField.getText());
            this.swapWindows();
        });
        container.getChildren().add(this.eventCreationButton);
        return container;
    }

    private void generateEventCreationScene() {
        this.displayWindowContainer = new VBox(20);
        this.attachedEvents.forEach(event -> {
            Label eventLabel = new Label(event.getName());
            eventLabel.setOnMouseClicked(e -> {
                this.clearCreationScene();
                this.eventNameTextField.setText(event.getName());
                this.eventDescriptionTextField.setText(event.getDescription());
                this.attachedEvent = event;
                this.swapWindows();
            });
            displayWindowContainer.getChildren().add(eventLabel);
        });
        this.displayWindowContainer.getChildren().add(
            this.addEventCreationComponent());
    }

    private TextField addLabeledTextField(String labelText,
                                          String textFieldText) {
        this.container.getChildren().add(new Label(labelText));
        TextField field = new TextField(textFieldText);
        this.container.getChildren().add(field);
        return field;
    }

    private void clearCreationScene() {
        this.eventDescriptionTextField.setText("");
        this.eventNameTextField.setText("");
        this.attachedEvent = null;
    }

    private CalendarEvent createEvent() {
        return new CalendarEvent(this.eventDate,
                                 this.eventNameTextField.getText(),
                                 this.eventDescriptionTextField.getText());
    }

    private void saveEvent() {
        CalendarEvent event = this.createEvent();
        this.attachedEvents.add(event);
        if (this.attachedEvent == null) {
            this.calendarModel.addEvent(event);
        } else {
            this.attachedEvents.remove(this.attachedEvent);
            this.calendarModel.replaceEvent(this.attachedEvent, event);
        }
    }

    private void removeEvent() {
        System.out.println(this.attachedEvent);
        this.calendarModel.removeEvent(this.attachedEvent);
        this.attachedEvents.remove(this.attachedEvent);
        System.out.print(this.attachedEvents);
        this.generateEventCreationScene();
    }

    private Button addSaveButton() {
        Button saveButton = new Button("Сохранить");
        this.container.getChildren().add(saveButton);
        saveButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {
                saveEvent();
                clearCreationScene();
                swapWindows();
            }
        });
        return saveButton;
    }

    private Button addDeleteButton() {
        Button deleteButton = new Button("Удалить");
        this.container.getChildren().add(deleteButton);
        deleteButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {
                removeEvent();
                clearCreationScene();
                swapWindows();
            }
        });
        return deleteButton;
    }
}

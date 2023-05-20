package com.example.mierda.calendar;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

public class EventWindow {
    final static String BUTTON_STYLE =
        "-fx-background-color: #f2f2f2; -fx-background-radius: 20; -fx-font-weight: 700";

    private VBox container;
    private VBox displayWindowContainer;
    private CalendarModel calendarModel;
    private Button saveButton;
    private HBox sceneAContainer;
    private HBox sceneBContainer;
    private VBox sceneARightContainter;
    private VBox sceneBRightContainter;
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
        this.sceneAContainer = new HBox(10);
        this.sceneBContainer = new HBox(10);
        this.sceneAContainer.setStyle("-fx-background-color: white");
        this.sceneAContainer.setMinWidth(400);
        this.sceneBContainer.setStyle("-fx-background-color: white");
        this.sceneARightContainter = new VBox(10);
        this.sceneBRightContainter = new VBox(10);
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(window);
        this.attachedEvents = attachedEvents;
        this.eventDate = eventDate;
        this.container = new VBox(20);
        this.sceneBContainer.getChildren().add(this.container);
        this.generateEventCreationScene();
        this.isDisplayWindow = false;
        this.calendarModel = calendarModel;
        this.dialogScene = new Scene(this.sceneAContainer, 800, 500);
        Label label = new Label("Новое событие");
        label.setStyle("-fx-font-size: 25; -fx-font-weight: 700");
        this.container.setPadding(new Insets(30, 60, 60, 60));
        this.container.getChildren().add(label);
        this.eventNameTextField = this.addLabeledTextField("Событие:", "");
        this.eventDescriptionTextField =
            this.addLabeledTextField("Описание:", "");
        this.saveButton = this.addSaveButton();
        this.deleteButton = this.addDeleteButton();
        this.sceneBContainer.getChildren().add(this.createSceneBRightPart());
        dialog.setResizable(false);
        dialog.setScene(dialogScene);
        this.swapWindows();
        dialog.show();
    }

    private void swapWindows() {
        this.isDisplayWindow = !this.isDisplayWindow;
        if (this.isDisplayWindow) {
            this.generateEventCreationScene();
            this.dialogScene.setRoot(this.sceneAContainer);
        } else {
            this.generateEventCreationScene();
            this.deleteButton.setVisible(this.attachedEvent != null);
            this.dialogScene.setRoot(this.sceneBContainer);
        }
    }

    private VBox createSceneARightPart() {
        VBox sceneARightContainter = new VBox();
        File file = new File(System.getProperty("user.dir") +
                             "/src/main/images/SceneALeftImage.png");
        Image image = new Image(file.toURI().toString());
        ImageView imageView = new ImageView(image);
        sceneARightContainter.getChildren().add(imageView);
        sceneARightContainter.setStyle("-fx-background-color: white");
        sceneARightContainter.setPadding(new Insets(110));
        return sceneARightContainter;
    }

    private VBox createSceneBRightPart() {
        VBox sceneBRightContainter = new VBox();
        File file = new File(System.getProperty("user.dir") +
                             "/src/main/images/SceneBRightImage.png");
        Image image = new Image(file.toURI().toString());
        ImageView imageView = new ImageView(image);
        sceneBRightContainter.getChildren().add(imageView);
        sceneBRightContainter.setStyle("-fx-background-color: white");
        sceneBRightContainter.setPadding(new Insets(120));
        return sceneBRightContainter;
    }

    private HBox addEventCreationComponent() {
        HBox container = new HBox();
        this.eventCreationTextField = new TextField();
        container.getChildren().add(this.eventCreationTextField);
        this.eventCreationButton = new Button("+");
        this.eventCreationButton.setStyle(BUTTON_STYLE);
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
        this.sceneAContainer.getChildren().clear();
        this.displayWindowContainer = new VBox(20);
        this.sceneARightContainter = this.createSceneARightPart();
        this.displayWindowContainer.setStyle("-fx-background-color: white");
        this.displayWindowContainer.setPadding(new Insets(30, 60, 60, 60));
        this.sceneAContainer.getChildren().add(this.displayWindowContainer);
        this.sceneAContainer.getChildren().add(this.sceneARightContainter);
        Label label = new Label("События");
        label.setStyle("-fx-font-size: 25; -fx-font-weight: 700");
        this.displayWindowContainer.getChildren().add(label);
        this.attachedEvents.forEach(event -> {
            Pane pane = new Pane();
            pane.setMinWidth(20);
            pane.setMinHeight(10);
            pane.setStyle(
                "-fx-background-color: #caef6d; -fx-background-radius: 30px 15px;");
            Label eventLabel = new Label(event.getName());
            eventLabel.setOnMouseClicked(e -> {
                this.clearCreationScene();
                this.eventNameTextField.setText(event.getName());
                this.eventDescriptionTextField.setText(event.getDescription());
                this.attachedEvent = event;
                this.swapWindows();
            });
            HBox container = new HBox(10);
            container.getChildren().add(pane);
            container.getChildren().add(eventLabel);
            this.displayWindowContainer.getChildren().add(container);
        });
        this.displayWindowContainer.getChildren().add(
            this.addEventCreationComponent());
    }

    private TextField addLabeledTextField(String labelText,
                                          String textFieldText) {
        Label label = new Label(labelText);
        label.setStyle("-fx-font-size: 14; -fx-font-weight: 700");
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
        this.calendarModel.removeEvent(this.attachedEvent);
        this.attachedEvents.remove(this.attachedEvent);
        this.generateEventCreationScene();
    }

    private Button addSaveButton() {
        Button saveButton = new Button("Сохранить");
        saveButton.setStyle(BUTTON_STYLE);
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
        deleteButton.setStyle(BUTTON_STYLE);
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

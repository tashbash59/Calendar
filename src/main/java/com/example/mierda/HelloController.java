package com.example.mierda;

import com.example.mierda.calendar.CalendarData;
import com.example.mierda.calendar.CalendarEntry;
import com.example.mierda.calendar.CalendarModel;
import com.example.mierda.calendar.TaskLink;
import com.example.mierda.tasks.TaskCreationWindow;
import com.example.mierda.tasks.TaskModel;
import com.example.mierda.tasks.TaskpaneController;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.ResourceBundle;
import java.util.Vector;
import javafx.animation.Animation;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

public class HelloController implements Initializable {
    @FXML public AnchorPane calendarComponent;
    @FXML public ImageView mierdaAnimation;
    @FXML public Button eat;
    @FXML public AnchorPane eatBar;
    @FXML public Button health;
    @FXML public AnchorPane healthBar;
    @FXML public Button happy;
    @FXML public AnchorPane happyBar;
    @FXML public Label moneyLabel;
    @FXML public AnchorPane gameAnchor;
    @FXML public AnchorPane taskPane;
    @FXML public Label monthLabel;
    @FXML private Button createTaskButton;

    private TaskModel taskModel;
    private CalendarModel calendarModel;
    final double onePartBar = 43.4;

    private final CalendarData calendarData = CalendarData.fromFilepath(
        System.getProperty("user.dir") +
        "/src/main/resources/com/example/mierda/calendarData.json");

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        moneyLabel.setText(Integer.toString(this.calendarData.getMoney()));
        this.taskModel = new TaskModel(this.taskPane);
        initAnimation();
        initCalendarComponent();
        initCreateTaskButton();
        eating(eatBar, eat);
        eating(healthBar, health);
        eating(happyBar, happy);
    }

    private void initCreateTaskButton() {
        this.createTaskButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {
                new TaskCreationWindow(createTaskButton.getScene().getWindow(),
                                       taskModel);
            }
        });
    }

    private void initCalendarComponent() {
        var children = new ArrayList<>(this.calendarComponent.getChildren());
        for (javafx.scene.Node child : children) {
            String id = child.getId();
            if (id == null)
                continue;
            if (!id.equals("CalendarEntry"))
                continue;
            this.calendarComponent.getChildren().remove(child);
        }

        if (this.calendarModel == null)
            this.calendarModel = new CalendarModel();

        this.monthLabel.setText(this.calendarModel.getMonthYearString());
        Vector<TaskLink> taskData = this.calendarData.getMonthTasks(
            this.calendarModel.getStartOfDisplayedMonth());
        if (taskData != null)
            taskData.forEach(taskLink -> {
                taskLink.getTasks().forEach(task -> {
                    try {
                        this.calendarModel.addTask(
                            Integer.parseInt(taskLink.getDay()), task);
                    } catch (Exception e) {
                        System.out.println("Could not parse task day due to: " +
                                           e);
                    }
                });
            });

        final double[] xOffsets = {22.0,  60.0,  104.0, 146.0,
                                   191.0, 235.0, 279.0};
        final double yCellWidth = 14.0;
        final double yBaseOffset = 10.0;

        for (int row = 0; row < this.calendarModel.getNumberOfRows(); ++row) {
            for (int column = 0;
                 column < this.calendarModel.getNumberOfColumns(); ++column) {
                CalendarEntry entry = this.calendarModel.getEntry(row, column);
                Label label = new Label(entry == null ? "" : entry.toString());
                label.setMinWidth(yCellWidth);
                boolean isToday =
                    (entry != null &&
                     entry.getDate().get(Calendar.DAY_OF_MONTH) ==
                         (Calendar.getInstance().get(Calendar.DAY_OF_MONTH)) &&
                     entry.getDate().get(Calendar.MONTH) ==
                         Calendar.getInstance().get(Calendar.MONTH) &&
                     entry.getDate().get(Calendar.YEAR) ==
                         Calendar.getInstance().get(Calendar.YEAR));
                String selectedStyle =
                    isToday
                        ? "-fx-background-color: #caef6d; -fx-background-radius: 30px 15px;"
                        : "";
                if (isToday) {
                    label.setMinWidth(yCellWidth * 1.75);
                }
                label.setLayoutX(
                    (isToday) ? (xOffsets[column] - yCellWidth / 1.75 / 2)
                              : xOffsets[column]);
                label.setLayoutY((yCellWidth + yBaseOffset) * (row + 2));
                label.setAlignment(Pos.CENTER);
                label.setId("CalendarEntry");
                label.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent e) {}
                });
                label.setStyle("-fx-font-size: 14px; -fx-font-weight: 400; " +
                               selectedStyle);
                this.calendarComponent.getChildren().add(label);
            }
        }
    }
    private void buttonAnimations(Button button, Image firstImage, int timer,
                                  Image secondImage, AnchorPane bar) {
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (bar.getPrefWidth() < bar.getMaxWidth() - onePartBar &&
                    calendarData.getMoney() > 0) {
                    mierdaAnimation.setImage(firstImage);
                    PauseTransition pause =
                        new PauseTransition(Duration.millis(timer));
                    pause.setOnFinished(
                        event1 -> mierdaAnimation.setImage(secondImage));
                    pause.play();
                }
            }
        });
    }

    private void initAnimation() {

        String currentDirectory = System.getProperty("user.dir");
        File sleepFile =
            new File(currentDirectory + "/src/main/images/sleep.png");
        Image sleep = new Image(sleepFile.toURI().toString());
        File defaultFile =
            new File(currentDirectory + "/src/main/images/default.png");
        Image defaultM = new Image(defaultFile.toURI().toString());
        File eatingFile =
            new File(currentDirectory + "/src/main/images/eat.png");
        Image eating = new Image(eatingFile.toURI().toString());
        File regenFile =
            new File(currentDirectory + "/src/main/images/regen1.png");
        Image regen = new Image(regenFile.toURI().toString());

        final int COLUMNS = 6;
        final int COUNT = 24;
        final int OFFSET_X = 1;
        final int OFFSET_Y = 1;
        final int WIDTH = 882;
        final int HEIGHT = 892;
        final int TIMER = 1500;

        mierdaAnimation.setImage(sleep);
        mierdaAnimation.setViewport(
            new Rectangle2D(OFFSET_X, OFFSET_Y, WIDTH, HEIGHT));
        final Animation animation =
            new SpriteAnimation(mierdaAnimation, Duration.millis(TIMER), COUNT,
                                COLUMNS, OFFSET_X, OFFSET_Y, WIDTH, HEIGHT);
        animation.setCycleCount(Animation.INDEFINITE);
        animation.play();
        gameAnchor.addEventFilter(
            MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    mierdaAnimation.setImage(defaultM);
                }
            });
        buttonAnimations(eat, eating, TIMER, defaultM, eatBar);
        buttonAnimations(health, regen, TIMER, defaultM, healthBar);
    }

    private void eating(AnchorPane bar, Button button) {
        button.addEventHandler(
            MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    if (bar.getPrefWidth() < bar.getMaxWidth() - onePartBar &&
                        calendarData.getMoney() > 0) {
                        bar.setPrefWidth(bar.getWidth() + onePartBar);
                        calendarData.addMoney(-10);
                        calendarData.save();
                        moneyLabel.setText(
                            Integer.toString(calendarData.getMoney()));
                    } else if (bar.getPrefWidth() < bar.getMaxWidth() &&
                               calendarData.getMoney() > 0) {
                        bar.setPrefWidth(bar.getMaxWidth());
                        calendarData.addMoney(-10);
                        calendarData.save();
                        moneyLabel.setText(
                            Integer.toString(calendarData.getMoney()));
                    }
                }
            });
    }

    @FXML
    public void selectNextMonth() {
        this.calendarModel.selectNextMonth();
        this.initCalendarComponent();
    }

    @FXML
    public void selectPreviousMonth() {
        this.calendarModel.selectPreviousMonth();
        this.initCalendarComponent();
    }
}

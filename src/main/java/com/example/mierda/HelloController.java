package com.example.mierda;

import com.example.mierda.calendar.CalendarData;
import com.example.mierda.calendar.CalendarEntry;
import com.example.mierda.calendar.CalendarModel;
import com.example.mierda.tasks.TaskCreationWindow;
import com.example.mierda.tasks.TaskModel;
import com.example.mierda.tasks.TaskpaneController;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

public class HelloController implements Initializable {
    @FXML private AnchorPane calendarComponent;
    @FXML private ImageView mierdaAnimation;
    @FXML private Button eat;
    @FXML private AnchorPane eatBar;
    @FXML private Button health;
    @FXML private AnchorPane healthBar;
    @FXML private Button happy;
    @FXML private AnchorPane happyBar;
    @FXML private Label moneyLabel;
    @FXML private AnchorPane gameAnchor;
    @FXML private AnchorPane taskPane;
    @FXML private Label monthLabel;
    @FXML private Button createTaskButton;
    @FXML private TextField createTaskText;
    @FXML private Button revival;

    private TaskModel taskModel;
    final double onePartBar = 43.4;

    private final CalendarData calendarData = CalendarData.fromFilepath(
        System.getProperty("user.dir") +
        "/src/main/resources/com/example/mierda/calendarData.json");
    private CalendarModel calendarModel;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        moneyLabel.setText(Integer.toString(this.calendarData.getMoney()));
        healthBar.setPrefWidth(calendarData.getHealth());
        happyBar.setPrefWidth(calendarData.getHappy());
        eatBar.setPrefWidth(calendarData.getHungry());
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
                String text = createTaskText.getText();
                if (text != null)
                    new TaskCreationWindow(
                        createTaskButton.getScene().getWindow(), taskModel,
                        text);
                else {
                    new TaskCreationWindow(
                        createTaskButton.getScene().getWindow(), taskModel);
                }
            }
        });
    }

    private void initCalendarComponent() {
        this.calendarModel = new CalendarModel(
            this.calendarData, this.calendarComponent, this.monthLabel);

        this.calendarModel.getController().update();
    }

    private void buttonAnimations(Button button, Image firstImage, int timer,
                                  Image secondImage, AnchorPane bar,
                                  SpriteAnimation animation, int count) {
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (healthBar.getPrefWidth() - 1 < healthBar.getMinWidth()) {
                    return;
                } else if (bar.getPrefWidth() <
                               bar.getMaxWidth() - onePartBar &&
                           calendarData.getMoney() > 10) {
                    animation.stop();
                    animation.setCount(count);
                    animation.play();
                    mierdaAnimation.setImage(firstImage);
                    animation.play();
                    PauseTransition pause =
                        new PauseTransition(Duration.millis(timer));
                    pause.setOnFinished(event1 -> {
                        animation.stop();
                        animation.setCount(24);
                        mierdaAnimation.setImage(secondImage);
                        animation.play();
                    });
                    pause.play();
                }
            }
        });
    }
    private void revivalButton(Button button, Image firstImage, int timer,
                               Image secondImage, AnchorPane bar,
                               SpriteAnimation animation) {
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (bar.getPrefWidth() - 1 < bar.getMinWidth() &&
                    calendarData.getMoney() > 99) {
                    animation.stop();
                    animation.setColumns(8);
                    animation.setCount(54);
                    animation.play();
                    mierdaAnimation.setImage(firstImage);
                    PauseTransition pause =
                        new PauseTransition(Duration.millis(timer));
                    pause.setOnFinished(event1 -> {
                        mierdaAnimation.setImage(secondImage);
                        animation.setColumns(6);
                        animation.setCount(24);
                    });
                    pause.play();
                    calendarData.addMoney(-100);
                    moneyLabel.setText(
                        Integer.toString(calendarData.getMoney()));
                    healthBar.setPrefWidth(healthBar.getMaxWidth());
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
            new File(currentDirectory + "/src/main/images/regen3.png");
        Image regen = new Image(regenFile.toURI().toString());
        File playFile =
            new File(currentDirectory + "/src/main/images/game.png");
        Image play = new Image(playFile.toURI().toString());
        File deathFile =
            new File(currentDirectory + "/src/main/images/death.png");
        Image death = new Image(deathFile.toURI().toString());
        File revivalFile =
            new File(currentDirectory + "/src/main/images/revival.png");
        Image revivalM = new Image(revivalFile.toURI().toString());

        final int COLUMNS = 6;
        final int COUNT = 24;
        final int OFFSET_X = 1;
        final int OFFSET_Y = 1;
        final int WIDTH = 882;
        final int HEIGHT = 892;
        final int TIMER = 1500;

        final Animation animation =
            new SpriteAnimation(mierdaAnimation, Duration.millis(TIMER), COUNT,
                                COLUMNS, OFFSET_X, OFFSET_Y, WIDTH, HEIGHT);
        if (healthBar.getPrefWidth() - 1 == health.getMinWidth()) {
            ((SpriteAnimation)animation).setColumns(1);
            ((SpriteAnimation)animation).setCount(1);
            mierdaAnimation.setImage(death);
        } else {
            mierdaAnimation.setImage(sleep);
        }
        mierdaAnimation.setViewport(
            new Rectangle2D(OFFSET_X, OFFSET_Y, WIDTH, HEIGHT));
        animation.setCycleCount(Animation.INDEFINITE);
        animation.play();
        gameAnchor.addEventFilter(
            MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    // mierdaAnimation.setImage(defaultM);
                }
            });
        buttonAnimations(eat, eating, TIMER, defaultM, eatBar,
                         ((SpriteAnimation)animation), 24);
        buttonAnimations(health, regen, TIMER, defaultM, healthBar,
                         ((SpriteAnimation)animation), 24);
        buttonAnimations(happy, play, TIMER, defaultM, happyBar,
                         ((SpriteAnimation)animation), 72);
        revivalButton(revival, revivalM, TIMER, defaultM, healthBar,
                      ((SpriteAnimation)animation));
    }

    private void eating(AnchorPane bar, Button button) {
        button.addEventHandler(
            MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    if (bar.getPrefWidth() < bar.getMaxWidth() - onePartBar &&
                        calendarData.getMoney() > 10) {
                        if (healthBar.getPrefWidth() - 1 <
                            healthBar.getMinWidth()) {
                            return;
                        } else {
                            bar.setPrefWidth(bar.getWidth() + onePartBar);
                            if (bar == healthBar)
                                calendarData.setHealth((int)bar.getWidth() +
                                                       (int)onePartBar);
                            else if (bar == happyBar)
                                calendarData.setHappy((int)bar.getWidth() +
                                                      (int)onePartBar);
                            else if (bar == eatBar)
                                calendarData.setHungry((int)bar.getWidth() +
                                                       (int)onePartBar);
                            calendarData.addMoney(-10);
                            moneyLabel.setText(
                                Integer.toString(calendarData.getMoney()));
                        }
                    } else if (bar.getPrefWidth() < bar.getMaxWidth() &&
                               calendarData.getMoney() > 10) {
                        if (healthBar.getPrefWidth() - 1 <
                            healthBar.getMinWidth()) {
                            return;
                        } else {
                            if (bar == healthBar)
                                calendarData.setHealth((int)bar.getMaxWidth());
                            else if (bar == happyBar)
                                calendarData.setHappy((int)bar.getMaxWidth());
                            else if (bar == eatBar)
                                calendarData.setHungry((int)bar.getMaxWidth());
                            bar.setPrefWidth(bar.getMaxWidth());
                            calendarData.addMoney(-10);
                            moneyLabel.setText(
                                Integer.toString(calendarData.getMoney()));
                        }
                    } else {
                        ModalWindow.newWindow();
                    }
                }
            });
    }

    @FXML
    public void selectNextMonth() {
        this.calendarModel.selectNextMonth();
    }

    @FXML
    public void selectPreviousMonth() {
        this.calendarModel.selectPreviousMonth();
    }
}

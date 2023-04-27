package com.example.mierda;

import com.example.mierda.calendar.CalendarData;
import com.example.mierda.calendar.CalendarEntry;
import com.example.mierda.calendar.CalendarModel;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.ResourceBundle;
import javafx.animation.Animation;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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

    private CalendarModel calendarModel;

    // private CalendarData calendarData = CalendarData.fromPath(
    //     System.getProperty("user.dir") +
    //     "/src/main/resources/com/example/mierda/calendarData.json");
    private CalendarData calendarData = CalendarData.getDefaultData();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        moneyLabel.setText(Integer.toString(this.calendarData.getMoney()));
        initAnimation();
        initCalendarComponent();
        eating(eatBar, eat);
        eating(healthBar, health);
        eating(happyBar, happy);
    }

    private void initCalendarComponent() {
        this.calendarData.toJSONObect();
        this.calendarData.save();
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
        this.calendarModel.generateRandomTasks();
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
                if (isToday)
                    label.setMinWidth(yCellWidth * 1.75);
                label.setLayoutX(xOffsets[column]);
                label.setLayoutY((yCellWidth + yBaseOffset) * (row + 2));
                label.setAlignment(Pos.CENTER);
                label.setId("CalendarEntry");
                label.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        if (entry != null) {
                            System.out.println(entry.getTask());
                        }
                    }
                });
                label.setStyle("-fx-font-size: 14px; -fx-font-weight: 400; " +
                               selectedStyle);
                this.calendarComponent.getChildren().add(label);
            }
        }
    }
    public void initAnimation() {

        String currentDirectory = System.getProperty("user.dir");
        File animationFile =
            new File(currentDirectory + "/src/main/images/sleep.png");
        Image ANIMATION_IMAGE = new Image(animationFile.toURI().toString());
        File defaultMierda =
            new File(currentDirectory + "/src/main/images/default.png");
        Image MIERDA = new Image(defaultMierda.toURI().toString());

        final int COLUMNS = 3;
        final int COUNT = 9;
        final int OFFSET_X = 1;
        final int OFFSET_Y = 1;
        final int WIDTH = 882;
        final int HEIGHT = 892;
        final int timer = 2000;

        mierdaAnimation.setImage(ANIMATION_IMAGE);
        mierdaAnimation.setViewport(
            new Rectangle2D(OFFSET_X, OFFSET_Y, WIDTH, HEIGHT));
        final Animation animation =
            new SpriteAnimation(mierdaAnimation, Duration.millis(timer), COUNT,
                                COLUMNS, OFFSET_X, OFFSET_Y, WIDTH, HEIGHT);
        animation.setCycleCount(Animation.INDEFINITE);
        animation.play();
        gameAnchor.addEventFilter(
            MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    mierdaAnimation.setImage(MIERDA);
                }
            });
    }

    private void eating(AnchorPane bar, Button button) {
        final double onePartBar = 43.4;
        button.addEventHandler(
            MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    if (bar.getPrefWidth() < bar.getMaxWidth() - onePartBar) {
                        bar.setPrefWidth(bar.getWidth() + onePartBar);
                        calendarData.addMoney(-10);
                        moneyLabel.setText(
                            Integer.toString(calendarData.getMoney()));
                    } else if (bar.getPrefWidth() < bar.getMaxWidth()) {
                        bar.setPrefWidth(bar.getMaxWidth());
                        calendarData.addMoney(-10);
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

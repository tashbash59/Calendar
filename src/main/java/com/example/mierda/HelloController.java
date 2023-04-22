package com.example.mierda;

import com.example.mierda.calendar.CalendarEntry;
import com.example.mierda.calendar.CalendarModel;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.animation.Animation;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.image.Image;
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

    static Integer money = 1000;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        moneyLabel.setText(money.toString());
        initAnimation();
        initCalendarComponent();
        eating(eatBar,eat);
        eating(healthBar,health);
        eating(happyBar,happy);
    }

    private void initCalendarComponent() {
        CalendarModel calendarModel = new CalendarModel();
        calendarModel.generateRandomTasks();
        final double[] xOffsets = {22.0,  60.0,  104.0, 146.0,
                                   191.0, 235.0, 279.0};
        final double yCellWidth = 14.0;
        final double yBaseOffset = 10.0;
        for (int row = 0; row < calendarModel.getNumberOfRows(); ++row) {
            for (int column = 0; column < calendarModel.getNumberOfColumns();
                 ++column) {
                CalendarEntry entry = calendarModel.getEntry(row, column);
                Label label = new Label(entry == null ? "" : entry.toString());
                label.setLayoutX(xOffsets[column]);
                label.setLayoutY((yCellWidth + yBaseOffset) * (row + 2));
                label.setMinWidth(yCellWidth);
                label.setAlignment(Pos.CENTER);
                label.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        System.out.println(entry.getTask());
                    }
                });
                label.setStyle("-fx-font-size: 14px; -fx-font-weight: 400;");
                this.calendarComponent.getChildren().add(label);
            }
        }
    }
    public void initAnimation() {


        File animationFile = new File("C:\\Users\\Danil\\Desktop\\python\\CW\\src\\main\\images\\sleep.png");
        Image ANIMATION_IMAGE = new Image(animationFile.toURI().toString());
        File defaultMierda = new File("C:\\Users\\Danil\\Desktop\\python\\CW\\src\\main\\images\\mierda.png");
        Image MIERDA = new Image(defaultMierda.toURI().toString());

        final int COLUMNS  =  3;
        final int COUNT    =  9;
        final int OFFSET_X =  1;
        final int OFFSET_Y =  1;
        final int WIDTH    = 882;
        final int HEIGHT   = 892;

        mierdaAnimation.setImage(ANIMATION_IMAGE);
        mierdaAnimation.setViewport(new Rectangle2D(OFFSET_X, OFFSET_Y, WIDTH, HEIGHT));
        //final ImageView imageView = new ImageView(IMAGE);
        //imageView.setViewport(new Rectangle2D(OFFSET_X, OFFSET_Y, WIDTH, HEIGHT));
        final Animation animation = new SpriteAnimation(
                mierdaAnimation,
                Duration.millis(1000),
                COUNT, COLUMNS,
                OFFSET_X, OFFSET_Y,
                WIDTH, HEIGHT
        );
        animation.setCycleCount(Animation.INDEFINITE);
        animation.play();
    }

    private void eating(AnchorPane bar,Button button) {
        final double onePartBar = 43.4;
        button.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (bar.getPrefWidth() < bar.getMaxWidth() - onePartBar) {
                    bar.setPrefWidth(bar.getWidth() + onePartBar);
                    money -= 10;
                    moneyLabel.setText(money.toString());
                } else if (bar.getPrefWidth() < bar.getMaxWidth()) {
                    bar.setPrefWidth(bar.getMaxWidth());
                    money -= 10;
                    moneyLabel.setText(money.toString());
                }
            }
        });
    }
}

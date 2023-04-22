package com.example.mierda;

import javafx.animation.Animation;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;

public class HelloApplication extends Application {

    static File file = new File("C:\\Users\\Danil\\Desktop\\python\\pr\\src\\main\\java\\com\\example\\mierda\\Frame.png");
    private static final Image IMAGE = new Image(file.toURI().toString());


    private static final int COLUMNS  =   2;
    private static final int COUNT    =  4;
    private static final int OFFSET_X =  18;
    private static final int OFFSET_Y =  25;
    private static final int WIDTH    = 882;
    private static final int HEIGHT   = 892;

    @Override
    public void start(Stage stage) throws IOException {
        final ImageView imageView = new ImageView(IMAGE);
        imageView.setViewport(new Rectangle2D(OFFSET_X, OFFSET_Y, WIDTH, HEIGHT));
        final Animation animation = new SpriteAnimation(
                imageView,
                Duration.millis(2000),
                COUNT, COLUMNS,
                OFFSET_X, OFFSET_Y,
                WIDTH, HEIGHT
        );
        animation.setCycleCount(Animation.INDEFINITE);
        animation.play();

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        //Scene scene = new Scene(fxmlLoader.load(), 1016, 654);
        //stage.setScene(scene);
        stage.setScene(new Scene(new Group(imageView)));
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}

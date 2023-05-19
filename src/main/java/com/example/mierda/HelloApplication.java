package com.example.mierda;

import java.io.File;
import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class HelloApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(
            HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1016, 654);
        scene.getStylesheets().add(
            HelloApplication.class.getResource("style.css").toExternalForm());
        stage.setResizable(false);
        File file = new File(System.getProperty("user.dir")+"/src/main/images/icon.png");
        Image image = new Image(file.toURI().toString());
        stage.setTitle("Миерда");

        stage.setScene(scene);
        stage.getIcons().add(image);
        stage.show();
    }

    public static void main(String[] args) { launch(); }
}

package com.example.mierda;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class ModalWindow {
    @FXML private Button Сlose;
    @FXML private Label text;

    public static void newWindow() {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        FXMLLoader fxmlLoader = new FXMLLoader(
                HelloApplication.class.getResource("dialog.fxml"));

        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load(),287,188);
        } catch (IOException e) {
            e.printStackTrace();
        }
        window.setResizable(false);
        window.initStyle(StageStyle.UNDECORATED);
        window.setScene(scene);
        window.setTitle("окно");
        window.showAndWait();
    }
}

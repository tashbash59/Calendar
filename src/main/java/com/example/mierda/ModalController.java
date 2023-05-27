package com.example.mierda;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class ModalController implements Initializable {
    @FXML private Button Сlose;
    @FXML private ImageView img;

    public void initialize(URL url, ResourceBundle resourceBundle) {
        Image mierda = new Image(this.getClass().getResourceAsStream("/images/mierda.png"));

        img.setImage(mierda);
        Сlose.setOnAction(actionEvent -> {
            Stage stage = (Stage) Сlose.getScene().getWindow();
            stage.close();
        });
    }
}
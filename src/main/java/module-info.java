module com.example.mierda {
    requires javafx.controls;
    requires javafx.fxml;
    requires json;


    opens com.example.mierda to javafx.fxml;
    exports com.example.mierda;
}
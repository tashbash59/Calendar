module com.example.mierda {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.mierda to javafx.fxml;
    exports com.example.mierda;
}
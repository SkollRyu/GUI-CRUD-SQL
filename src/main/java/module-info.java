module com.example.crud {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens app to javafx.fxml;
    exports app;
    exports Controller;
    opens Controller to javafx.fxml;
}
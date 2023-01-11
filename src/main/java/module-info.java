module com.example.sw_two {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens com.sw_two to javafx.fxml;
    exports com.sw_two;
    exports com.sw_two.database;
    opens com.sw_two.database to javafx.fxml;
}
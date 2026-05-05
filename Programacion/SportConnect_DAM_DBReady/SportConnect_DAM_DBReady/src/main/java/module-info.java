module org.example.sportconnect_01 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens org.example.sportconnect_01 to javafx.fxml;
    opens org.example.sportconnect_01.controller to javafx.fxml;

    exports org.example.sportconnect_01;
    exports org.example.sportconnect_01.controller;
    exports org.example.sportconnect_01.model;
    exports org.example.sportconnect_01.service;
    exports org.example.sportconnect_01.util;
}

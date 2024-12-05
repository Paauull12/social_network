module org.example.projfinal {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens org.example.projfinal to javafx.fxml;
    exports org.example.projfinal;
}
module com.example.pos {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires jasperreports;


    opens com.example.pos to javafx.fxml;
    exports com.example.pos;

}
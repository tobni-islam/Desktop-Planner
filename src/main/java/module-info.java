module com.example.tp {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;

    opens com.example.tp to javafx.fxml;
    exports com.example.tp;

}
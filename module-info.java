module com.example.demo2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.base;
    requires javafx.media;
    requires javafx.graphics;
    requires javafx.web;

    requires org.json;


    opens com.example.demo2 to javafx.fxml;
    exports com.example.demo2;

}
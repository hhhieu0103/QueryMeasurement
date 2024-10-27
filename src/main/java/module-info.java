module org.hieuho.querymeasurement {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.kordamp.ikonli.javafx;
    requires com.opencsv;
    requires java.sql;
    requires javafx.swing;
    requires org.slf4j;
    requires jdk.compiler;

    opens org.hieuho.querymeasurement to javafx.fxml;
    exports org.hieuho.querymeasurement;
}
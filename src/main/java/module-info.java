module sample.demo {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.apache.logging.log4j;
    requires java.sql;
    requires org.apache.logging.log4j.core;

    opens sample.demo to javafx.fxml;
    exports sample.demo;
}
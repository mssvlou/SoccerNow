module pt.ul.fc.css.soccernow {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires transitive javafx.graphics;
    requires com.fasterxml.jackson.databind;
    requires java.net.http;

    opens pt.ul.fc.css.soccernow to javafx.fxml, javafx.web;
    opens pt.ul.fc.css.soccernow.controllers to javafx.fxml;

    exports pt.ul.fc.css.soccernow;
    exports pt.ul.fc.css.soccernow.dto;
}

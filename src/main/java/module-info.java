module com.mily.stacja {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.xml;
    requires java.desktop;


    opens com.mily.stacja to javafx.fxml;
    exports com.mily.stacja;
}
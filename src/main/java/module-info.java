module com.group8.playlistx {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;

    opens com.group8.playlistx to javafx.fxml;
    exports com.group8.playlistx;
}
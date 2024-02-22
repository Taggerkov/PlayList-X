module com.group8.playlistx {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires org.jetbrains.annotations;

    opens com.playlistx to javafx.fxml;
    exports com.playlistx;
}
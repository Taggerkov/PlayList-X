module com.playlistx {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires org.jetbrains.annotations;
    requires java.desktop;

    opens com.playlistx to javafx.fxml;
    opens com.playlistx.view to javafx.fxml;
    opens com.playlistx.viewmodel to javafx.fxml;
    opens com.playlistx.model.utils to javafx.fxml;

    exports com.playlistx;
    exports com.playlistx.model.utils;
}
/**
 * PlayList X client Module Info.
 * <br>
 * Controls runtime access and required add-ons.
 */
module com.playlistx {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires java.desktop;
    requires org.jetbrains.annotations;
    requires java.rmi;
    requires remoteobserver;
    requires java.net.http;
    requires java.sql;
    requires javafx.web;

    opens com.playlistx to javafx.fxml;
    opens com.playlistx.view to javafx.fxml;
    exports com.playlistx;
    opens com.playlistx.model to javafx.fxml;
}
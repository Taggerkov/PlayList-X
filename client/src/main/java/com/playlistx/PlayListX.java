package com.playlistx;

import com.playlistx.view.ViewHandler;
import javafx.application.Application;
import javafx.stage.Stage;

public class PlayListX extends Application {
    @Override
    public void start(Stage primaryStage) {
        ViewHandler.get();
    }
}

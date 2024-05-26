package com.playlistx;

import com.playlistx.view.ViewHandler;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * PlayList X client Application.
 * Runs the client with and integrated UI powered by JavaFX.
 *
 * @author Sergiu Chirap
 * @version final
 * @see javafx.application.Application
 * @since 0.1
 */

public class PlayListX extends Application {
    /**
     * The main entry point for all JavaFX applications.
     * The start method is called after the init method has returned,
     * and after the system is ready for the application to begin running.
     *
     * <p>
     * NOTE: This method is called on the JavaFX Application Thread.
     * </p>
     *
     * @param primaryStage the primary stage for this application, onto which
     *                     the application scene can be set.
     *                     Applications may create other stages, if needed, but they will not be
     *                     primary stages.
     */
    @Override
    public void start(Stage primaryStage) {
        ViewHandler.get();
    }
}
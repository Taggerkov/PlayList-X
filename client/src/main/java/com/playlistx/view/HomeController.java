package com.playlistx.view;

import com.playlistx.model.paths.FXMLs;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import org.jetbrains.annotations.NotNull;

public class HomeController implements Controller {
    private static HomeController instance;
    private Scene scene;
    @FXML
    private ScrollPane favouritesDisplay;

    private HomeController() {
    }

    public static HomeController get() {
        if (instance == null) return instance = new HomeController();
        else return instance;
    }

    @Override
    public void init(@NotNull Scene scene) {
        this.scene = scene;
    }

    @Override
    public String getFXML() {
        return FXMLs.home;
    }

    @Override
    public Scene getScene() {
        return scene;
    }

    @FXML
    private void refresh() {

    }

    @FXML
    private void openSettings() {

    }

    @FXML
    private void toggleFavorites() {
        favouritesDisplay.setVisible(!favouritesDisplay.isVisible());
    }
}
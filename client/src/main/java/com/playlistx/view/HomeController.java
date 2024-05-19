package com.playlistx.view;

import com.playlistx.model.paths.FXMLs;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.VBox;
import org.jetbrains.annotations.NotNull;

public class HomeController implements Controller {
    private static HomeController instance;
    private Scene scene;
    @FXML
    private VBox goTo;
    @FXML
    private TabPane viewSwitch;
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
        Views.HOME.show();
    }

    @FXML
    private void openSettings() {
        Views.SETTINGS.show();
    }

    @FXML
    private void toggleGoTo() {
        goTo.setVisible(!goTo.isVisible());
    }

    @FXML
    private void toggleFavorites() {
        favouritesDisplay.setVisible(!favouritesDisplay.isVisible());
    }

    @FXML
    private void goToHome() {
        Views.HOME.show();
    }

    @FXML
    private void goToAllSongs() {
        Views.SONGLIST.show();
    }

    @FXML
    private void goToPlaylists() {
        Views.ALL_PLAYLISTS.show();
    }

    public void injectTab(Tab tab) {
        viewSwitch.getTabs().add(tab);
    }

    public void switchTab(Tab tab) {
        viewSwitch.getSelectionModel().select(tab);
    }
}
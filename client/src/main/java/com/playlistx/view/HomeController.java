package com.playlistx.view;

import com.playlistx.model.paths.CSS;
import com.playlistx.model.paths.FXMLs;
import com.playlistx.viewmodel.HomeModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.VBox;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class HomeController implements Controller {
    private static HomeController instance;
    private final HomeModel model = HomeModel.get();
    private Scene scene;
    @FXML
    private VBox settings, goTo;
    @FXML
    private TabPane viewSwitch;
    @FXML
    private ScrollPane favouritesDisplay;
    @FXML
    private ChoiceBox<CSS> themeSelector;

    private HomeController() {
    }

    public static HomeController get() {
        if (instance == null) return instance = new HomeController();
        else return instance;
    }

    @Override
    public void init(@NotNull Scene scene) {
        this.scene = scene;
        themeSelector.setItems(FXCollections.observableArrayList(CSS.LIGHT, CSS.DARK));
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
    private void toggleSettings() {
        settings.setVisible(!settings.isVisible());
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
    private void confirmTheme() {
        CSS.setCSS(themeSelector.getSelectionModel().getSelectedItem());
        settings.setVisible(false);
    }

    @FXML
    private void goToHome() {
        Views.HOME.show();
        goTo.setVisible(false);
    }

    @FXML
    private void goToAllSongs() {
        Views.SONGLIST.show();
        goTo.setVisible(false);
    }

    @FXML
    private void goToPlaylists() {
        Views.ALL_PLAYLISTS.show();
        goTo.setVisible(false);
    }

    public void injectTab(Tab tab) {
        viewSwitch.getTabs().add(tab);
    }

    public void switchTab(Tab tab) {
        if (tab == null)  viewSwitch.getSelectionModel().select(0);
        viewSwitch.getSelectionModel().select(tab);
    }
}
package com.playlistx.view;

import com.playlistx.model.paths.CSS;
import com.playlistx.model.paths.FXMLs;
import com.playlistx.viewmodel.HomeModel;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import org.jetbrains.annotations.NotNull;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class HomeController implements Controller, PropertyChangeListener {
    private static HomeController instance;
    private final HomeModel model = HomeModel.get();
    private final String STYLE_RED = "-fx-border-color: red; -fx-border-width: 2px";
    private Scene scene;
    @FXML
    private VBox profile, settings, goTo;
    @FXML
    private TabPane viewSwitch;
    @FXML
    private ScrollPane favouritesDisplay;
    @FXML
    private TextField newName, oldPass, newPass, menuNewName, menuOldPass, menuNewPass;
    @FXML
    private ImageView nameAtn, passAtn;
    @FXML
    private ChoiceBox<CSS> themeSelector;

    private HomeController() {
        model.addListener(this);
    }

    public static HomeController get() {
        if (instance == null) return instance = new HomeController();
        else return instance;
    }

    @Override
    public void init(@NotNull Scene scene) {
        this.scene = scene;
        themeSelector.setItems(FXCollections.observableArrayList(model.getCSS()));
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
    private void toggleProfile() {
        if (!profile.isVisible()) cleanProfile();
        profile.setVisible(!profile.isVisible());
        settings.setVisible(false);
        goTo.setVisible(false);
    }

    @FXML
    private void toggleSettings() {
        settings.setVisible(!settings.isVisible());
        profile.setVisible(false);
        goTo.setVisible(false);
    }

    @FXML
    private void toggleGoTo() {
        goTo.setVisible(!goTo.isVisible());
        profile.setVisible(false);
        settings.setVisible(false);
    }

    @FXML
    private void toggleFavorites() {
        favouritesDisplay.setVisible(!favouritesDisplay.isVisible());
    }

    @FXML
    private void newUsername(@NotNull ActionEvent evt) {
        if (evt.getSource() == nameAtn)
            if (!model.changeUsername(newName.getText(), oldPass.getText())) {
                ViewHandler.popUp(ViewHandler.Notify.FILE, "Wrong Password or Invalid Username");
                oldPass.setStyle(STYLE_RED);
                newName.setStyle(STYLE_RED);
            } else {
                oldPass.setStyle("");
                newName.setStyle("");
            }
        else {
            //noinspection DuplicatedCode
            if (!model.changeUsername(menuNewName.getText(), menuOldPass.getText())) {
                ViewHandler.popUp(ViewHandler.Notify.FILE, "Wrong Password or Invalid Username");
                menuOldPass.setStyle(STYLE_RED);
                menuNewName.setStyle(STYLE_RED);
            } else {
                menuOldPass.setStyle("");
                menuNewName.setStyle("");
            }
        }
        cleanProfile();
    }

    @FXML
    private void newPassword(@NotNull ActionEvent evt) {
        if (evt.getSource() == passAtn)
            if (!model.changePassword(oldPass.getText(), newPass.getText())) {
                ViewHandler.popUp(ViewHandler.Notify.FILE, "Wrong Password or Invalid Password!");
                oldPass.setStyle(STYLE_RED);
                newPass.setStyle(STYLE_RED);
            } else {
                oldPass.setStyle("");
                newPass.setStyle("");
            }
        else {
            //noinspection DuplicatedCode
            if (!model.changePassword(menuOldPass.getText(), menuNewPass.getText())) {
                ViewHandler.popUp(ViewHandler.Notify.FILE, "Wrong Password or Invalid Password!");
                menuOldPass.setStyle(STYLE_RED);
                menuNewPass.setStyle(STYLE_RED);
            } else {
                menuOldPass.setStyle("");
                menuNewPass.setStyle("");
            }
        }
        cleanProfile();
    }

    private void cleanProfile() {
        newName.setText("");
        oldPass.setText("");
        newPass.setText("");
        menuNewName.setText("");
        menuOldPass.setText("");
        menuNewPass.setText("");
    }

    @FXML
    private void confirmTheme() {
        model.setCSS(themeSelector.getSelectionModel().getSelectedItem());
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

    @FXML
    private void close() {
        model.close();
    }

    public void injectTab(Tab tab) {
        viewSwitch.getTabs().add(tab);
    }

    public void switchTab(Tab tab) {
        if (tab == null) viewSwitch.getSelectionModel().select(0);
        viewSwitch.getSelectionModel().select(tab);
    }

    /**
     * This method gets called when a bound property is changed.
     *
     * @param evt A PropertyChangeEvent object describing the event source
     *            and the property that has changed.
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equalsIgnoreCase("REFRESH")) refresh();
    }
}
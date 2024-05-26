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

/**
 * {@link Views#HOME} Controller class. Displays 'Home' view and the main static frame of the UI.
 * <br> This class could be considered the 'Mother' of the other {@link Controller}s. This is because it holds all the
 * other views inside of {@link #viewSwitch}.
 *<br> To access and interact with it refer to {@link #injectTab(Tab)} and {@link #switchTab(Tab)}.
 * @author Sergiu Chirap
 * @version final
 * @see Views
 * @see Controller
 * @see TabPane
 * @since 0.2
 */
class HomeController implements Controller, PropertyChangeListener {
    /**
     * This is the {@code Singleton} instance.
     */
    private static HomeController instance;
    /**
     * Class own logic manager.
     *
     * @see HomeModel
     */
    private final HomeModel model = HomeModel.get();
    /**
     * Class error styling for {@link TextField}s.
     */
    private final String STYLE_RED = "-fx-border-color: red; -fx-border-width: 2px";
    /**
     * The {@link Scene} of the class.
     */
    private Scene scene;
    /**
     * UI displays that follows all around the program.
     * <br> Accessed from the 'Quick Access' they are intended to ease move-around and core features of the application.
     * <br> Only one at a time should be visible.
     */
    @FXML
    private VBox profile, settings, goTo;
    /**
     * UI zone that will shift and be in charge of displaying different views.
     */
    @FXML
    private TabPane viewSwitch;
    /**
     * Displays pinned/favourite playlists by the user.
     *
     * @see com.playlistx.model.music.Playlist
     */
    @FXML
    private ScrollPane favouritesDisplay;
    /**
     * Text inputs related to user modification.
     */
    @FXML
    private TextField newName, oldPass, newPass, menuNewName, menuOldPass, menuNewPass;
    /**
     * Images used as buttons for choosing user modification type.
     */
    @FXML
    private ImageView nameAtn, passAtn;
    /**
     * Displays all available {@link CSS} and contains users chosen one.
     */
    @FXML
    private ChoiceBox<CSS> themeSelector;

    /**
     * Private constructor that is intended to run only once due being a {@code Singleton} class.
     */
    private HomeController() {
        model.addListener(this);
    }

    /**
     * {@code Singleton} getter. Gets the singleton instance or creates a new one if none exists.
     *
     * @return The singleton instance.
     */
    public static HomeController get() {
        if (instance == null) return instance = new HomeController();
        else return instance;
    }

    /**
     * Sets the {@link Scene} for this {@code Controller}.
     * <br> Not all {@code Controller}s needs a {@code scene}.
     *
     * @param scene The {@link Scene} of the class.
     */
    @Override
    public void init(@NotNull Scene scene) {
        this.scene = scene;
        themeSelector.setItems(FXCollections.observableArrayList(model.getCSS()));
    }

    /**
     * Gets their respective {@code FXML} location.
     *
     * @return A {@code String} which contains the location of the respective {@code Controller}
     */
    @Override
    public String getFXML() {
        return FXMLs.home;
    }

    /**
     * Gets their respective {@link Scene}.
     *
     * @return The {@link Scene} of the class.
     */
    @Override
    public Scene getScene() {
        return scene;
    }

    /**
     * Recalls the display of this view.
     */
    @FXML
    private void refresh() {
        Views.HOME.show();
    }

    /**
     * Toggles {@link #profile} visibility while hiding {@link #settings} and {@link #goTo} if needed.
     */
    @FXML
    private void toggleProfile() {
        if (!profile.isVisible()) cleanProfile();
        profile.setVisible(!profile.isVisible());
        settings.setVisible(false);
        goTo.setVisible(false);
    }

    /**
     * Toggles {@link #settings} visibility while hiding {@link #profile} and {@link #goTo} if needed.
     */
    @FXML
    private void toggleSettings() {
        settings.setVisible(!settings.isVisible());
        profile.setVisible(false);
        goTo.setVisible(false);
    }

    /**
     * Toggles {@link #goTo} visibility while hiding {@link #profile} and {@link #settings} if needed.
     */
    @FXML
    private void toggleGoTo() {
        goTo.setVisible(!goTo.isVisible());
        profile.setVisible(false);
        settings.setVisible(false);
    }

    /**
     * Toggles {@link #favouritesDisplay} visibility.
     */
    @FXML
    private void toggleFavorites() {
        favouritesDisplay.setVisible(!favouritesDisplay.isVisible());
    }

    /**
     * Changes current username to the new provided by the user.
     *
     * @param evt {@link #nameAtn} action event.
     */
    @FXML
    private void newUsername(@NotNull ActionEvent evt) {
        if (evt.getSource() == nameAtn)
            if (model.changeUsername(newName.getText(), oldPass.getText())) {
                oldPass.setStyle("");
                newName.setStyle("");
            } else {
                ViewHandler.popUp(ViewHandler.Notify.FILE, "Wrong Password or Invalid Username");
                oldPass.setStyle(STYLE_RED);
                newName.setStyle(STYLE_RED);
            }
        else {
            //noinspection DuplicatedCode
            if (model.changeUsername(menuNewName.getText(), menuOldPass.getText())) {
                menuOldPass.setStyle("");
                menuNewName.setStyle("");
            } else {
                ViewHandler.popUp(ViewHandler.Notify.FILE, "Wrong Password or Invalid Username");
                menuOldPass.setStyle(STYLE_RED);
                menuNewName.setStyle(STYLE_RED);
            }
        }
        cleanProfile();
    }

    /**
     * Changes current password to the new provided by the user.
     *
     * @param evt {@link #passAtn} action event.
     */
    @FXML
    private void newPassword(@NotNull ActionEvent evt) {
        if (evt.getSource() == passAtn)
            if (model.changePassword(oldPass.getText(), newPass.getText())) {
                oldPass.setStyle("");
                newPass.setStyle("");
            } else {
                ViewHandler.popUp(ViewHandler.Notify.FILE, "Wrong Password or Invalid Password!");
                oldPass.setStyle(STYLE_RED);
                newPass.setStyle(STYLE_RED);
            }
        else {
            //noinspection DuplicatedCode
            if (model.changePassword(menuOldPass.getText(), menuNewPass.getText())) {
                menuOldPass.setStyle("");
                menuNewPass.setStyle("");
            } else {
                ViewHandler.popUp(ViewHandler.Notify.FILE, "Wrong Password or Invalid Password!");
                menuOldPass.setStyle(STYLE_RED);
                menuNewPass.setStyle(STYLE_RED);
            }
        }
        cleanProfile();
    }

    /**
     * Private method for clearing all text inputs related to user modification.
     */
    private void cleanProfile() {
        newName.setText("");
        oldPass.setText("");
        newPass.setText("");
        menuNewName.setText("");
        menuOldPass.setText("");
        menuNewPass.setText("");
    }

    /**
     * Applies extracted {@link CSS} from {@link #themeSelector}.
     */
    @FXML
    private void confirmTheme() {
        model.setCSS(themeSelector.getSelectionModel().getSelectedItem());
        settings.setVisible(false);
    }

    /**
     * Calls to display {@link Views#HOME}
     */
    @FXML
    private void goToHome() {
        Views.HOME.show();
        goTo.setVisible(false);
    }

    /**
     * Calls to display {@link Views#SONGLIST}
     */
    @FXML
    private void goToAllSongs() {
        Views.SONGLIST.show();
        goTo.setVisible(false);
    }

    /**
     * Calls to display {@link Views#ALL_PLAYLISTS}
     */
    @FXML
    private void goToPlaylists() {
        Views.ALL_PLAYLISTS.show();
        goTo.setVisible(false);
    }

    /**
     * Closes the main {@link javafx.stage.Stage} thus exiting the application.
     */
    @FXML
    private void close() {
        model.close();
    }

    /**
     * UI functionality to add injected {@link Tab}s to {@link #viewSwitch}.
     *
     * @param tab Any tab containing loaded views with {@code FXML}.
     */
    public void injectTab(Tab tab) {
        viewSwitch.getTabs().add(tab);
    }

    /**
     * UI functionality to switch between {@link #viewSwitch} tabs.
     *
     * @param tab The tab you want to be displayed. Only works if it was previously injected through {@link #injectTab(Tab)}.
     */
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
    public void propertyChange(@NotNull PropertyChangeEvent evt) {
        if (evt.getPropertyName().equalsIgnoreCase("REFRESH")) refresh();
    }
}
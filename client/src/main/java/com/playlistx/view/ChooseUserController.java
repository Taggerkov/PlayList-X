package com.playlistx.view;

import com.playlistx.model.music.Playlist;
import com.playlistx.model.paths.FXMLs;
import com.playlistx.viewmodel.ChooseUserModel;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.controlsfx.control.textfield.TextFields;
import org.jetbrains.annotations.NotNull;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * {@link Controller} class for the 'Choose User' view.
 * Used when choosing with which user share any playlist.
 *
 * @author Sergiu Chirap
 * @version final
 * @see com.playlistx.view.Controller
 * @see TextFields#bindAutoCompletion(TextField, Object[])
 * @since 0.6
 */
class ChooseUserController implements Controller {
    /**
     * Class own logic manager.
     *
     * @see ChooseUserModel
     */
    private final ChooseUserModel model = ChooseUserModel.get();
    /**
     * This is the {@code Singleton} instance.
     */
    private static ChooseUserController instance;
    /**
     * The {@link Stage} of the class.
     */
    private Stage stage;
    /**
     * The {@link Scene} of the class.
     */
    private Scene scene;
    /**
     * The ID of the scoped {@link Playlist}.
     */
    private int playlistID;
    /**
     * An {@link ArrayList} of the available users.
     */
    private final ArrayList<String> users = new ArrayList<>();
    /**
     * Input, containing user's typing thus the chosen user.
     */
    @FXML
    private TextField chosenUser;

    /**
     * Class private constructor. Intended to run only once due to being a {@code Singleton}.
     *
     * @param playlistID The ID of the scoped {@link Playlist}.
     * @throws RemoteException   RMI connection error.
     * @throws NotBoundException RMI connection error.
     */
    private ChooseUserController(int playlistID) throws RemoteException, NotBoundException {
        this.playlistID = playlistID;
    }

    /**
     * {@code Singleton} getter. This method calls for the singleton instance or creates a new one.
     * <br> This while setting up the next scoped {@link Playlist}.
     *
     * @param playlistID The ID of the scoped {@link Playlist}.
     * @return the {@code Singleton} instance.
     * @throws RemoteException   RMI connection error.
     * @throws NotBoundException RMI connection error.
     */
    protected static ChooseUserController get(int playlistID) throws RemoteException, NotBoundException {
        if (instance == null) instance = new ChooseUserController(playlistID);
        instance.playlistID = playlistID;
        return instance;
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
        TextFields.bindAutoCompletion(chosenUser, model.getUsers(playlistID));
    }

    /**
     * Gets their respective {@code FXML} location.
     *
     * @return A {@code String} which contains the location of the respective {@code Controller}
     */
    @Override
    public String getFXML() {
        return FXMLs.chooseUser;
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

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Confirms choosing action and proceeds to adding the user extracted from the {@link #chosenUser}.
     */
    @FXML
    private void confirm() {
        model.addUser(chosenUser.getText());
        stage.close();
    }

    /**
     * Cancels choosing action and just closes this window.
     */
    @FXML
    private void cancel() {
        stage.close();
    }
}
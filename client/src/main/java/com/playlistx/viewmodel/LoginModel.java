package com.playlistx.viewmodel;

import com.playlistx.model.login.LoginException;
import com.playlistx.model.login.User;
import com.playlistx.model.music.Playlist;
import com.playlistx.model.paths.CSS;
import com.playlistx.view.ViewHandler;
import com.playlistx.view.ViewHandler.Notify;
import com.playlistx.view.Views;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.jetbrains.annotations.NotNull;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

/**
 * Logic Model for 'Login' view.
 * <br> This class is a {@code Singleton}.
 *
 * @author Sergiu Chirap
 * @version final
 * @since 0.7
 */
public class LoginModel implements PropertyChangeListener {
    /**
     * Class invalid styling for {@link TextField}s.
     */
    private static final String STYLE_RED = "-fx-border-color: red; -fx-border-width: 2px";
    /**
     * Class invalid styling for {@link TextField}s.
     */
    private static final String STYLE_GREEN = "-fx-border-color: green; -fx-border-width: 2px";
    /**
     * This is the {@code Singleton} instance.
     */
    private static LoginModel instance;
    /**
     * User model manager.
     */
    private final User user = User.get();
    /**
     * Observer Pattern trigger manager.
     */
    private final PropertyChangeSupport signal = new PropertyChangeSupport(this);

    /**
     * Class private constructor. Intended to run only once due to being a {@code Singleton}.
     *
     * @throws RemoteException   RMI connection error.
     * @throws NotBoundException RMI connection error.
     */
    private LoginModel() throws RemoteException, NotBoundException {
        CSS.addListener(this);
        user.addListener(this);
    }

    /**
     * {@code Singleton} getter. Gets the singleton instance or creates a new one if none exists.
     *
     * @return The singleton instance.
     */
    public static LoginModel get() {
        try {
            if (instance == null) instance = new LoginModel();
            return instance;
        } catch (RemoteException | NotBoundException e) {
            ViewHandler.popUp(Notify.ACCESS, "RMI Connection Error!");
            throw new RuntimeException("RMI Connection Error!");
        }
    }

    /**
     * Logs in with the provided credentials.
     * @param username A {@code String} which states the username.
     * @param password A {@code String} which states the password.
     */
    public void login(String username, String password) {
        try {
            if (user.login(username, password)) {
                signal.firePropertyChange("EXIT", null, null);
                Views.HOME_INIT.show();
            }
        } catch (LoginException e) {
            signal.firePropertyChange("LOGIN-USER", null, null);
        }
    }

    /**
     * Sings up with the provided credentials.
     * @param username A {@code String} which states the username.
     * @param password A {@code String} which states the password.
     */
    public void signUp(String username, String password) {
        user.signUp(username, password);
    }

    /**
     * Popup an {@link javafx.scene.control.Alert Alert}, if reconfirmed applications closes.
     * @param msg The message to be displayed to the user.
     */
    public void cancel(String msg) {
        if (ViewHandler.popUp(Notify.CONFIRM, msg)) System.exit(0);
    }

    /**
     * Generates an available username.
     * @return A {@code String} containing a generated available username.
     */
    public String genUser() {
        return User.genUsername();
    }

    /**
     * Popup an {@link javafx.scene.control.Alert Alert} with the provided message.
     * @param msg The message to be displayed to the user.
     */
    public void popUp(String msg) {
        ViewHandler.popUp(Notify.INPUT, msg);
    }

    /**
     * Adds text property listeners to 'Sign' side inputs.
     * @param signUser The {@link TextField} input of the username.
     * @param signPass The {@link TextField} input of the password.
     */
    public void addSignListeners(@NotNull TextField signUser, @NotNull PasswordField signPass) {
        signUser.textProperty().addListener((obs, oldText, newText) -> {
            if (signUser.getText().isBlank()) signUser.setStyle("");
            else {
                if (User.checkUsername(signUser.getText())) checkUserAvailability(signUser);
                else signUser.setStyle(STYLE_RED);
            }
        });
        signPass.textProperty().addListener((obs, oldText, newText) -> {
            if (signPass.getText().isBlank()) signPass.setStyle("");
            else checkPasswordRequirements(signPass);
        });
    }

    /**
     * Checks user availability and applies stiles.
     * @param signUser Username's input.
     */
    private void checkUserAvailability(@NotNull TextField signUser) {
        if (User.isAvailable(signUser.getText()))
            signUser.setStyle(STYLE_GREEN);
        else signUser.setStyle(STYLE_RED);
    }

    /**
     * Check password requirements and applies stiles.
     * @param signPass Password's input.
     */
    private void checkPasswordRequirements(@NotNull PasswordField signPass) {
        if (User.checkPassword(signPass.getText()))
            signPass.setStyle(STYLE_GREEN);
        else signPass.setStyle(STYLE_RED);
    }

    /**
     * Adds a {@link PropertyChangeListener} to this class {@link PropertyChangeSupport}.
     * @param listener The listener to be added to this class support.
     */
    public void addListener(PropertyChangeListener listener) {
        signal.addPropertyChangeListener(listener);
    }

    /**
     * This method gets called when a bound property is changed.
     *
     * @param evt A PropertyChangeEvent object describing the event source
     *            and the property that has changed.
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        signal.firePropertyChange(evt);
    }
}
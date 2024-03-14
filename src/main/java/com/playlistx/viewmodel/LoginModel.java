package com.playlistx.viewmodel;

import com.playlistx.model.login.User;
import com.playlistx.model.login.UserName;
import com.playlistx.model.utils.exceptions.InvalidInput;
import com.playlistx.view.ViewHandler;
import com.playlistx.view.ViewHandler.Notify;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.jetbrains.annotations.NotNull;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class LoginModel implements PropertyChangeListener {
    private static final String STYLE_RED = "-fx-border-color: red; -fx-border-width: 2px";
    private static final String STYLE_GREEN = "-fx-border-color: green; -fx-border-width: 2px";
    private static LoginModel instance;
    private final User user = new User(this);
    private final PropertyChangeSupport signal = new PropertyChangeSupport(this);

    private LoginModel() {
    }

    public static LoginModel get() {
        if (instance == null) instance = new LoginModel();
        return instance;
    }

    public void login(String userName, String password) {
        if (user.login(userName, password)) {
            signal.firePropertyChange("EXIT", null, null);
            ViewHandler.get().setUser(user);
            // ViewHandler.get().display(Views.SHELVE);
        }
    }

    public void signUp(String username, String password) {
        user.signUp(username, password);
    }

    public void cancel(String msg) {
        if (ViewHandler.get().popUp(Notify.CONFIRM, msg)) System.exit(0);
    }

    public String genUser() {
        return UserName.fresh(null).toString();
    }

    public void popUp(String msg) {
        ViewHandler.get().popUp(Notify.INPUT, msg);
    }

    public void addSignListeners(@NotNull TextField signUser, @NotNull PasswordField signPass) {
        signUser.textProperty().addListener((obs, oldText, newText) -> {
            if (signUser.getText().isBlank()) signUser.setStyle("");
            else {
                try {
                    UserName.fresh(signUser.getText());
                    checkUserAvailability(signUser);
                } catch (InvalidInput e) {
                    signUser.setStyle(STYLE_RED);
                }
            }
        });
        signPass.textProperty().addListener((obs, oldText, newText) -> {
            if (signPass.getText().isBlank()) signPass.setStyle("");
            else checkPasswordRequirements(signPass);
        });
    }

    private void checkUserAvailability(@NotNull TextField signUser) {
        if (user.isAvailable(signUser.getText()))
            signUser.setStyle(STYLE_GREEN);
        else signUser.setStyle(STYLE_RED);
    }

    private void checkPasswordRequirements(@NotNull PasswordField signPass) {
        if (user.checkPassword(signPass.getText()))
            signPass.setStyle(STYLE_GREEN);
        else signPass.setStyle(STYLE_RED);
    }

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
package com.playlistx.viewmodel;

import com.playlistx.model.login.*;
import com.playlistx.view.ViewHandler;
import com.playlistx.view.ViewHandler.*;
import com.playlistx.view.Views;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.jetbrains.annotations.NotNull;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class LoginModel implements PropertyChangeListener {
    private static final String STYLE_RED = "-fx-border-color: red; -fx-border-width: 2px";
    private static final String STYLE_GREEN = "-fx-border-color: green; -fx-border-width: 2px";
    private static LoginModel instance;
    private final User user = User.get();
    private final PropertyChangeSupport signal = new PropertyChangeSupport(this);

    private LoginModel() throws RemoteException, NotBoundException {
        user.addListener(this);
    }

    public static LoginModel get() throws RemoteException, NotBoundException {
        if (instance == null) instance = new LoginModel();
        return instance;
    }

    public void login(String userName, String password) {
        if (user.login(userName, password)) {
            signal.firePropertyChange("EXIT", null, null);
            try {
                try {
                    ViewHandler.get().display(Views.HOME);
                } catch (LoginException e) {
                    ViewHandler.get().popUp(Notify.INPUT, "This 'user' doesn't exist!");
                }
            } catch (RemoteException | NotBoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void signUp(String username, String password) {
        user.signUp(username, password);
    }

    public void cancel(String msg) {
        try {
            if (ViewHandler.get().popUp(Notify.CONFIRM, msg)) System.exit(0);
        } catch (RemoteException | NotBoundException e) {
            System.exit(0);
        }
    }

    public String genUser() {
        return User.genUsername();
    }

    public void popUp(String msg) {
        try {
            ViewHandler.get().popUp(Notify.INPUT, msg);
        } catch (RemoteException | NotBoundException e) {
            throw new RuntimeException(e);
        }
    }

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

    private void checkUserAvailability(@NotNull TextField signUser) {
        if (User.isAvailable(signUser.getText()))
            signUser.setStyle(STYLE_GREEN);
        else signUser.setStyle(STYLE_RED);
    }

    private void checkPasswordRequirements(@NotNull PasswordField signPass) {
        if (User.checkPassword(signPass.getText()))
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
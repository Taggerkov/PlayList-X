package com.playlistx.viewmodel;

import com.playlistx.model.login.User;
import com.playlistx.model.login.UserName;
import com.playlistx.model.utils.exceptions.InvalidInput;
import com.playlistx.view.ViewHandler;
import com.playlistx.view.ViewHandler.PopUp;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class LoginModel implements PropertyChangeListener {
    private static LoginModel instance;
    private final User user = new User(this);
    private final PropertyChangeSupport signal = new PropertyChangeSupport(this);
    private LoginModel(){
    }

    public static LoginModel get() {
        if (instance == null) instance = new LoginModel();
        return instance;
    }

    public void login(String userName, int hashPassword) {
        if (user.login(userName, hashPassword)) {
            signal.firePropertyChange("EXIT", null, null);
            ViewHandler.get().setUser(user);
            // ViewHandler.get().display(Views.SHELVE);
        }
    }

    public void signUp(String username, int hashPassword) {
        user.signUp(username, hashPassword);
    }

    public void cancel() {
        if (ViewHandler.get().popUp(PopUp.CONFIRM, "Are you sure you want to exit the program?")) System.exit(0);
    }

    public String genUser() {
        return UserName.fresh(null).toString();
    }

    public void addSignUserListener(@NotNull TextField signUser) {
        signUser.textProperty().addListener((obs, oldText, newText) -> {
            if (signUser.getText().isBlank()) signUser.setStyle("");
            else {
                checkUserAvailability(signUser);
                try {
                    UserName.fresh(signUser.getText());
                } catch (InvalidInput e) {
                    signUser.setStyle("-fx-border-color: red; -fx-border-width: 2px");
                }
            }
        });
    }

    public void checkUserAvailability(@NotNull TextField signUser) {
        if (user.isAvailable(signUser.getText()))
            signUser.setStyle("-fx-border-color: green; -fx-border-width: 2px");
        else signUser.setStyle("-fx-border-color: red; -fx-border-width: 2px");
    }

    public void cleanUp(@Nullable TextField user, @Nullable PasswordField password) {
        if (user != null) {
            user.setText("");
            user.setStyle("");
        }
        if (password != null) {
            password.setText("");
            password.setStyle("");
        }
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
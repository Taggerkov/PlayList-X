package com.playlistx.view;

import com.playlistx.model.paths.FXMLs;
import com.playlistx.viewmodel.LoginModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import org.controlsfx.control.Notifications;
import org.jetbrains.annotations.NotNull;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * {@link Views#LOGIN} Controller class. Displays 'Login' views and handles user sing-up and login actions.
 *
 * @author Sergiu Chirap
 * @version final
 * @since 0.1
 */
class LoginController implements Controller, PropertyChangeListener {
    /**
     * Class user error message.
     */
    private static final String ERROR_USER = "User doesn't exist!";
    /**
     * Class password error message.
     */
    private static final String ERROR_PASSWORD = "Password doesn't match!";
    /**
     * Class user-length error message.
     */
    private static final String ERROR_LENGTH = "Username should be between 2 and 40 characters!";
    /**
     * Class password-requirements error message.
     */
    private static final String ERROR_COMPLEXITY = "Password must contain at least one 'Digit', 'Upper' & 'Lower' case and a 'Special Character'.";
    /**
     * Class cancel-confirm message.
     */
    private static final String CONFIRM_CANCEL = "Are you sure you want to exit the program?";
    /**
     * Class error styling for {@link TextField}s.
     */
    private static final String STYLE_RED = "-fx-border-color: red; -fx-border-width: 2px";
    /**
     * Class own logic manager
     *
     * @see LoginModel
     */
    private final LoginModel model = LoginModel.get();
    /**
     * This is the {@code Singleton} instance.
     */
    private static LoginController instance;
    /**
     * The {@link Scene} of the class.
     */
    private Scene scene;
    /**
     * Class in charge of the switch of the 'Login' and 'SignUp' sides of this view.
     */
    @FXML
    private TabPane tabPane;
    /**
     * The 'Login' or 'SignUp' side of this view.
     */
    @FXML
    private Tab tabLogin, tabSign;
    /**
     * Username text inputs.
     */
    @FXML
    private TextField loginUser, signUser;
    /**
     * Password hidden-text inputs.
     */
    @FXML
    private PasswordField loginPass, signPass;
    /**
     * Confirmation buttons.
     */
    @FXML
    private Button toSign, toLogin;

    /**
     * Private constructor that is intended to run only once due being a {@code Singleton} class.
     */
    private LoginController() {
    }

    /**
     * {@code Singleton} getter. Gets the singleton instance or creates a new one if none exists.
     *
     * @return The singleton instance.
     */
    protected static LoginController get() {
        if (instance == null) instance = new LoginController();
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
        model.addListener(this);
        model.addSignListeners(signUser, signPass);
    }

    /**
     * Gets their respective {@code FXML} location.
     *
     * @return A {@code String} which contains the location of the respective {@code Controller}
     */
    @Override
    public String getFXML() {
        return FXMLs.login;
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
     * Proceeds to log in with the provided input on {@link #loginUser} and {@link #loginPass}.
     */
    @FXML
    private void login() {
        model.login(loginUser.getText(), loginPass.getText());
    }

    /**
     * Proceeds to sign up with the provided input on {@link #signUser} and {@link #signPass}.
     */
    @FXML
    private void signup() {
        model.signUp(signUser.getText(), signPass.getText());
    }

    /**
     * Popup a confirmation {@link Alert} and if reaffirmed closes the main {@link javafx.stage.Stage} thus exiting the application.
     */
    @FXML
    private void cancel() {
        model.cancel(CONFIRM_CANCEL);
    }

    /**
     * Generates an available username through our username generator.
     */
    @FXML
    private void genUser() {
        signUser.setText(model.genUser());
    }

    /**
     * Switches between {@link #tabLogin} and {@link #tabSign} sides.
     * @param event {@link #toSign} or {@link #toLogin} {@link ActionEvent}.
     */
    @FXML
    private void jumpTo(@NotNull ActionEvent event) {
        if (event.getSource() == toSign) tabPane.getSelectionModel().select(tabSign);
        else if (event.getSource() == toLogin) tabPane.getSelectionModel().select(tabLogin);
    }

    /**
     * Clears out the hidden side text inputs.
     */
    @FXML
    private void cleanUp() {
        if (tabSign != null && tabLogin != null) if (tabSign.isSelected()) {
            loginUser.setText("");
            loginPass.setText("");
        } else {
            signUser.setText("");
            signPass.setText("");
        }
    }

    /**
     * This method gets called when a bound property is changed.
     *
     * @param evt A PropertyChangeEvent object describing the event source
     *            and the property that has changed.
     */
    @Override
    public void propertyChange(@NotNull PropertyChangeEvent evt) {
        switch (evt.getPropertyName()) {
            case "LOGIN-USER" -> {
                cleanUp();
                loginUser.setStyle(STYLE_RED);
                model.popUp(ERROR_USER);
            }
            case "LOGIN-PASSWORD" -> {
                cleanUp();
                loginPass.setStyle(STYLE_RED);
                model.popUp(ERROR_PASSWORD);
            }
            case "SIGNUP-USER" -> {
                signUser.setStyle(STYLE_RED);
                model.popUp(ERROR_LENGTH);
            }
            case "SIGNUP-PASSWORD" -> {
                signPass.setStyle(STYLE_RED);
                model.popUp(ERROR_COMPLEXITY);
            }
            case "SIGNUP" -> {
                tabPane.getSelectionModel().select(tabLogin);
            }
            case "EXIT" -> {
                cleanUp();
            }
        }
    }
}
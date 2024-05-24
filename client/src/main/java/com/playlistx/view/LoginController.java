package com.playlistx.view;

import com.playlistx.model.paths.FXMLs;
import com.playlistx.viewmodel.LoginModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import org.jetbrains.annotations.NotNull;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

class LoginController implements Controller, PropertyChangeListener {
    private static final String ERROR_USER = "User doesn't exist!";
    private static final String ERROR_PASSWORD = "Password doesn't match!";
    private static final String ERROR_LENGTH = "Username should be between 2 and 40 characters!";
    private static final String ERROR_COMPLEXITY = "Password must contain at least one 'Digit', 'Upper' & 'Lower' case and a 'Special Character'.";
    private static final String CONFIRM_CANCEL = "Are you sure you want to exit the program?";
    private static final String STYLE_RED = "-fx-border-color: red; -fx-border-width: 2px";
    private final LoginModel model = LoginModel.get();
    private static LoginController instance;
    private Scene scene;
    @FXML
    private TabPane tabPane;
    @FXML
    private Tab tabLogin, tabSign;
    @FXML
    private TextField loginUser, signUser;
    @FXML
    private PasswordField loginPass, signPass;
    @FXML
    private Button toSign, toLogin;
    @FXML
    private ImageView loginLogo, signLogo;

    private LoginController() {
    }

    protected static LoginController get() {
        if (instance == null) instance = new LoginController();
        return instance;
    }

    @Override
    public void init(@NotNull Scene scene) {
        this.scene = scene;
        model.addListener(this);
        model.addSignListeners(signUser, signPass);
    }

    @Override
    public String getFXML() {
        return FXMLs.login;
    }

    @Override
    public Scene getScene() {
        return scene;
    }

    @FXML
    private void login() {
        model.login(loginUser.getText(), loginPass.getText());
    }

    @FXML
    private void signup() {
        model.signUp(signUser.getText(), signPass.getText());
    }

    @FXML
    private void cancel() {
        model.cancel(CONFIRM_CANCEL);
    }

    @FXML
    private void genUser() {
        signUser.setText(model.genUser());
    }

    @FXML
    private void jumpTo(@NotNull ActionEvent event) {
        if (event.getSource() == toSign) tabPane.getSelectionModel().select(tabSign);
        else if (event.getSource() == toLogin) tabPane.getSelectionModel().select(tabLogin);
    }

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
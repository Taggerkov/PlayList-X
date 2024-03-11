package com.playlistx.view;

import com.playlistx.view.ViewHandler.PopUp;
import com.playlistx.viewmodel.LoginModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import org.jetbrains.annotations.NotNull;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class LoginController implements PropertyChangeListener {
    private static final LoginModel model = LoginModel.get();
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
    private LoginController(){
    }

    public static LoginController get() {
        if (instance == null) instance = new LoginController();
        return instance;
    }

    public void init(@NotNull Scene scene) {
        this.scene = scene;
        model.addListener(this);
        model.addSignUserListener(signUser);
    }

    public Scene getScene() {
        return scene;
    }

    @FXML
    private void login() {
        model.login(loginUser.getText(), loginPass.getText().hashCode());
    }

    @FXML
    private void signup() {
        model.signUp(signUser.getText(), signPass.getText().hashCode());
    }

    @FXML
    private void cancel() {
        model.cancel();
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
            model.cleanUp(loginUser, loginPass);
        } else {
            model.cleanUp(signUser, signPass);
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
                model.cleanUp(loginUser, loginPass);
                loginUser.setStyle("-fx-border-color: red; -fx-border-width: 2px");
                ViewHandler.get().popUp(PopUp.INPUT, "User doesn't exist!");
            }
            case "LOGIN-PASSWORD" -> {
                model.cleanUp(null, loginPass);
                loginPass.setStyle("-fx-border-color: red; -fx-border-width: 2px");
                ViewHandler.get().popUp(PopUp.INPUT, "Password doesn't match");
            }
            case "SIGNUP-USER" -> {
                signUser.setStyle("-fx-border-color: red; -fx-border-width: 2px");
                ViewHandler.get().popUp(PopUp.INPUT, "Username should be between 2 and 40 characters!");
            }
            case "SIGNUP" -> {
                tabPane.getSelectionModel().select(tabLogin);
            }
            case "EXIT" -> {
                model.cleanUp(loginUser, loginPass);
            }
        }
    }
}

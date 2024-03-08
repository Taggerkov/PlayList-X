package com.playlistx.view;

import com.playlistx.model.login.User;
import com.playlistx.model.paths.CSS;
import com.playlistx.model.paths.FXMLs;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;


public class ViewHandler {
    public static final Stage WINDOW = new Stage();
    private static ViewHandler instance;
    private final LoginController login = LoginController.get();
    private User user;
    private Alert alert;

    private ViewHandler() {
        loadLogin();
        display(Views.LOGIN);
        WINDOW.setResizable(false);
    }

    public synchronized static ViewHandler get() {
        if (instance == null) instance = new ViewHandler();
        return instance;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void display(@NotNull Views view) {
        Scene scene = null;
        String title = "PlayList X: ";

        switch (view) {
            case LOGIN -> {
                scene = login.getScene();
                WINDOW.setOnCloseRequest(null);
                title += "Login";
            }
            /*case SHELVE -> {
                shelve.setUser(user);
                scene = shelve.getScene();
                WINDOW.setOnCloseRequest(event -> {
                    user.logout();
                    System.exit(0);
                });
                title += "Shelve";
            }*/
            default -> popUp(PopUp.ACCESS, "This 'View' doesn't exist or is not available!");
        }

        scene.getStylesheets().add(String.valueOf((getClass().getResource(CSS.getCSS().get()))));
        WINDOW.getIcons().add(new Image(CSS.getLogo()));
        WINDOW.setScene(scene);
        WINDOW.setTitle(title);
        WINDOW.show();
    }

    private void loadLogin() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setController(login);
            loader.setLocation(getClass().getResource(FXMLs.loginPath));
            login.init(new Scene(loader.load()));
        } catch (IOException e) {
            popUp(PopUp.ACCESS, "Failed loading start-up window!");
        }
    }

    public boolean popUp(@NotNull PopUp type, String msg) {
        switch (type) {
            case CONFIRM -> {
                Alert alert = new Alert(Alert.AlertType.WARNING, msg, ButtonType.YES, ButtonType.NO);
                alert.setTitle("CONFIRM ACTION?");
                alert.setHeaderText(null);
                Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                stage.getIcons().add(new Image(CSS.getLogo()));
                alert.showAndWait();
                if (alert.getResult() == ButtonType.YES) {
                    alert.close();
                    return true;
                }
            }
            case ACCESS -> {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, msg, ButtonType.CLOSE);
                alert.setTitle("ACTION CANNOT BE DONE: INVALID ACCESS");
                alert.setHeaderText(null);
                Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                stage.getIcons().add(new Image(CSS.getLogo()));
                alert.showAndWait();
                if (alert.getResult() == ButtonType.CLOSE) {
                    alert.close();
                }
            }
            case FILE -> {
                Alert alert = new Alert(Alert.AlertType.ERROR, msg, ButtonType.CLOSE);
                alert.setTitle("ACTION CANNOT BE DONE: INVALID FILE");
                alert.setHeaderText(null);
                Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                stage.getIcons().add(new Image(CSS.getLogo()));
                alert.showAndWait();
                if (alert.getResult() == ButtonType.CLOSE) {
                    alert.close();
                }
            }
            case INPUT -> {
                Alert alert = new Alert(Alert.AlertType.ERROR, msg, ButtonType.CLOSE);
                alert.setTitle("ACTION CANNOT BE DONE: INVALID INPUT");
                alert.setHeaderText(null);
                Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                stage.getIcons().add(new Image(CSS.getLogo()));
                alert.showAndWait();
                if (alert.getResult() == ButtonType.CLOSE) {
                    alert.close();
                }
            }
        }
        return false;
    }

    public enum Views {
        LOGIN;
    }

    public enum PopUp {
        CONFIRM, ACCESS, FILE, INPUT;
    }
}
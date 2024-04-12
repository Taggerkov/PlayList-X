package com.playlistx.view;

import com.playlistx.model.login.User;
import com.playlistx.model.paths.CSS;
import com.playlistx.model.paths.FXMLs;
import com.playlistx.view.ChooseUserController.ChoiceType;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;


public class ViewHandler {
    public static final Stage WINDOW = new Stage();
    private static ViewHandler instance;
    private final LoginController login = LoginController.get();
    private ChooseUserController chooseUser = ChooseUserController.get(ChoiceType.ADD, 0);
    private final User user = User.get();

    private ViewHandler() throws RemoteException, NotBoundException {
        loadLogin();
        loadChooseUser();
        display(Views.LOGIN);
        WINDOW.setResizable(false);
    }

    public static ViewHandler get() throws RemoteException, NotBoundException {
        if (instance == null) instance = new ViewHandler();
        return instance;
    }

    public void setTitle(String title) {
        WINDOW.setTitle("PlayListX: " + title);
    }

    public void display(@NotNull Views view) {
        Scene scene = null;

        switch (view) {
            case LOGIN -> {
                scene = login.getScene();
                WINDOW.setOnCloseRequest(event -> System.exit(0));
                setTitle("Login");
            }
            /*case CHAT -> {
                scene = login.getScene();
                WINDOW.setOnCloseRequest(event -> {
                    user.logout();
                    System.exit(0);
                });
                setTitle("Chat");
            }*/
            default -> popUp(Notify.ACCESS, "This 'View' doesn't exist or is not available!");
        }

        assert scene != null;
        scene.getStylesheets().add(String.valueOf((getClass().getResource(CSS.getCSS().get()))));
        WINDOW.getIcons().add(new Image(CSS.getLogo()));
        WINDOW.setScene(scene);
        WINDOW.show();
        WINDOW.toFront();
    }

    public void showChooseUser(ChooseUserController.ChoiceType type, int chatID) throws IOException, NotBoundException {
        chooseUser = ChooseUserController.get(type, chatID);
        Stage chooseStage = new Stage();
        chooseStage.setTitle("PlayListX: Choose User");
        chooseStage.getIcons().add(new Image(CSS.getLogo()));
        chooseStage.initOwner(WINDOW);
        Scene scene = chooseUser.getScene();
        scene.getStylesheets().add(String.valueOf((getClass().getResource(CSS.getCSS().get()))));
        scene.setFill(Color.TRANSPARENT);
        chooseStage.setScene(scene);
        chooseStage.initModality(Modality.APPLICATION_MODAL);
        chooseStage.initStyle(StageStyle.TRANSPARENT);
        chooseUser.setStage(chooseStage);
        chooseStage.show();
    }

    private void loadLogin() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setController(login);
            loader.setLocation(getClass().getResource(FXMLs.loginPath));
            login.init(new Scene(loader.load()));
        } catch (IOException e) {
            e.printStackTrace();
            popUp(Notify.ACCESS, "Failed loading start-up window!");
        }
    }

    private void loadChooseUser() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setController(chooseUser);
            loader.setLocation(getClass().getResource(FXMLs.chooseUserPath));
            chooseUser.init(new Scene(loader.load()));
        } catch (IOException e) {
            e.printStackTrace();
            popUp(Notify.ACCESS, "Failed loading choose user window!");
        }
    }

    public boolean popUp(@NotNull ViewHandler.Notify type, String msg) {
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
        LOGIN, CHAT;
    }

    public enum Notify {
        CONFIRM, ACCESS, FILE, INPUT;
    }
}
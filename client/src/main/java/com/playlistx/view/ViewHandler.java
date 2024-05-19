package com.playlistx.view;

import com.playlistx.model.login.User;
import com.playlistx.model.paths.CSS;
import com.playlistx.model.paths.FXMLs;
import com.playlistx.view.ChooseUserController.ChoiceType;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Tab;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.HashMap;


public class ViewHandler {
    public static final Stage WINDOW = new Stage();
    private static ViewHandler instance;
    private final User user = User.get();
    private final Controller[] controllers = {LoginController.get(), HomeController.get(), SongListController.get()};
    private final HashMap<Controller, Tab> tabs = new HashMap<>();
    private int selectPlaylistID;

    private ViewHandler() throws NotBoundException, RemoteException {
        loadAllControllers();
        display(Views.LOGIN);
        WINDOW.setResizable(true);
        WINDOW.widthProperty().addListener((o, oldValue, newValue)->{
            if(newValue.intValue() < 670) {
                WINDOW.setResizable(false);
                WINDOW.setWidth(670);
                WINDOW.setResizable(true);
            }
        });
        WINDOW.heightProperty().addListener((o, oldValue, newValue)->{
            if(newValue.intValue() < 430) {
                WINDOW.setResizable(false);
                WINDOW.setWidth(430);
                WINDOW.setResizable(true);
            }
        });
    }

    public static @Nullable ViewHandler get() {
        try {
            if (instance == null) instance = new ViewHandler();
            return instance;
        } catch (RemoteException | NotBoundException e) {
            popUp(Notify.ACCESS, "RMI Connection Error!");
            return null;
        }
    }

    public static boolean popUp(@NotNull Notify type, String msg) {
        Alert alert = null;
        switch (type) {
            case CONFIRM -> {
                alert = new Alert(Alert.AlertType.WARNING, msg, ButtonType.YES, ButtonType.NO);
                alert.setTitle("CONFIRM ACTION?");
            }
            case ACCESS -> {
                alert = new Alert(Alert.AlertType.INFORMATION, msg, ButtonType.CLOSE);
                alert.setTitle("ACTION CANNOT BE DONE: INVALID ACCESS");
            }
            case FILE -> {
                alert = new Alert(Alert.AlertType.ERROR, msg, ButtonType.CLOSE);
                alert.setTitle("ACTION CANNOT BE DONE: INVALID FILE");
            }
            case INPUT -> {
                alert = new Alert(Alert.AlertType.ERROR, msg, ButtonType.CLOSE);
                alert.setTitle("ACTION CANNOT BE DONE: INVALID INPUT");
            }
        }
        alert.setHeaderText(null);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(CSS.logo()));
        alert.showAndWait();

        if (alert.getResult() == ButtonType.YES) {
            alert.close();
            return true;
        }
        return false;
    }

    public void setTitle(String title) {
        WINDOW.setTitle("PlayListX: " + title);
    }

    public void selectPlaylist(int playlistID) {
        selectPlaylistID = playlistID;
    }

    public void display(@NotNull Views view) {
        Scene scene = null;

        switch (view) {
            case LOGIN -> {
                Controller login = LoginController.get();
                scene = login.getScene();
                WINDOW.setOnCloseRequest(event -> System.exit(0));
                setTitle("Login");
            }
            case HOME -> {
                Controller home = HomeController.get();
                scene = home.getScene();
                WINDOW.setOnCloseRequest(event -> {
                    user.logout();
                    System.exit(0);
                });
                setTitle("Home");
            }
            case SONGLIST -> {
                HomeController.get().switchTab(tabs.get(SongListController.get()));
                SongListController.get().isSelect(false, 0);
                setTitle("SongList");
            }
            case SONGLIST_SELECT -> {
                HomeController.get().switchTab(tabs.get(SongListController.get()));
                SongListController.get().isSelect(true, selectPlaylistID);
                setTitle("SongList");
            }
            case ALL_PLAYLISTS -> {

            }
            case PLAYLIST -> {

            }
            default -> popUp(Notify.ACCESS, "This 'View' doesn't exist or is not available!");
        }

        assert scene != null;
        scene.getStylesheets().add(String.valueOf((getClass().getResource(CSS.path()))));
        WINDOW.getIcons().add(new Image(CSS.logo()));
        WINDOW.setScene(scene);
        WINDOW.show();
        WINDOW.toFront();
    }

    public void showChooseUser(ChoiceType type, int chatID) throws IOException, NotBoundException {
        Controller chooseUser = ChooseUserController.get(type, chatID);
        Stage chooseStage = new Stage();
        chooseStage.setTitle("PlayListX: Choose User");
        chooseStage.getIcons().add(new Image(CSS.logo()));
        chooseStage.initOwner(WINDOW);
        Scene scene = chooseUser.getScene();
        scene.getStylesheets().add(String.valueOf((getClass().getResource(CSS.path()))));
        scene.setFill(Color.TRANSPARENT);
        chooseStage.setScene(scene);
        chooseStage.initModality(Modality.APPLICATION_MODAL);
        chooseStage.initStyle(StageStyle.TRANSPARENT);
        chooseStage.show();
    }

    private void loadController(@NotNull Controller controller) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setController(controller);
            loader.setLocation(getClass().getResource(controller.getFXML()));
            if (controller != HomeController.get() & controller != LoginController.get()) {

                Tab tab = loader.load();
                HomeController.get().injectTab(tab);
                tabs.put(controller, tab);
            } else {
                controller.init(new Scene(loader.load()));
            }
        } catch (IOException | IllegalStateException e) {
            e.printStackTrace();
            popUp(Notify.ACCESS, "Failed loading " + controller.getClass().getName() + " FXML!");
            System.exit(0);
        }
    }

    private void loadAllControllers() {
        for (Controller controller : controllers) loadController(controller);
    }

    public @Nullable HBox loadSongItems() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource(FXMLs.songItem));
            loader.setController(SongListController.get());
            return loader.load();
        } catch (IOException e) {
            popUp(Notify.ACCESS, "Failed loading song list resources!");
        }
        return null;
    }

    public enum Notify {
        CONFIRM, ACCESS, FILE, INPUT;
    }
}
package com.playlistx.view;

import com.playlistx.model.ModelManager;
import com.playlistx.model.login.User;
import com.playlistx.model.paths.CSS;
import com.playlistx.model.paths.FXMLs;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Tab;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.HashMap;

import static javafx.application.Application.setUserAgentStylesheet;


public class ViewHandler {
    public static final Stage WINDOW = new Stage();
    private static ViewHandler instance;
    private final User user = User.get();
    private final Controller[] controllers = {LoginController.get(), HomeController.get(), SongListController.get(), PlayListsController.get(), ThePlayListController.get()};
    private final HashMap<Controller, Tab> tabs = new HashMap<>();
    private int selectPlaylistID;

    private ViewHandler() throws NotBoundException, RemoteException {
        loadAllControllers();
        display(Views.LOGIN);
        WINDOW.setMinWidth(680);
        WINDOW.setMinHeight(490);
    }

    public static @NotNull ViewHandler get() {
        try {
            if (instance == null) instance = new ViewHandler();
        } catch (RemoteException | NotBoundException e) {
            popUp(Notify.ACCESS, "RMI Connection Error!");
        }
        return instance;
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
                WINDOW.setResizable(false);
                scene = LoginController.get().getScene();
                WINDOW.setOnCloseRequest(event -> System.exit(0));
                setTitle("Login");
            }
            case HOME_INIT -> {
                WINDOW.setResizable(true);
                WINDOW.setWidth(1200);
                WINDOW.setHeight(700);
                scene = HomeController.get().getScene();
                WINDOW.setOnCloseRequest(event -> {
                    user.logout();
                    System.exit(0);
                });
                setTitle("Home");
            }
            case HOME -> {
                scene = HomeController.get().getScene();
                HomeController.get().switchTab(null);
                setTitle("Home");
            }
            case SONGLIST -> {
                scene = SongListController.get().getScene();
                HomeController.get().switchTab(tabs.get(SongListController.get()));
                SongListController.get().isSelect(false, 0);
                setTitle("SongList");
            }
            case SONGLIST_SELECT -> {
                scene = SongListController.get().getScene();
                HomeController.get().switchTab(tabs.get(SongListController.get()));
                SongListController.get().isSelect(true, selectPlaylistID);
                setTitle("SongList");
            }
            case ALL_PLAYLISTS -> {
                scene = PlayListsController.get().getScene();
                PlayListsController.get().refresh();
                HomeController.get().switchTab(tabs.get(PlayListsController.get()));
                setTitle("PlayLists");
            }
            case PLAYLIST -> {
                scene = ThePlayListController.get().getScene();
                ThePlayListController.get().setPlayList(selectPlaylistID);
                ThePlayListController.get().refresh();
                HomeController.get().switchTab(tabs.get(ThePlayListController.get()));
                try {
                    setTitle(ModelManager.get().getPlaylist(selectPlaylistID).getTitle());
                } catch (RemoteException | NotBoundException e) {
                    popUp(Notify.ACCESS, "RMI Connection Error!");
                    throw new RuntimeException(e);
                }
            }
            default -> popUp(Notify.ACCESS, "This 'View' doesn't exist or is not available!");
        }
        if (view == Views.LOGIN || view == Views.HOME_INIT) {
            scene.getStylesheets().add(String.valueOf((getClass().getResource(CSS.path()))));
            WINDOW.getIcons().add(new Image(CSS.logo()));
            WINDOW.setScene(scene);
            WINDOW.show();
            WINDOW.toFront();
        }
    }

    public void showChooseUser(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(FXMLs.chooseUser));
            ChooseUserController chooseUser = ChooseUserController.get(selectPlaylistID);
            loader.setController(chooseUser);
            chooseUser.init(new Scene(loader.load()));
            Stage chooseStage = new Stage();
            chooseUser.setStage(chooseStage);
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
        } catch (RemoteException | NotBoundException e) {
            ViewHandler.popUp(ViewHandler.Notify.ACCESS, "RMI Connection Error!");
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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

    protected @Nullable HBox loadSongItems(Controller controller) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource(FXMLs.songItem));
            loader.setController(controller);
            return loader.load();
        } catch (IOException e) {
            popUp(Notify.ACCESS, "Failed loading song list resources!");
        }
        return null;
    }

    protected @Nullable HBox loadPlaylistItems() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource(FXMLs.playlistItem));
            loader.setController(PlayListsController.get());
            return loader.load();
        } catch (IOException e) {
            popUp(Notify.ACCESS, "Failed loading song list resources!");
        }
        return null;
    }

    public void reloadCSS() {
        Scene scene = WINDOW.getScene();
        scene.getStylesheets().clear();
        setUserAgentStylesheet(null);
        scene.getStylesheets().add(String.valueOf((getClass().getResource(CSS.path()))));
    }

    public void playVideoYT(String linkYT) {
        Stage videoStage = new Stage();
        videoStage.setTitle("PlayVideoYT");
        videoStage.getIcons().add(new Image(CSS.logo()));
        videoStage.initOwner(WINDOW);
        WebView webview = new WebView();
        try {
            webview.getEngine().load(linkYT);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        webview.setPrefSize(640, 390);
        videoStage.setScene(new Scene(webview));
        videoStage.initModality(Modality.WINDOW_MODAL);
        videoStage.show();
    }

    public enum Notify {
        CONFIRM, ACCESS, FILE, INPUT;
    }
}
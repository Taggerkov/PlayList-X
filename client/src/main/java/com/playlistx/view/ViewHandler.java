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
import javafx.stage.Window;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.HashMap;

import static javafx.application.Application.setUserAgentStylesheet;

/**
 * {@code ViewHandler} is the manager and handler of client's UI.
 * It's responsible for the correct loading of all {@link Controller}s and their respective {@code FXML} files.
 * <br>
 * All requests will be processed by its {@code singleton} instance.
 *
 * @author Sergiu Chirap
 * @version 3.4
 * @see Controller
 * @see FXMLLoader
 * @since 0.1
 */
public class ViewHandler {
    /**
     * UI main {@link Stage}. Most of the runtime will be displayed through the {@code WINDOW} {@link Stage}.
     *
     * @see Stage
     */
    public static final Stage WINDOW = new Stage();
    /**
     * Class {@code singleton} instance.
     */
    private static ViewHandler instance;
    /**
     * Main user handle class.
     *
     * @see User
     */
    private final User user = User.get();
    /**
     * An array containing all {@link Controller}s to be loaded.
     *
     * @see Controller
     */
    private final Controller[] controllers = {LoginController.get(), HomeController.get(), SongListController.get(), PlayListsController.get(), ThePlayListController.get()};
    /**
     * {@link HashMap} containing the relation between the respective {@link Controller} and their {@link Tab}.
     *
     * @see HashMap
     * @see Controller
     * @see Tab
     * @see javafx.scene.control.TabPane
     */
    private final HashMap<Controller, Tab> tabs = new HashMap<>();
    /**
     * ID keeping track of the {@link com.playlistx.model.music.Playlist} in scope.
     *
     * @see com.playlistx.model.music.Playlist
     */
    private int selectPlaylistID;

    /**
     * {@code Singleton} constructor. Initializes and displays the UI.
     *
     * @throws NotBoundException if and RMI Connection Error occurred.
     * @throws RemoteException   if and RMI Connection Error occurred.
     */
    private ViewHandler() throws NotBoundException, RemoteException {
        loadAllControllers();
        display(Views.LOGIN);
        WINDOW.setMinWidth(680);
        WINDOW.setMinHeight(490);
    }

    /**
     * Class {@code singleton} getter. Creates new if called for the first time
     * and {@code instance} if later.
     * <br> Displays error {@link Alert} on RMI error!
     *
     * @return the {@code singleton} instance of this class.
     */
    public static @NotNull ViewHandler get() {
        try {
            if (instance == null) instance = new ViewHandler();
        } catch (RemoteException | NotBoundException e) {
            popUp(Notify.ACCESS, "RMI Connection Error!");
            throw new RuntimeException(e);
        }
        return instance;
    }

    /**
     * UI popup system. Displays an {@link Alert} based on the {@link Notify} type and the {@code string} provided.
     *
     * @param type A {@code Notify} {@link Enum} that stated the preset of the popup.
     * @param msg  The {@code string} message to be displayed.
     * @return A {@code boolean} in case of any selection preset which states user's choice.
     * @see Notify
     */
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

    /**
     * {@code WINDOWS} title setter. It keeps a preset and then adds the provided {@code title}.
     *
     * @param title A {@code string} that will be added to the title of the {@link Stage}.
     * @see Stage
     */
    public void setTitle(@Nullable String title) {
        WINDOW.setTitle("PlayListX: " + title);
    }

    /**
     * Setter for the {@code playlistID} value.
     *
     * @param playlistID The ID as an int of the {@link com.playlistx.model.music.Playlist} to be scoped.
     */
    public void selectPlaylist(int playlistID) {
        selectPlaylistID = playlistID;
    }

    /**
     * UI display request processor. This method will switch to the desired view.
     * <br> Same request could also be sent through {@link Views}.
     *
     * @param view An {@link Enum} that states the desired view.
     * @see Views
     */
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

    /**
     * Special request to display the 'ChooseUser' view.
     * <br> A new {@link Stage} will be shown and will lock {@code WINDOW}.
     *
     * @see Stage#initOwner(Window)
     */
    public void showChooseUser() {
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

    /**
     * {@link Controller} loader.
     *
     * @param controller The {@link Controller} to be loaded.
     * @see FXMLLoader
     */
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

    /**
     * Proxy method of {@link #loadController(Controller)} to load all {@code controllers}.
     */
    private void loadAllControllers() {
        for (Controller controller : controllers) loadController(controller);
    }

    /**
     * Dedicated loader for repeated {@code songItem.fxml}.
     *
     * @param controller Which {@link Controller} to own the item.
     * @return A {@link HBox} loaded with {@code songItem.fxml}.
     * @see FXMLLoader
     */
    protected @Nullable HBox loadSongItems(@NotNull Controller controller) {
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

    /**
     * Dedicated loader for repeated {@code playlistItems.fxml}.
     *
     * @return A {@link HBox} loaded with {@code playlistItems.fxml}.
     * @see FXMLLoader
     */
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

    /**
     * Reloads CSS current file.
     *
     * @see CSS
     */
    public void reloadCSS() {
        Scene scene = WINDOW.getScene();
        scene.getStylesheets().clear();
        setUserAgentStylesheet(null);
        scene.getStylesheets().add(String.valueOf((getClass().getResource(CSS.path()))));
    }

    /**
     * Plays a YouTube video through as a popup.
     * <br> A new {@link Stage} will be shown and will lock {@code WINDOW}.
     *
     * @param linkYT A {@code string} containing the YouTube video.
     * @see WebView
     * @see Stage#initOwner(Window)
     */
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

    /**
     * Inner {@link Enum} used for {@link #popUp(Notify, String)}, stating the popup preset.
     */
    public enum Notify {
        CONFIRM, ACCESS, FILE, INPUT;
    }
}
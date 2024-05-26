package com.playlistx.view;

import com.playlistx.model.Model;
import com.playlistx.model.music.Playlist;
import com.playlistx.model.paths.FXMLs;
import com.playlistx.viewmodel.PlayListsModel;
import com.playlistx.viewmodel.comparators.*;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.jetbrains.annotations.NotNull;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/**
 * {@link Views#ALL_PLAYLISTS} Controller class. Displays all user related {@link Playlist}s and handles view actions.
 *
 * @author Sergiu Chirap
 * @version final
 * @see Model#getAllPlaylists()
 * @since 0.5
 */
class PlayListsController implements Controller, PropertyChangeListener {
    /**
     * This is the {@code Singleton} instance.
     */
    private static PlayListsController instance;
    /**
     * Class own logic manager.
     *
     * @see PlayListsModel
     */
    private final PlayListsModel model = PlayListsModel.get();
    /**
     * The {@link Scene} of the class.
     */
    private Scene scene;
    /**
     * Boolean that states if the sort is in reverse state.
     */
    private boolean isSortReverse = false;
    /**
     * UI zone where injected {@link Playlist}s will be displayed.
     *
     * @see ViewHandler#loadPlaylistItems()
     */
    @FXML
    private VBox playlistList;
    /**
     * <b>play...</b> -> Per {@link Playlist} descriptive displays.
     * <br><b>sort...</b> ->  Interactive sort toggles.
     */
    @FXML
    private Label sortTitle, playTitle, sortYear, playYear, sortAuthor, playAuthor, sortAccess, playAccess, sortSongCount, playSongCount, activeSort;

    /**
     * Private constructor that is intended to run only once due being a {@code Singleton} class.
     */
    private PlayListsController() {
        model.addListener(this);
    }

    /**
     * {@code Singleton} getter. Gets the singleton instance or creates a new one if none exists.
     *
     * @return The singleton instance.
     */
    public static PlayListsController get() {
        if (instance == null) return instance = new PlayListsController();
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
    }

    /**
     * Loads {@link Playlist} and resets to default sort.
     *
     * @see #refresh(Comparator)
     */
    public void refresh() {
        refresh(new PlayTitleComparator());
        activeSort = sortTitle;
    }

    /**
     * Gets their respective {@code FXML} location.
     *
     * @return A {@code String} which contains the location of the respective {@code Controller}.
     */
    @Override
    public String getFXML() {
        return FXMLs.playlists;
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
     * Injects the available {@link Playlist}s sorted by the chosen {@link Comparator<Playlist>} to {@link #playlistList}.
     *
     * @param comparator One of our custom {@link Comparator<Playlist>}s.
     */
    private void refresh(Comparator<Playlist> comparator) {
        List<Playlist> playlists = model.getAllPlayLists();
        playlists.sort(comparator);
        playlistList.getChildren().clear();
        for (Playlist playlist : playlists) {
            HBox playlistItem;
            try {
                playlistItem = Objects.requireNonNull(ViewHandler.get()).loadPlaylistItems();
            } catch (NullPointerException e) {
                ViewHandler.popUp(ViewHandler.Notify.FILE, "Files for PlayLists couldn't be loaded!");
                throw new RuntimeException(e);
            }
            playTitle.setText(playlist.getTitle());
            playYear.setText(String.valueOf(Instant.ofEpochMilli(playlist.getCreationDate().getTime()).atZone(ZoneId.systemDefault()).toLocalDate().getYear()).substring(2));
            playAuthor.setText(playlist.getOwner());
            if (playlist.isPublic()) {
                playAccess.setText("Public");
            } else playAccess.setText("Private");
            playSongCount.setText(String.valueOf(playlist.getSongsCount()));
            assert playlistItem != null;
            playlistItem.addEventFilter(MouseEvent.MOUSE_CLICKED, e -> {
                ViewHandler.get().selectPlaylist(playlist.getId());
                Views.PLAYLIST.show();
            });
            playlistList.getChildren().add(playlistItem);
        }
    }

    /**
     * Toggles sort type.
     *
     * @param evt A {@link MouseEvent} from our sort {@link Label}s.
     */
    @FXML
    private void toggleSort(@NotNull MouseEvent evt) {
        if (evt.getSource() == activeSort) isSortReverse = !isSortReverse;
        clearVisualSelection();
        if (evt.getSource() == sortTitle) {
            activeSort = sortTitle;
            if (isSortReverse) {
                refresh(new PlayTitleComparator().reversed());
                sortTitle.setText(sortTitle.getText() + " ▲");
            } else {
                refresh(new PlayTitleComparator());
                isSortReverse = false;
                sortTitle.setText(sortTitle.getText() + " ▼");
            }
        } else if (evt.getSource() == sortYear) {
            activeSort = sortYear;
            if (isSortReverse) {
                refresh(new PlayYearComparator().reversed());
                sortYear.setText(sortYear.getText() + " ▲");
            } else {
                refresh(new PlayYearComparator());
                isSortReverse = false;
                sortYear.setText(sortYear.getText() + " ▼");
            }
        } else if (evt.getSource() == sortAuthor) {
            activeSort = sortAuthor;
            if (isSortReverse) {
                refresh(new PlayAuthorComparator().reversed());
                sortAuthor.setText(sortAuthor.getText() + " ▲");
            } else {
                refresh(new PlayAuthorComparator());
                isSortReverse = false;
                sortAuthor.setText(sortAuthor.getText() + " ▼");
            }
        } else if (evt.getSource() == sortAccess) {
            activeSort = sortAccess;
            if (isSortReverse) {
                refresh(new PlayAccessComparator().reversed());
                sortAccess.setText(sortAccess.getText() + " ▲");
            } else {
                refresh(new PlayAccessComparator());
                isSortReverse = false;
                sortAccess.setText(sortAccess.getText() + " ▼");
            }
        } else if (evt.getSource() == sortSongCount) {
            activeSort = sortSongCount;
            if (isSortReverse) {
                refresh(new PlaySongCountComparator().reversed());
                sortSongCount.setText(sortSongCount.getText() + " ▲");
            } else {
                refresh(new PlaySongCountComparator());
                isSortReverse = false;
                sortSongCount.setText(sortSongCount.getText() + " ▼");
            }
        }
    }

    /**
     * Deletes sort representative arrow. Made to avoid visual bugs.
     */
    private void clearVisualSelection() {
        activeSort.setText(activeSort.getText().substring(0, activeSort.getText().length() - 2));
    }

    /**
     * Creates a new {@link Playlist}.
     */
    @FXML
    private void createPlaylist() {
        model.createNewPlayList();
        refresh(new PlayTitleComparator());
    }

    /**
     * This method gets called when a bound property is changed.
     *
     * @param evt A PropertyChangeEvent object describing the event source
     *            and the property that has changed.
     */
    @Override
    public void propertyChange(@NotNull PropertyChangeEvent evt) {
        if (evt.getPropertyName().equalsIgnoreCase("REFRESH")) refresh();
    }
}
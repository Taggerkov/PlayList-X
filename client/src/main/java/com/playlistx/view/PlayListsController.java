package com.playlistx.view;

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

public class PlayListsController implements Controller, PropertyChangeListener {
    private static PlayListsController instance;
    private final PlayListsModel model = PlayListsModel.get();
    private Scene scene;
    private boolean isSortReverse = false;
    @FXML
    private VBox playlistList;
    @FXML
    private Label sortTitle, playTitle, sortYear, playYear, sortAuthor, playAuthor, sortAccess, playAccess, sortSongCount, playSongCount, activeSort;

    private PlayListsController() {
        model.addListener(this);
    }

    public static PlayListsController get() {
        if (instance == null) return instance = new PlayListsController();
        return instance;
    }

    @Override
    public void init(@NotNull Scene scene) {
        this.scene = scene;
    }

    public void refresh() {
        refresh(new PlayTitleComparator());
        activeSort = sortTitle;
    }

    @Override
    public String getFXML() {
        return FXMLs.playlists;
    }

    @Override
    public Scene getScene() {
        return scene;
    }

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

    private void clearVisualSelection() {
        activeSort.setText(activeSort.getText().substring(0, activeSort.getText().length() - 2));
    }

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
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equalsIgnoreCase("REFRESH")) refresh();
    }
}
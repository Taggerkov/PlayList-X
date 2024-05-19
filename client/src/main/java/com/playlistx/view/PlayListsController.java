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

import java.time.ZoneId;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class PlayListsController implements Controller {
    private static PlayListsController instance;
    private final PlayListsModel model = PlayListsModel.get();
    private Scene scene;
    private boolean isSortReverse = false;
    @FXML
    private VBox playlistList;
    @FXML
    private Label sortTitle, playTitle, sortYear, playYear, sortAuthor, playAuthor, sortAccess, playAccess, sortSongCount, playSongCount, activeSort;

    private PlayListsController() {
    }

    public static PlayListsController get() {
        if (instance == null) return instance = new PlayListsController();
        return instance;
    }

    @Override
    public void init(@NotNull Scene scene) {
        this.scene = scene;
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

    public void refresh(Comparator<Playlist> comparator) {
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
            playYear.setText(String.valueOf(playlist.getCreationDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().getYear()).substring(2));
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
        if (evt.getSource() == sortTitle) {
            clearVisualSelection();
            activeSort = sortTitle;
            if (isSortReverse){
                refresh(new PlayTitleComparator().reversed());
                sortTitle.setText(sortTitle.getText() + " ▲");
            }
            else {
                refresh(new PlayTitleComparator());
                isSortReverse = false;
                sortTitle.setText(sortTitle.getText() + " ▼");
            }
        } else if (evt.getSource() == sortYear) {
            clearVisualSelection();
            activeSort = sortYear;
            if (isSortReverse) {
                refresh(new PlayYearComparator().reversed());
                sortTitle.setText(sortTitle.getText() + " ▲");
            }
            else {
                refresh(new PlayYearComparator());
                isSortReverse = false;
                sortTitle.setText(sortTitle.getText() + " ▼");
            }
        } else if (evt.getSource() == sortAuthor) {
            activeSort = sortAuthor;
            clearVisualSelection();
            if (isSortReverse) {
                refresh(new PlayAuthorComparator().reversed());
                sortTitle.setText(sortTitle.getText() + " ▲");
            }
            else {
                refresh(new PlayAuthorComparator());
                isSortReverse = false;
                sortTitle.setText(sortTitle.getText() + " ▼");
            }
        } else if (evt.getSource() == sortAccess) {
            clearVisualSelection();
            activeSort = sortAccess;
            if (isSortReverse) {
                refresh(new PlayAccessComparator().reversed());
                sortTitle.setText(sortTitle.getText() + " ▲");
            }
            else {
                refresh(new PlayAccessComparator());
                isSortReverse = false;
                sortTitle.setText(sortTitle.getText() + " ▼");
            }
        } else if (evt.getSource() == sortSongCount) {
            clearVisualSelection();
            activeSort = sortSongCount;
            if (isSortReverse) {
                refresh(new PlaySongCountComparator().reversed());
                sortTitle.setText(sortTitle.getText() + " ▲");
            }
            else {
                refresh(new PlaySongCountComparator());
                isSortReverse = false;
                sortTitle.setText(sortTitle.getText() + " ▼");
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
}
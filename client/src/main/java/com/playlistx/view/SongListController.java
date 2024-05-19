package com.playlistx.view;

import com.playlistx.model.music.Song;
import com.playlistx.model.paths.FXMLs;
import com.playlistx.viewmodel.SongListModel;
import com.playlistx.viewmodel.comparators.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class SongListController implements Controller {
    private static SongListController instance;
    private final SongListModel model = SongListModel.get();
    private Scene scene;
    private boolean isSortReverse = false, isSelect;
    private int selectPlaylistID;
    @FXML
    private VBox songList;
    @FXML
    private Label songTitle, sortTitle, songYear, sortYear, songArtist, sortArtist, songGenre, sortGenre, songAlbum, sortAlbum, songDuration, sortDuration, activeSort;

    private SongListController() {
    }

    public static SongListController get() {
        if (instance == null) return instance = new SongListController();
        return instance;
    }

    @Override
    public void init(@NotNull Scene scene) {
        this.scene = scene;
    }

    @Override
    public String getFXML() {
        return FXMLs.songList;
    }

    @Override
    public Scene getScene() {
        return scene;
    }

    public void isSelect(boolean isSelect, int selectPlaylistID) {
        refresh(new SongYearComparator());
        activeSort = sortYear;
        this.isSelect = isSelect;
        this.selectPlaylistID = selectPlaylistID;
    }

    private void refresh(Comparator<Song> comparator) {
        List<Song> songs = model.getSongsAll();
        songs.sort(comparator);
        songList.getChildren().clear();
        for (Song song : songs) {
            HBox songItem;
            try {
                songItem = Objects.requireNonNull(ViewHandler.get()).loadSongItems();
            } catch (NullPointerException e) {
                ViewHandler.popUp(ViewHandler.Notify.FILE, "Files for Song List couldn't be loaded!");
                throw new RuntimeException(e);
            }
            songTitle.setText(song.getTitle());
            songYear.setText(String.valueOf(song.getYear()).substring(2));
            songArtist.setText(song.getArtist());
            songGenre.setText(song.getGenre());
            songDuration.setText(String.valueOf(song.getDuration()));
            assert songItem != null;
            if (isSelect) songItem.addEventFilter(MouseEvent.MOUSE_CLICKED, evt -> {
                model.addToPlaylist(selectPlaylistID, song);
                Views.PLAYLIST.show();
            });
            songList.getChildren().add(songItem);
        }
    }

    @FXML
    private void addSong() {
        ViewHandler.popUp(ViewHandler.Notify.ACCESS, "Feature still in Work! Contact us through email.");
    }

    @FXML
    private void toggleSort(@NotNull MouseEvent evt) {
        if (evt.getSource() == activeSort) isSortReverse = !isSortReverse;
        if (evt.getSource() == sortTitle) {
            clearVisualSelection();
            activeSort = sortTitle;
            if (isSortReverse){
                refresh(new SongTitleComparator().reversed());
                sortTitle.setText(sortTitle.getText() + " ▲");
            }
            else {
                refresh(new SongTitleComparator());
                isSortReverse = false;
                sortTitle.setText(sortTitle.getText() + " ▼");
            }
        } else if (evt.getSource() == sortYear) {
            clearVisualSelection();
            activeSort = sortYear;
            if (isSortReverse) {
                refresh(new SongYearComparator().reversed());
                sortTitle.setText(sortTitle.getText() + " ▲");
            }
            else {
               refresh(new SongYearComparator());
                isSortReverse = false;
                sortTitle.setText(sortTitle.getText() + " ▼");
            }
        } else if (evt.getSource() == sortArtist) {
            activeSort = sortArtist;
            clearVisualSelection();
            if (isSortReverse) {
                refresh(new SongArtistComparator().reversed());
                sortTitle.setText(sortTitle.getText() + " ▲");
            }
            else {
                refresh(new SongArtistComparator());
                isSortReverse = false;
                sortTitle.setText(sortTitle.getText() + " ▼");
            }
        } else if (evt.getSource() == sortGenre) {
            clearVisualSelection();
            activeSort = sortGenre;
            if (isSortReverse) {
                refresh(new SongGenreComparator().reversed());
                sortTitle.setText(sortTitle.getText() + " ▲");
            }
            else {
                refresh(new SongGenreComparator());
                isSortReverse = false;
                sortTitle.setText(sortTitle.getText() + " ▼");
            }
        } else if (evt.getSource() == sortAlbum) {
            clearVisualSelection();
            activeSort = sortGenre;
            if (isSortReverse) {
                refresh(new SongAlbumComparator().reversed());
                sortTitle.setText(sortTitle.getText() + " ▲");
            }
            else {
                refresh(new SongAlbumComparator());
                isSortReverse = false;
                sortTitle.setText(sortTitle.getText() + " ▼");
            }
        } else if (evt.getSource() == sortDuration) {
            clearVisualSelection();
            activeSort = sortDuration;
            if (isSortReverse) {
                refresh(new SongDurationComparator().reversed());
                sortTitle.setText(sortTitle.getText() + " ▲");
            }
            else {
                refresh(new SongDurationComparator());
                isSortReverse = false;
                sortTitle.setText(sortTitle.getText() + " ▼");
            }
        }
    }

    private void clearVisualSelection() {
        activeSort.setText(activeSort.getText().substring(0, activeSort.getText().length() - 2));
    }
}
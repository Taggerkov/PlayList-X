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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class SongListController implements Controller, PropertyChangeListener {
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
        model.addListener(this);
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
                songItem = ViewHandler.get().loadSongItems();
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
    private void toggleSort(@NotNull MouseEvent evt) {
        if (evt.getSource() == activeSort) isSortReverse = !isSortReverse;
        clearVisualSelection();
        if (evt.getSource() == sortTitle) {
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
            activeSort = sortYear;
            if (isSortReverse) {
                refresh(new SongYearComparator().reversed());
                sortYear.setText(sortYear.getText() + " ▲");
            }
            else {
               refresh(new SongYearComparator());
                isSortReverse = false;
                sortYear.setText(sortYear.getText() + " ▼");
            }
        } else if (evt.getSource() == sortArtist) {
            activeSort = sortArtist;
            if (isSortReverse) {
                refresh(new SongArtistComparator().reversed());
                sortArtist.setText(sortArtist.getText() + " ▲");
            }
            else {
                refresh(new SongArtistComparator());
                isSortReverse = false;
                sortArtist.setText(sortArtist.getText() + " ▼");
            }
        } else if (evt.getSource() == sortGenre) {
            activeSort = sortGenre;
            if (isSortReverse) {
                refresh(new SongGenreComparator().reversed());
                sortGenre.setText(sortGenre.getText() + " ▲");
            }
            else {
                refresh(new SongGenreComparator());
                isSortReverse = false;
                sortGenre.setText(sortGenre.getText() + " ▼");
            }
        } else if (evt.getSource() == sortAlbum) {
            activeSort = sortAlbum;
            if (isSortReverse) {
                refresh(new SongAlbumComparator().reversed());
                sortAlbum.setText(sortAlbum.getText() + " ▲");
            }
            else {
                refresh(new SongAlbumComparator());
                isSortReverse = false;
                sortAlbum.setText(sortAlbum.getText() + " ▼");
            }
        } else if (evt.getSource() == sortDuration) {
            activeSort = sortDuration;
            if (isSortReverse) {
                refresh(new SongDurationComparator().reversed());
                sortDuration.setText(sortDuration.getText() + " ▲");
            }
            else {
                refresh(new SongDurationComparator());
                isSortReverse = false;
                sortDuration.setText(sortDuration.getText() + " ▼");
            }
        }
    }

    private void clearVisualSelection() {
        activeSort.setText(activeSort.getText().substring(0, activeSort.getText().length() - 2));
    }

    @FXML
    private void addSong() {
        model.addSong();
    }

    /**
     * This method gets called when a bound property is changed.
     *
     * @param evt A PropertyChangeEvent object describing the event source
     *            and the property that has changed.
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {

    }
}
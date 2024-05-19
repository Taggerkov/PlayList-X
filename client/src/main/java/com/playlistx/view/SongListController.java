package com.playlistx.view;

import com.playlistx.model.music.Song;
import com.playlistx.model.paths.FXMLs;
import com.playlistx.viewmodel.SongListModel;
import com.playlistx.viewmodel.comparators.SongTitleComparator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public class SongListController implements Controller {
    private static SongListController instance;
    private final SongListModel model = SongListModel.get();
    private final SongTitleComparator titleComparator = new SongTitleComparator();
    private Scene scene;
    private boolean isSortReverse, isSelect;
    private int selectPlaylistID;
    @FXML
    private VBox songList;
    @FXML
    private Label songTitle, sortTitle, songYear, sortYear, songArtist, sortArtist, songGenre, sortGenre, songAlbum, sortAlbum, songDuration, sortDuration, activeSort;

    private SongListController() {
    }

    public static SongListController get()  {
        if (instance == null) return instance = new SongListController();
        return instance;
    }

    @Override
    public void init(@NotNull Scene scene) {
        this.scene = scene;
    }

    @Override
    public String getFXML() {
        return FXMLs.login;
    }

    @Override
    public Scene getScene() {
        return scene;
    }

    public void isSelect(boolean isSelect, int selectPlaylistID) {
        this.isSelect = isSelect;
        this.selectPlaylistID = selectPlaylistID;
    }

    private void refresh() {
        List<Song> songs = model.getSongsAll();
        songs.sort(titleComparator);
        songList.getChildren().clear();
        for (Song song : songs) {
            HBox songItem = new HBox();
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

    }

    @FXML
    private void toggleSort(@NotNull ActionEvent evt) {
        if (evt.getSource() == activeSort) isSortReverse = !isSortReverse;
        if (evt.getSource() == sortTitle) {


        } else if (evt.getSource() == sortYear) {

        } else if (evt.getSource() == sortArtist) {

        } else if (evt.getSource() == sortGenre) {

        } else if (evt.getSource() == sortAlbum) {

        } else if (evt.getSource() == sortDuration) {

        }
    }
}
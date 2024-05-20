package com.playlistx.view;

import com.playlistx.model.music.Playlist;
import com.playlistx.model.music.Song;
import com.playlistx.model.paths.FXMLs;
import com.playlistx.viewmodel.ThePlayListModel;
import com.playlistx.viewmodel.comparators.*;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.jetbrains.annotations.NotNull;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Comparator;
import java.util.List;


public class ThePlayListController implements Controller, PropertyChangeListener {
    private static ThePlayListController instance;
    private final ThePlayListModel model = ThePlayListModel.get();
    private int playlistId;
    private boolean isSortReverse = false;
    private Scene scene;
    private boolean isOnClick, isOnShift;
    @FXML
    private VBox songList;
    @FXML
    private Label sortTitle, songTitle, sortYear, songYear, sortArtist, songArtist, sortGenre, songGenre, sortAlbum, songAlbum, sortDuration, songDuration, activeSort;
    @FXML
    private TextArea screenTitle, screenDesc;
    @FXML
    private Text isPublic;

    private ThePlayListController() {
        model.addListener(this);
    }

    public static ThePlayListController get() {
        if (instance == null) return instance = new ThePlayListController();
        else return instance;
    }

    @Override
    public void init(@NotNull Scene scene) {
        this.scene = scene;
    }

    @Override
    public String getFXML() {
        return FXMLs.thePlaylist;
    }

    @Override
    public Scene getScene() {
        return scene;
    }

    public void setPlayList(int playlistId) {
        this.playlistId = playlistId;
    }

    public void refresh() {
        if (activeSort == null) {
            refresh(new SongYearComparator());
            activeSort = sortYear;
        }
        //noinspection DuplicatedCode
        if (activeSort.equals(sortTitle)) refresh(new SongTitleComparator());
        else if (activeSort.equals(sortYear)) refresh(new SongYearComparator());
        else if (activeSort.equals(sortArtist)) refresh(new SongArtistComparator());
        else if (activeSort.equals(sortGenre)) refresh(new SongGenreComparator());
        else if (activeSort.equals(sortAlbum)) refresh(new SongAlbumComparator());
        else if (activeSort.equals(sortDuration)) refresh(new SongDurationComparator());
    }

    @SuppressWarnings("DuplicatedCode")
    private void refresh(Comparator<Song> comparator) {
        Playlist thePlayList = model.getPlayList(playlistId);
        screenTitle.setText(thePlayList.getTitle());
        // screenDesc.setText(thePlayList.getDescription());
        if (thePlayList.isPublic()) isPublic.setText("Pu");
        else isPublic.setText("Pr");
        List<Song> songs = thePlayList.getSongs();
        songs.sort(comparator);
        songList.getChildren().clear();
        for (Song song : songs) {
            System.out.println("Song: " + song.getTitle());
            HBox songItem;
            try {
                songItem = ViewHandler.get().loadSongItems(this);
            } catch (NullPointerException e) {
                ViewHandler.popUp(ViewHandler.Notify.FILE, "Files for Song List couldn't be loaded!");
                throw new RuntimeException(e);
            }
            songTitle.setText(song.getTitle());
            songYear.setText(String.valueOf(song.getYear()).substring(2));
            songArtist.setText(song.getArtist());
            songGenre.setText(song.getGenre());
            songAlbum.setText(song.getAlbumName());
            songDuration.setText(String.valueOf(song.getDuration()));
            assert songItem != null;
            songItem.addEventFilter(MouseEvent.MOUSE_CLICKED, evt -> {
                if (evt.getButton().equals(MouseButton.PRIMARY)) ViewHandler.get().playVideoYT(song.getLink());
                else if (evt.getButton().equals(MouseButton.SECONDARY)) {
                    thePlayList.removeSong(song);
                    refresh(comparator);
                }
            });
            songList.getChildren().add(songItem);
        }
    }

    @FXML
    @SuppressWarnings("DuplicatedCode")
    private void toggleSort(@NotNull MouseEvent evt) {
        if (evt.getSource() == activeSort) isSortReverse = !isSortReverse;
        clearVisualSelection();
        if (evt.getSource() == sortTitle) {
            activeSort = sortTitle;
            if (isSortReverse) {
                refresh(new SongTitleComparator().reversed());
                sortTitle.setText(sortTitle.getText() + " ▲");
            } else {
                refresh(new SongTitleComparator());
                isSortReverse = false;
                sortTitle.setText(sortTitle.getText() + " ▼");
            }
        } else if (evt.getSource() == sortYear) {
            activeSort = sortYear;
            if (isSortReverse) {
                refresh(new SongYearComparator().reversed());
                sortYear.setText(sortYear.getText() + " ▲");
            } else {
                refresh(new SongYearComparator());
                isSortReverse = false;
                sortYear.setText(sortYear.getText() + " ▼");
            }
        } else if (evt.getSource() == sortArtist) {
            activeSort = sortArtist;
            if (isSortReverse) {
                refresh(new SongArtistComparator().reversed());
                sortArtist.setText(sortArtist.getText() + " ▲");
            } else {
                refresh(new SongArtistComparator());
                isSortReverse = false;
                sortArtist.setText(sortArtist.getText() + " ▼");
            }
        } else if (evt.getSource() == sortGenre) {
            activeSort = sortGenre;
            if (isSortReverse) {
                refresh(new SongGenreComparator().reversed());
                sortGenre.setText(sortGenre.getText() + " ▲");
            } else {
                refresh(new SongGenreComparator());
                isSortReverse = false;
                sortGenre.setText(sortGenre.getText() + " ▼");
            }
        } else if (evt.getSource() == sortAlbum) {
            activeSort = sortAlbum;
            if (isSortReverse) {
                refresh(new SongAlbumComparator().reversed());
                sortAlbum.setText(sortAlbum.getText() + " ▲");
            } else {
                refresh(new SongAlbumComparator());
                isSortReverse = false;
                sortAlbum.setText(sortAlbum.getText() + " ▼");
            }
        } else if (evt.getSource() == sortDuration) {
            activeSort = sortDuration;
            if (isSortReverse) {
                refresh(new SongDurationComparator().reversed());
                sortDuration.setText(sortDuration.getText() + " ▲");
            } else {
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
    private void saveTitle() {
        model.newTitle(playlistId, screenTitle.getText());
    }

    @FXML
    private void saveDesc() {
        model.newDesc(playlistId, screenDesc.getText());
    }

    @FXML
    private void addSong() {
        Views.SONGLIST_SELECT.show();
    }

    @FXML
    private void share() {
        ViewHandler.get().showChooseUser();
    }

    @FXML
    private void toggleVisibility() {
        boolean temp = isPublic.getText().equalsIgnoreCase("PU");
        model.isPublic(playlistId, temp);
        if (temp) isPublic.setText("Pr");
        else isPublic.setText("Pu");
    }

    /**
     * This method gets called when a bound property is changed.
     *
     * @param evt A PropertyChangeEvent object describing the event source
     *            and the property that has changed.
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        refresh();
    }
}
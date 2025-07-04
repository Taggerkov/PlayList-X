package com.playlistx.view;

import com.playlistx.model.music.Playlist;
import com.playlistx.model.music.Song;
import com.playlistx.model.paths.FXMLs;
import com.playlistx.viewmodel.SongListModel;
import com.playlistx.viewmodel.comparators.*;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.jetbrains.annotations.NotNull;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Comparator;
import java.util.List;

/**
 * {@link Views#SONGLIST} Controller Class. Displays all available songs in our database and handler user interaction.
 *
 * @author Sergiu Chirap
 * @version final
 * @see PlayListsController
 * @since 0.4
 */
class SongListController implements Controller, PropertyChangeListener {
    /**
     * This is the {@code Singleton} instance.
     */
    private static SongListController instance;
    /**
     * Class own logic manager.
     *
     * @see SongListModel
     */
    private final SongListModel model = SongListModel.get();
    /**
     * The {@link Scene} of the class.
     */
    private Scene scene;
    /**
     * <b>isSortReverse</b> = Boolean that states if the sort is in reverse state.
     * <br> <b>isSelect</b> = States how user got to this view. The view responds different depending on this.
     */
    private boolean isSortReverse = false, isSelect;
    /**
     * ID of the scoped playlist.
     */
    private int selectPlaylistID;
    /**
     * UI zone where injected {@link Song}s will be displayed.
     *
     * @see ViewHandler#loadSongItems(Controller)
     */
    @FXML
    private VBox songList;
    /**
     * <b>song...</b> -> Per {@link Song} descriptive displays.
     * <br><b>sort...</b> ->  Interactive sort toggles.
     */
    @FXML
    private Label songTitle, sortTitle, songYear, sortYear, songArtist, sortArtist, songGenre, sortGenre, songAlbum, sortAlbum, songDuration, sortDuration, activeSort;

    /**
     * Private constructor that is intended to run only once due being a {@code Singleton} class.
     */
    private SongListController() {
        model.addListener(this);
    }

    /**
     * {@code Singleton} getter. Gets the singleton instance or creates a new one if none exists.
     *
     * @return The singleton instance.
     */
    public static SongListController get() {
        if (instance == null) return instance = new SongListController();
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
     * Gets their respective {@code FXML} location.
     *
     * @return A {@code String} which contains the location of the respective {@code Controller}
     */
    @Override
    public String getFXML() {
        return FXMLs.songList;
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
     * Sets {@link #isSelect} and if {@code true}, sets {@link #selectPlaylistID}.
     *
     * @param isSelect         A {@code boolean} that states how the user got to this view.
     * @param selectPlaylistID The scoped {@link Playlist} ID.
     */
    public void isSelect(boolean isSelect, int selectPlaylistID) {
        refresh(new SongYearComparator());
        activeSort = sortYear;
        this.isSelect = isSelect;
        this.selectPlaylistID = selectPlaylistID;
    }

    /**
     * Injects all available {@link Song}s into {@link #songList}.
     *
     * @param comparator One of our custom {@link Comparator<Playlist>}s.
     */
    @SuppressWarnings("DuplicatedCode")
    private void refresh(Comparator<Song> comparator) {
        List<Song> songs = model.getSongsAll();
        songs.sort(comparator);
        songList.getChildren().clear();
        for (Song song : songs) {
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
                if (evt.getButton().equals(MouseButton.PRIMARY))
                    if (!isSelect) ViewHandler.get().playVideoYT(song.getLink());
                    else {
                        model.addToPlaylist(selectPlaylistID, song);
                        Views.PLAYLIST.show();
                    }
            });
            songList.getChildren().add(songItem);
        }
    }

    /**
     * Toggles sort type.
     *
     * @param evt A {@link MouseEvent} from our sort {@link Label}s.
     */
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

    /**
     * Deletes sort representative arrow. Made to avoid visual bugs.
     */
    private void clearVisualSelection() {
        activeSort.setText(activeSort.getText().substring(0, activeSort.getText().length() - 2));
    }

    /**
     * Suggests a new song to our admins.
     */
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
    public void propertyChange(@NotNull PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("REFRESH")) {
            //noinspection DuplicatedCode
            if (activeSort.equals(sortTitle)) refresh(new SongTitleComparator());
            else if (activeSort.equals(sortYear)) refresh(new SongYearComparator());
            else if (activeSort.equals(sortArtist)) refresh(new SongArtistComparator());
            else if (activeSort.equals(sortGenre)) refresh(new SongGenreComparator());
            else if (activeSort.equals(sortAlbum)) refresh(new SongAlbumComparator());
            else if (activeSort.equals(sortDuration)) refresh(new SongDurationComparator());
        }
    }
}
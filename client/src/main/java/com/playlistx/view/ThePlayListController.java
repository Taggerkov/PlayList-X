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
import javafx.scene.control.TextField;
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

/**
 * {@link Views#PLAYLIST} Controller class. Display data from the scoped {@link Playlist} and handles user actions.
 *
 * @author Sergiu Chirap
 * @version final
 * @since 0.7
 */
class ThePlayListController implements Controller, PropertyChangeListener {
    /**
     * This is the {@code Singleton} instance.
     */
    private static ThePlayListController instance;
    /**
     * Class own logic manager.
     *
     * @see ThePlayListModel
     */
    private final ThePlayListModel model = ThePlayListModel.get();
    /**
     * ID of the scoped {@link Playlist}.
     */
    private int playlistId;
    /**
     * Boolean that states if the sort is in reverse state.
     */
    private boolean isSortReverse = false;
    /**
     * The {@link Scene} of the class.
     */
    private Scene scene;
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
    private Label sortTitle, songTitle, sortYear, songYear, sortArtist, songArtist, sortGenre, songGenre, sortAlbum, songAlbum, sortDuration, songDuration, activeSort;
    /**
     * {@link Playlist} title display and text input.
     */
    @FXML
    private TextField screenTitle;
    /**
     * {@link Playlist} description display and text input.
     */
    @FXML
    private TextArea screenDesc;
    /**
     * {@link Playlist} Publicity state display.
     */
    @FXML
    private Text isPublic;

    /**
     * Private constructor that is intended to run only once due being a {@code Singleton} class.
     */
    private ThePlayListController() {
        model.addListener(this);
    }

    /**
     * {@code Singleton} getter. Gets the singleton instance or creates a new one if none exists.
     *
     * @return The singleton instance.
     */
    public static ThePlayListController get() {
        if (instance == null) return instance = new ThePlayListController();
        else return instance;
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
        return FXMLs.thePlaylist;
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
     * Sets {@link #playlistId}.
     *
     * @param playlistId The ID of the scoped {@link Playlist}.
     */
    public void setPlayList(int playlistId) {
        this.playlistId = playlistId;
    }

    /**
     * Loads {@link Song}s and refreshes view.
     *
     * @see #refresh(Comparator)
     */
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

    /**
     * Injects all available {@link Song}s into {@link #songList}.
     *
     * @param comparator One of our custom {@link Comparator<Playlist>}s.
     */
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
     * Changes the scoped {@link Playlist} title with the extracted input of {@link #screenTitle}.
     */
    @FXML
    private void saveTitle() {
        model.newTitle(playlistId, screenTitle.getText());
    }

    /**
     * Changes the scoped {@link Playlist} description with the extracted input of {@link #screenDesc}.
     */
    @FXML
    private void saveDesc() {
        model.newDesc(playlistId, screenDesc.getText());
    }

    /**
     * Adds a chosen from the available {@link Song}s to the scoped {@link Playlist}.
     */
    @FXML
    private void addSong() {
        Views.SONGLIST_SELECT.show();
    }

    /**
     * Shares access of the scoped {@link Playlist} to another user.
     * @see ChooseUserController
     */
    @FXML
    private void share() {
        ViewHandler.get().showChooseUser();
    }

    /**
     * Toggles scoped {@link Playlist} visibility.
     */
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
    public void propertyChange(@NotNull PropertyChangeEvent evt) {
        if (evt.getPropertyName().equalsIgnoreCase("REFRESH")) refresh();
    }
}
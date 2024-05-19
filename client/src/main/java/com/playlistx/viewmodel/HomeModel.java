package com.playlistx.viewmodel;

import com.playlistx.model.Model;
import com.playlistx.model.music.Playlist;
import com.playlistx.model.music.Song;
import com.playlistx.view.ViewHandler;
import org.jetbrains.annotations.NotNull;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class HomeModel {
    private static HomeModel instance;
    private final Model model = Model.get();

    private HomeModel() throws RemoteException, NotBoundException {

    }

    public static @NotNull HomeModel get() throws NotBoundException, RemoteException {
        if (instance == null) return instance = new HomeModel();
        else return instance;
    }

    public @NotNull List<Playlist> getPlaylistsAll() {
        try {
            return model.getAllPlaylists();
        } catch (RemoteException e) {
            ViewHandler.popUp(ViewHandler.Notify.ACCESS, "RMI Connection Error!");
            return new ArrayList<>();
        }
    }

    public @NotNull List<Playlist> getPlaylistsLatest() {
        List<Playlist> playlists = getPlaylistsAll();
        if (playlists.size() <= 1) return playlists;
        playlists.sort(new PlayListComparator());
        int index;
        if (playlists.size() > 15) index = 14;
        else index = playlists.size() - 1;
        return playlists.subList(0, index);
    }

    public @NotNull List<Song> getSongsAll() {
        try {
            return model.getAllSongs();
        } catch (RemoteException e) {
            ViewHandler.popUp(ViewHandler.Notify.ACCESS, "RMI Connection Error!");
            return new ArrayList<>();
        }
    }

    public @NotNull List<Song> getSongsLatest() {
        List<Song> songs = getSongsAll();
        if (songs.size() <= 1) return songs;
        songs.sort(new SongComparator());
        int index;
        if (songs.size() > 15) index = 14;
        else index = songs.size() - 1;
        return songs.subList(0, index);
    }

    private static class PlayListComparator implements java.util.Comparator<Playlist> {

        /**
         * Compares its two arguments for order.  Returns a negative integer,
         * zero, or a positive integer as the first argument is less than, equal
         * to, or greater than the second.<p>
         * <p>
         * The implementor must ensure that {@link Integer#signum
         * signum}{@code (compare(x, y)) == -signum(compare(y, x))} for
         * all {@code x} and {@code y}.  (This implies that {@code
         * compare(x, y)} must throw an exception if and only if {@code
         * compare(y, x)} throws an exception.)<p>
         * <p>
         * The implementor must also ensure that the relation is transitive:
         * {@code ((compare(x, y)>0) && (compare(y, z)>0))} implies
         * {@code compare(x, z)>0}.<p>
         * <p>
         * Finally, the implementor must ensure that {@code compare(x,
         * y)==0} implies that {@code signum(compare(x,
         * z))==signum(compare(y, z))} for all {@code z}.
         *
         * @param list1 the first Playlist to be compared.
         * @param list2 the second Playlist to be compared.
         * @return a negative integer, zero, or a positive integer as the
         * first argument is less than, equal to, or greater than the
         * second.
         * @throws NullPointerException if an argument is null and this
         *                              comparator does not permit null arguments
         * @throws ClassCastException   if the arguments' types prevent them from
         *                              being compared by this comparator.
         * @apiNote It is generally the case, but <i>not</i> strictly required that
         * {@code (compare(x, y)==0) == (x.equals(y))}.  Generally speaking,
         * any comparator that violates this condition should clearly indicate
         * this fact.  The recommended language is "Note: this comparator
         * imposes orderings that are inconsistent with equals."
         */
        @Override
        public int compare(@NotNull Playlist list1, @NotNull Playlist list2) {
            return list1.getSongsCount() - list2.getSongsCount();
        }
    }

    private static class SongComparator implements java.util.Comparator<Song> {

        /**
         * Compares its two arguments for order.  Returns a negative integer,
         * zero, or a positive integer as the first argument is less than, equal
         * to, or greater than the second.<p>
         * <p>
         * The implementor must ensure that {@link Integer#signum
         * signum}{@code (compare(x, y)) == -signum(compare(y, x))} for
         * all {@code x} and {@code y}.  (This implies that {@code
         * compare(x, y)} must throw an exception if and only if {@code
         * compare(y, x)} throws an exception.)<p>
         * <p>
         * The implementor must also ensure that the relation is transitive:
         * {@code ((compare(x, y)>0) && (compare(y, z)>0))} implies
         * {@code compare(x, z)>0}.<p>
         * <p>
         * Finally, the implementor must ensure that {@code compare(x,
         * y)==0} implies that {@code signum(compare(x,
         * z))==signum(compare(y, z))} for all {@code z}.
         *
         * @param song1 the first song to be compared.
         * @param song2 the second song to be compared.
         * @return a negative integer, zero, or a positive integer as the
         * first argument is less than, equal to, or greater than the
         * second.
         * @throws NullPointerException if an argument is null and this
         *                              comparator does not permit null arguments
         * @throws ClassCastException   if the arguments' types prevent them from
         *                              being compared by this comparator.
         * @apiNote It is generally the case, but <i>not</i> strictly required that
         * {@code (compare(x, y)==0) == (x.equals(y))}.  Generally speaking,
         * any comparator that violates this condition should clearly indicate
         * this fact.  The recommended language is "Note: this comparator
         * imposes orderings that are inconsistent with equals."
         */
        @Override
        public int compare(@NotNull Song song1, @NotNull Song song2) {
            return song1.getYear() - song2.getYear();
        }


    }


}

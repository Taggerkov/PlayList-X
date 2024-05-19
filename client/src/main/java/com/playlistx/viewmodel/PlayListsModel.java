package com.playlistx.viewmodel;

import com.playlistx.model.Model;
import com.playlistx.model.login.User;
import com.playlistx.model.music.Playlist;
import com.playlistx.view.ViewHandler;
import org.jetbrains.annotations.NotNull;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PlayListsModel {
    private static PlayListsModel instance;
    private final Model model = Model.get();

    private PlayListsModel() throws RemoteException, NotBoundException {
    }

    public static @NotNull PlayListsModel get() {
        try {
            if (instance == null) return instance = new PlayListsModel();
            else return instance;
        } catch (RemoteException | NotBoundException e) {
            ViewHandler.popUp(ViewHandler.Notify.ACCESS, "RMI Connection Error!");
            throw new RuntimeException(e);
        }
    }

    public @NotNull List<Playlist> getAllPlayLists() {
        try {
            return model.getAllPlaylists();
        } catch (RemoteException e) {
            ViewHandler.popUp(ViewHandler.Notify.ACCESS, "RMI Connection Error!");
            return new ArrayList<>();
        }
    }

    public void createNewPlayList() {
        Random random = new Random();
        try {
            model.createPlaylist(LocalDateTime.now().getNano() + random.nextInt(5000, 10000), User.genUsername() + "'s PlayList of " + LocalDateTime.now().truncatedTo(ChronoUnit.HOURS),
                    User.genUsername(), new ArrayList<>(), Date.valueOf(LocalDate.now()), 0, false);
        } catch (RemoteException e) {
            ViewHandler.popUp(ViewHandler.Notify.ACCESS, "RMI Connection Error!");
        }
    }
}
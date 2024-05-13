package com.playlistx.view;

import com.playlistx.model.paths.FXMLs;
import com.playlistx.viewmodel.LoginModel;
import javafx.scene.Scene;
import org.jetbrains.annotations.NotNull;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class SongListController implements Controller {
    private static SongListController instance;
    private final LoginModel model = LoginModel.get();
    private Scene scene;

    private SongListController() throws NotBoundException, RemoteException {
    }

    public static SongListController get() throws NotBoundException, RemoteException {
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

}

package com.playlistx.view;

import com.playlistx.model.paths.FXMLs;
import com.playlistx.viewmodel.LoginModel;
import javafx.scene.Scene;
import org.jetbrains.annotations.NotNull;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class PlayListsController implements Controller {
    private static PlayListsController instance;
    private final LoginModel model = LoginModel.get();
    private Scene scene;

    private PlayListsController() throws NotBoundException, RemoteException {
    }

    public static PlayListsController get() throws NotBoundException, RemoteException {
        if (instance == null) return instance = new PlayListsController();
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
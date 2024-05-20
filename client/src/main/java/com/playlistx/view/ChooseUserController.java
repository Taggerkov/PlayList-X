package com.playlistx.view;

import com.playlistx.model.paths.FXMLs;
import com.playlistx.viewmodel.ChooseUserModel;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.controlsfx.control.textfield.TextFields;
import org.jetbrains.annotations.NotNull;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;

class ChooseUserController implements Controller {
    private final ChooseUserModel model = ChooseUserModel.get();
    private static ChooseUserController instance;
    private Stage stage;
    private Scene scene;
    private int playlistID;
    private ArrayList<String> users = new ArrayList<>();
    @FXML
    private TextField chosenUser;

    private ChooseUserController(int playlistID) throws RemoteException, NotBoundException {
        this.playlistID = playlistID;
    }

    protected static ChooseUserController get(int playlistID) throws RemoteException, NotBoundException {
        if (instance == null) instance = new ChooseUserController(playlistID);
        instance.playlistID = playlistID;
        return instance;
    }

    @Override
    public void init(@NotNull Scene scene) {
        this.scene = scene;
        TextFields.bindAutoCompletion(chosenUser, model.getUsers(playlistID));
    }

    @Override
    public String getFXML() {
        return FXMLs.chooseUser;
    }

    @Override
    public Scene getScene() {
        return scene;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    private void confirm() {
        model.addUser(chosenUser.getText());
        stage.close();
    }

    @FXML
    private void cancel() {
        stage.close();
    }
}
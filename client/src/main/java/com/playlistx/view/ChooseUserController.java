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
    private ChoiceType type;
    private int chatID;
    private ArrayList<String> users = new ArrayList<>();
    @FXML
    private TextField chosenUser;

    private ChooseUserController(ChoiceType type, int chatID) throws RemoteException, NotBoundException {
        this.type = type;
        this.chatID = chatID;
    }

    protected static ChooseUserController get(ChoiceType type, int chatID) throws RemoteException, NotBoundException {
        if (instance == null) instance = new ChooseUserController(type, chatID);
        instance.type = type;
        instance.chatID = chatID;
        return instance;
    }

    @Override
    public void init(@NotNull Scene scene) {
        this.scene = scene;
        TextFields.bindAutoCompletion(chosenUser, users);
    }

    @Override
    public String getFXML() {
        return FXMLs.chooseUser;
    }

    @Override
    public Scene getScene() {
        //getUsers(!type.equals(ChoiceType.ADD));
        return scene;
    }

    protected void setStage(Stage stage) {
        this.stage = stage;
    }

    /*private void getUsers(boolean chatOnly) {
        users = model.getUsers(chatID, chatOnly);
        TextFields.bindAutoCompletion(chosenUser, users);
    }

    @FXML
    private void confirm() {
        String answer = chosenUser.getText();
        if (answer != null && !answer.isBlank()) {
            switch (type) {
                case ADD -> model.addUser(chatID, answer);
                case REMOVE -> model.removeUser(chatID, answer);
                case ADMIN -> model.setAdmin(chatID, answer);
            }
        }
        chosenUser.setText("");
        stage.close();
    }*/

    @FXML
    private void cancel() {
        stage.close();
    }

    public enum ChoiceType {
        ADD, REMOVE, ADMIN;
    }
}
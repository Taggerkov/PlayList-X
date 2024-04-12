package com.playlistx.viewmodel;

import com.playlistx.model.Model;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;

public class ChooseUserModel {
    private final Model model = Model.get();
    private static ChooseUserModel instance;

    private ChooseUserModel() throws RemoteException, NotBoundException {
    }

    public static ChooseUserModel get() throws RemoteException, NotBoundException {
        if (instance == null) instance = new ChooseUserModel();
        return instance;
    }

    public ArrayList<String> getUsers(int chatID, boolean chatOnly) {
        return null;
    }

    public void setAdmin(int chatID, String username) {

    }

    public void addUser(int chatID, String username) {

    }

    public void removeUser(int chatID, String username) {

    }
}
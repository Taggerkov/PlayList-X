package com.playlistx.model.proxy;

import com.playlistx.model.login.LoginException;
import org.jetbrains.annotations.Nullable;

import java.beans.PropertyChangeListener;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Client extends Remote {

    String login(String username, byte[] hashWord) throws RemoteException;

    boolean isAvailable(String username) throws RemoteException;

    boolean signUp(String username, byte[] hashWord) throws RemoteException;

    boolean changeUsername(String newUsername, byte[] hashWord) throws RemoteException, LoginException;

    boolean changePassword(byte[] oldPassword, byte[] newPassword) throws RemoteException, LoginException;

    boolean deleteUser(byte[] hashWord) throws RemoteException, LoginException;

    String[] getAllUsers() throws RemoteException;

    int totalUsers() throws RemoteException;

    String echo(@Nullable String msg) throws RemoteException;

    void close() throws RemoteException;

    void broadcast(String msg) throws RemoteException;

    void addListener(PropertyChangeListener listener) throws RemoteException;

    void removeListener(PropertyChangeListener listener) throws RemoteException;
}
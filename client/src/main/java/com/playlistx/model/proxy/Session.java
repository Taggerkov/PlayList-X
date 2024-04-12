package com.playlistx.model.proxy;

import dk.via.remote.observer.RemotePropertyChangeListener;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Session extends Remote {

    String getClient() throws RemoteException;

    void broadcast(String msg) throws RemoteException;

    void addListener(RemotePropertyChangeListener<String> listener) throws RemoteException;

    void removeListener(RemotePropertyChangeListener<String> listener) throws RemoteException;
}

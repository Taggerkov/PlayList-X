package com.playlistx.model.proxy;

import dk.via.remote.observer.RemotePropertyChangeListener;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * The Session interface is responsible for the proper RMI connection and handle the first layer session.
 *
 * @author Sergiu Chirap
 * @version final
 * @since 0.1
 */
public interface Session extends Remote {

    /**
     * Demands the server the creation of a personal session.
     * @return The ID of the second layer session.
     * @throws RemoteException RMI Connection Error
     */
    String getClient() throws RemoteException;

    /**
     * Fires a {@link dk.via.remote.observer.RemotePropertyChangeEvent remote event} to all the {@link RemotePropertyChangeListener remote listeners}.
     * @param msg The message attached to the event.
     * @throws RemoteException RMI Connection Error.
     */
    void broadcast(String msg) throws RemoteException;

    /**
     * Adds a {@link RemotePropertyChangeListener listener} to the {@link dk.via.remote.observer.RemotePropertyChangeSupport Observer pattern}.
     * @param listener The remote listener.
     * @throws RemoteException RMI Connection Error.
     */
    void addListener(RemotePropertyChangeListener<String> listener) throws RemoteException;

    /**
     * Removes a {@link RemotePropertyChangeListener listener} to the {@link dk.via.remote.observer.RemotePropertyChangeSupport Observer pattern}.
     * @param listener The remote listener.
     * @throws RemoteException RMI Connection Error.
     */
    void removeListener(RemotePropertyChangeListener<String> listener) throws RemoteException;
}
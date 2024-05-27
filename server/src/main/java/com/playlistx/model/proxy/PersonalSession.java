package com.playlistx.model.proxy;

import dk.via.remote.observer.RemotePropertyChangeListener;
import dk.via.remote.observer.RemotePropertyChangeSupport;
import org.jetbrains.annotations.NotNull;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDateTime;

/**
 * Remote class, handler of RMI first layer session.
 *
 * @author Sergiu Chirap
 * @version final
 * @see Session
 * @since 0.1
 */
public class PersonalSession extends UnicastRemoteObject implements Session, PropertyChangeListener {
    /**
     * Remote Observer Pattern trigger manager.
     */
    private final RemotePropertyChangeSupport<String> signal = new RemotePropertyChangeSupport<>();

    /**
     * Public Constructor.
     *
     * @throws RemoteException RMI Connection Error.
     */
    public PersonalSession() throws RemoteException {
    }

    /**
     * Demands the server the creation of a personal session.
     *
     * @return The ID of the second layer session.
     * @throws RemoteException RMI Connection Error
     */
    @Override
    public String getClient() throws RemoteException {
        String name = LocalDateTime.now().toString();
        Registry registry = LocateRegistry.getRegistry(1099);
        RMIClient client = new RMIClient();
        client.addListener(this);
        try {
            registry.bind(name, client);
        } catch (AlreadyBoundException e) {
            name += "DOUBLE-CHEESE";
            registry.rebind(name, UnicastRemoteObject.exportObject(new RMIClient(), 0));
        }
        System.out.println("New client registered!");
        return name;
    }

    /**
     * Fires a {@link dk.via.remote.observer.RemotePropertyChangeEvent remote event} to all the {@link RemotePropertyChangeListener remote listeners}.
     *
     * @param msg The message attached to the event.
     * @throws RemoteException RMI Connection Error.
     */
    @Override
    public void broadcast(String msg) throws RemoteException {
        signal.firePropertyChange("BROADCAST", null, msg);
    }

    /**
     * Adds a {@link RemotePropertyChangeListener listener} to the {@link dk.via.remote.observer.RemotePropertyChangeSupport Observer pattern}.
     *
     * @param listener The remote listener.
     * @throws RemoteException RMI Connection Error.
     */
    @Override
    public void addListener(RemotePropertyChangeListener<String> listener) throws RemoteException {
        signal.addPropertyChangeListener(listener);
    }

    /**
     * Removes a {@link RemotePropertyChangeListener listener} to the {@link dk.via.remote.observer.RemotePropertyChangeSupport Observer pattern}.
     *
     * @param listener The remote listener.
     * @throws RemoteException RMI Connection Error.
     */
    @Override
    public void removeListener(RemotePropertyChangeListener<String> listener) throws RemoteException {
        signal.addPropertyChangeListener(listener);
    }

    /**
     * This method gets called when a bound property is changed.
     *
     * @param evt A PropertyChangeEvent object describing the event source
     *            and the property that has changed.
     */
    @Override
    public void propertyChange(@NotNull PropertyChangeEvent evt) {
        try {
            broadcast((String) evt.getNewValue());
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
}
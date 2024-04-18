package com.playlistx.model.proxy;

import dk.via.remote.observer.RemotePropertyChangeListener;
import dk.via.remote.observer.RemotePropertyChangeSupport;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDateTime;

public class PersonalSession extends UnicastRemoteObject implements Session, PropertyChangeListener {
    private final RemotePropertyChangeSupport<String> signal = new RemotePropertyChangeSupport<>();

    public PersonalSession() throws RemoteException {
    }

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

    @Override
    public void broadcast(String msg) throws RemoteException {
        signal.firePropertyChange("BROADCAST", null, msg);
    }

    @Override
    public void addListener(RemotePropertyChangeListener<String> listener) throws RemoteException {
        signal.addPropertyChangeListener(listener);
    }

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
    public void propertyChange(PropertyChangeEvent evt) {
        try {
            broadcast((String) evt.getNewValue());
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
}
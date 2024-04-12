package com.playlistx.model.proxy;

import com.playlistx.model.ModelManager;
import dk.via.remote.observer.RemotePropertyChangeEvent;
import dk.via.remote.observer.RemotePropertyChangeListener;
import org.jetbrains.annotations.NotNull;

import java.beans.PropertyChangeSupport;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class RemoteListener extends UnicastRemoteObject implements RemotePropertyChangeListener<String> {
    private final PropertyChangeSupport signal = new PropertyChangeSupport(this);

    public RemoteListener(@NotNull ModelManager model, @NotNull Session session) throws RemoteException {
        model.listenTo(this);
        session.addListener(this);
    }

    public void addListener(ModelManager model) {
        signal.addPropertyChangeListener(model);
    }

    public void removeListener(ModelManager model, @NotNull Session session) {
        try {
            session.removeListener(this);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        signal.removePropertyChangeListener(model);
    }

    @Override
    public void propertyChange(@NotNull RemotePropertyChangeEvent<String> evt) throws RemoteException {
        signal.firePropertyChange(evt.getPropertyName(), evt.getOldValue(), evt.getNewValue());
    }
}
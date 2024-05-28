package com.playlistx.model.proxy;

import com.playlistx.model.ModelManager;
import dk.via.remote.observer.RemotePropertyChangeEvent;
import dk.via.remote.observer.RemotePropertyChangeListener;
import org.jetbrains.annotations.NotNull;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * This class is a {@link RemotePropertyChangeListener} implemented for a remote use of the Observer pattern.
 *
 * @author Sergiu Chirap
 * @version final
 * @see PropertyChangeListener
 * @see PropertyChangeSupport
 * @see dk.via.remote.observer.RemotePropertyChangeSupport RemotePropertyChangeSupport
 * @since 0.1
 */
public class RemoteListener extends UnicastRemoteObject implements RemotePropertyChangeListener<String> {
    /**
     * Observer Pattern trigger manager.
     */
    private final PropertyChangeSupport signal = new PropertyChangeSupport(this);

    /**
     * Class Constructor. Sets up related Observer pattern.
     *
     * @param model   Client's RMI model.
     * @param session RMI remote session.
     * @throws RemoteException RMI Connection Error.
     */
    public RemoteListener(@NotNull ModelManager model, @NotNull Session session) throws RemoteException {
        model.listenTo(this);
        session.addListener(this);
    }

    /**
     * Adds the {@link com.playlistx.model.Model Model} to this class {@link PropertyChangeSupport}.
     *
     * @param model The model to be added to this class support.
     */
    public void addListener(ModelManager model) {
        signal.addPropertyChangeListener(model);
    }

    /**
     * Removes the {@link com.playlistx.model.Model Model} from this class {@link PropertyChangeSupport}.
     *
     * @param model The model to be added to this class support.
     */
    public void removeListener(ModelManager model, @NotNull Session session) {
        try {
            session.removeListener(this);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        signal.removePropertyChangeListener(model);
    }

    /**
     * Fires a {@link RemotePropertyChangeEvent remote event} as a local {@link java.beans.PropertyChangeEvent event} to all its {@link PropertyChangeListener listeners}.
     *
     * @param evt The remote event.
     * @throws RemoteException RMI Connection Error.
     */
    @Override
    public void propertyChange(@NotNull RemotePropertyChangeEvent<String> evt) throws RemoteException {
        signal.firePropertyChange(evt.getPropertyName(), evt.getOldValue(), evt.getNewValue());
    }
}
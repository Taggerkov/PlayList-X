package com.playlistx.model;

import com.playlistx.model.login.LoginException;
import com.playlistx.model.proxy.Client;
import com.playlistx.model.proxy.RemoteListener;
import com.playlistx.model.proxy.Session;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ModelManager implements Model, PropertyChangeListener {
    private static ModelManager instance;
    private final Session session;
    private final Client client;
    private final RemoteListener remoteListener;
    private final PropertyChangeSupport signal = new PropertyChangeSupport(this);

    private ModelManager() throws RemoteException, NotBoundException {
        Registry registry = LocateRegistry.getRegistry(1099);
        session = (Session) registry.lookup("session");
        String clientLookUp = session.getClient();
        client = (Client) registry.lookup(clientLookUp);
        remoteListener = new RemoteListener(this, session);
    }

    public static Model get() throws RemoteException, NotBoundException {
        if (instance == null) instance = new ModelManager();
        return instance;
    }

    @Override
    public String login(String string, byte[] hashWord) throws RemoteException, LoginException {
        return client.login(string, hashWord);
    }

    @Override
    public boolean isAvailable(String generatedName) throws RemoteException {
        return client.isAvailable(generatedName);
    }

    @Override
    public void signUp(String string, byte[] hashWord) throws RemoteException {
        client.signUp(string, hashWord);
    }

    @Override
    public boolean changeUsername(String newUsername, byte[] hashWord) throws RemoteException {
        return client.changeUsername(newUsername, hashWord);
    }

    @Override
    public boolean changePassword(byte[] oldHashWord, byte[] newHashWord) throws RemoteException {
        return client.changePassword(oldHashWord, newHashWord);
    }

    @Override
    public boolean deleteUser(byte[] hashWord) throws RemoteException {
        return client.deleteUser(hashWord);
    }

    @Override
    public String[] getAllUsers() throws RemoteException {
        return client.getAllUsers();
    }

    @Override
    public int totalUsers() throws RemoteException {
        return client.totalUsers();
    }

    @Override
    public String echo(@Nullable String msg) throws RemoteException {
        return client.echo(msg);
    }

    @Override
    public void addListener(PropertyChangeListener listener) {
        signal.addPropertyChangeListener(listener);
    }

    @Override
    public void removeListener(PropertyChangeListener listener) {
        signal.removePropertyChangeListener(listener);
    }

    @Override
    public void listenTo(@NotNull RemoteListener toListen) {
        toListen.addListener(this);
    }

    @Override
    public void close() throws RemoteException {
        remoteListener.removeListener(this, session);
        client.removeListener(null);
        client.close();
    }

    /**
     * This method gets called when a bound property is changed.
     *
     * @param evt A PropertyChangeEvent object describing the event source
     *            and the property that has changed.
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        signal.firePropertyChange(evt);
    }
}
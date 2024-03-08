package com.playlistx.model.login;

import com.playlistx.model.utils.exceptions.InvalidInput;
import org.jetbrains.annotations.NotNull;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class User {
    private static final KeyChain keyChain = KeyChain.get();
    private final PropertyChangeSupport signal = new PropertyChangeSupport(this);
    private UserName userName;
    private int passwordHash;
    private HashKey logInHashKey;

    public User(PropertyChangeListener listener) {
        signal.addPropertyChangeListener(listener);
    }
    public void addListener(PropertyChangeListener listener) {
        signal.addPropertyChangeListener(listener);
    }
    public void removeListener(PropertyChangeListener listener) {
        signal.removePropertyChangeListener(listener);
    }

    public boolean login(@NotNull String userName, int passwordHash) {
        HashKey key = keyChain.getKey(userName);
        if (key != null) {
            if (key.equals(new HashKey(UserName.fresh(userName), passwordHash))) {
                setUser(key);
                return true;
            } else {
                signal.firePropertyChange("PASSWORD", null, null);
            }
        } else signal.firePropertyChange("USER", null, null);
        return false;
    }

    public void signUp(@NotNull String userName, int passwordHash) {
        try {
            KeyChain.get().addKey(new HashKey(UserName.fresh(userName), passwordHash));
            signal.firePropertyChange("SIGN", null, null);
        } catch (InvalidInput e) {
            signal.firePropertyChange("INV-USER", null, null);
        }
    }

    private void setUser(@NotNull HashKey key) {
        userName = key.getUserName();
        passwordHash = key.getHashPass();
        logInHashKey = key;
    }

    public UserName getUserName() {
        return userName;
    }

    public int getPasswordHash() {
        return passwordHash;
    }

    public void logout() {
        HashKey logOutHashKey = new HashKey(userName, passwordHash);
        if (!logOutHashKey.equals(logInHashKey)) keyChain.replaceKey(logOutHashKey, logInHashKey);
    }
}

package com.playlistx.model.login;

import com.playlistx.model.utils.exceptions.InvalidInput;
import org.jetbrains.annotations.NotNull;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * The {@code User} class holds the login information and is responsible for 'user' handling.
 * <p>
 * This class interacts with {@link KeyChain} class to create, delete and check user credentials, and makes
 * use of {@link UserName} class as for the users 'username'.
 * <p>
 * Class features {@link PropertyChangeSupport} which fires 'PropertyChange' to its listeners, this occurs in most of related changes or with invalid inputs,
 * such as credentials.
 *
 * @author Sergiu Chirap
 * @see KeyChain
 * @see UserName
 * @see java.beans.PropertyChangeSupport
 * @since 0.1
 */
public class User {
    /**
     * This class is used for credential storage.
     */
    private static final KeyChain keyChain = KeyChain.get();
    /**
     * This class is used for 'PropertyChange' handling.
     */
    private final PropertyChangeSupport signal = new PropertyChangeSupport(this);
    private UserName username;

    public User(PropertyChangeListener listener) {
        signal.addPropertyChangeListener(listener);
    }

    public boolean login(@NotNull String username, int hashWord) {
        UserName userName = UserName.fresh(username);
        int key = keyChain.getKey(userName);
        if (key != 0) {
            if (key == hashWord) {
                this.username = userName;
                return true;
            } else {
                signal.firePropertyChange("PASSWORD", null, null);
            }
        } else signal.firePropertyChange("USER", null, null);
        return false;
    }

    public void signUp(@NotNull String userName, int passwordHash) {
        try {
            KeyChain.get().registerKey(UserName.fresh(userName), passwordHash);
            signal.firePropertyChange("SIGN", null, null);
        } catch (InvalidInput e) {
            signal.firePropertyChange("INV-USER", null, null);
        }
    }

    public boolean isAvailable(String username) {
        return keyChain.isAvailable(username);
    }

    public UserName getUsername() {
        return username;
    }

    public void addListener(PropertyChangeListener listener) {
        signal.addPropertyChangeListener(listener);
    }

    public void removeListener(PropertyChangeListener listener) {
        signal.removePropertyChangeListener(listener);
    }

    public void logout() {
        for (PropertyChangeListener listener : signal.getPropertyChangeListeners())
            signal.removePropertyChangeListener(listener);
        signal.firePropertyChange("LOGOUT", this, null);
    }
}
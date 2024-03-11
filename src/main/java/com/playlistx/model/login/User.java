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
    /**
     * This class is responsible for the 'username' proper application.
     */
    private UserName username;

    /**
     * {@code User}'s constructor. Initializes a new 'user', which will carry non initialized credentials.
     * A {@code User} instance must call {@link #login(String, int)} for its proper function.
     * <p>
     * The {@code listener} is required due some methods relying on {@link java.beans.PropertyChangeEvent Event}s.
     *
     * @param listener A {@link java.beans.PropertyChangeListener}.
     */
    public User(PropertyChangeListener listener) {
        signal.addPropertyChangeListener(listener);
    }

    /**
     * Logs the 'user' in based on provided credentials. This method checks local saved users with {@link KeyChain}.
     * <p>
     * If no such 'user' exist, try calling {@link #signUp(String, int)} instead.
     * <p>
     * This method will fire the following {@link java.beans.PropertyChangeEvent Event}s:
     * <blockquote><pre>
     *     PASSWORD - When the provided {@code hashWord} doesn't match.
     *     USER - When such provided {@code username} wasn't found.
     * </pre></blockquote>
     *
     * @param username A {@link String} which will represent the 'username'.
     * @param hashWord An {@code int} which contains the hashed password.
     * @return A {@code boolean} which states if the login process was successful.
     */
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

    /**
     * Registers the 'user' with the provided credentials. This method locally saves with {@link KeyChain}.
     * <p>
     * After 'user' registration, proceed calling {@link #login(String, int)} for proper function.
     * <p>
     * This method will fire the following {@link java.beans.PropertyChangeEvent Event}s:
     * <blockquote><pre>
     *      SIGN - When the sign up process was successful.
     *      INV-USER - When the provided {@code username} is already registered.
     * </pre></blockquote>
     *
     * @param username A {@link String} which will represent the 'username'.
     * @param hashWord An {@code int} which contains the hashed password.
     */
    public void signUp(@NotNull String username, int hashWord) {
        try {
            KeyChain.get().registerKey(UserName.fresh(username), hashWord);
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
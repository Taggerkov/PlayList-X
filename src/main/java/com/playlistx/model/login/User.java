package com.playlistx.model.login;

import com.playlistx.model.utils.exceptions.InputException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* CLASS H-COUNTER: (Dear colleagues, please remember to add the hours contributed to this code!) 6.25h */

/**
 * The {@code User} class holds the login information and is responsible for 'user' handling.
 * <p>
 * This class interacts with {@link KeyChain} class to create, delete and check user credentials, and makes
 * use of {@link UserName} class as for the users 'username'.
 * <p>
 * Class features {@link java.beans.PropertyChangeSupport} which fires {@link java.beans.PropertyChangeEvent Events} to its listeners, this occurs in most of related changes or with invalid inputs,
 * such as credentials.
 * <p>
 * Some methods may throw a {@link LoginException}.
 *
 * @author Sergiu Chirap
 * @version 1.0
 * @see KeyChain
 * @see UserName
 * @see LoginException
 * @see java.beans.PropertyChangeSupport PropertyChangeSupport
 * @see java.beans.PropertyChangeEvent PropertyChangeEvent
 * @since 0.1
 */
public class User {
    /**
     * This {@link java.lang.String} stores the 'Hash Algorithm' error message.
     */
    private static final String ERROR_HASH = "password hashing being corrupted. Invalid hash algorithm!";
    /**
     * This {@link java.lang.String} contains the 'password' requirements as a regex.
     */
    private static final String REGEX_PASSWORD = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[-+_!@#$%^&*., ?]).+$";
    /**
     * This class is used for credential storage.
     */
    private final KeyChain keyChain;
    /**
     * This class is used for {@link java.beans.PropertyChangeEvent Event} handling.
     */
    private final PropertyChangeSupport signal = new PropertyChangeSupport(this);
    /**
     * This class is responsible for the 'username' proper application.
     */
    private UserName username;

    /**
     * {@code User}'s constructor. Initializes a new 'user', which will carry non initialized credentials.
     * A {@code User} instance must call {@link #login(String, String)} for its proper function.
     * <p>
     * The {@code listener} is required due some methods relying on {@link java.beans.PropertyChangeEvent Events}.
     *
     * @param listener A {@link java.beans.PropertyChangeListener}.
     * @throws LoginException An {@link java.lang.Exception} that occurs if users couldn't be loaded.
     */
    public User(@NotNull PropertyChangeListener listener) throws LoginException {
        keyChain = KeyChain.get();
        signal.addPropertyChangeListener(listener);
    }

    /**
     * Generates an available 'username'.
     *
     * @return A {@link java.lang.String} which represents the 'username'.
     */
    public static String genUsername() {
        return UserName.fresh(null).toString();
    }

    /**
     * Checks if an 'user' already owns the provided {@code username}.
     * This method locally checks with {@link KeyChain}.
     *
     * @param username A {@link java.lang.String String} which represents the 'username'.
     * @return A {@code boolean} which states the availability of the {@code username}.
     * @throws LoginException An {@link java.lang.Exception} that occurs if users couldn't be loaded.
     */
    public static boolean isAvailable(@NotNull String username) throws LoginException {
        return KeyChain.get().isAvailable(username);
    }

    /**
     * Checks if the provided {@code username} meets the requirements.
     *
     * @param username A {@link java.lang.String String} which represents the 'username'.
     * @return A {@code boolean} which states if {@code username} meets the requirements.
     */
    public static boolean checkUsername(String username) {
        try {
            UserName.fresh(username);
            return true;
        } catch (LoginException e) {
            return false;
        }
    }

    /**
     * Checks if the provided {@code password} meets the requirements.
     *
     * @param password A {@link java.lang.String String} which represents the 'password'.
     * @return A {@code boolean} which states if {@code username} meets the requirements.
     */
    public static boolean checkPassword(String password) {
        Pattern pattern = Pattern.compile(REGEX_PASSWORD);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches() && password.length() > 4;
    }

    /**
     * Gets total number of 'users' registered.
     *
     * @return An {@code int} which represents the total number of 'users'.
     */
    public static int totalUsers() {
        return KeyChain.get().size();
    }

    /**
     * Logs the 'user' in based on provided credentials. This method checks local saved users with {@link KeyChain}.
     * <p>
     * If no such 'user' exist, try calling {@link #signUp(String, String)} instead.
     * <p>
     * This method will fire the following {@link java.beans.PropertyChangeEvent Events}:
     * <blockquote><pre>
     *     LOGIN - When the login process was successful.
     *     LOGIN-USER - When such provided {@code username} wasn't found.
     *     LOGIN-PASSWORD - When the provided {@code hashWord} doesn't match.
     * </pre></blockquote>
     *
     * @param username A {@link java.lang.String} which represents the 'username'.
     * @param password An {@link java.lang.String} which represent the password.
     * @return A {@code boolean} which states if the login process was successful.
     */
    public boolean login(@NotNull String username, String password) {
        UserName userName = UserName.fresh(username);
        byte[] hashWord = toHashWord(password);
        byte[] key = keyChain.getKey(userName);
        if (key != null) {
            if (Arrays.equals(key, hashWord)) {
                this.username = userName;
                signal.firePropertyChange("LOGIN", null, null);
                return true;
            } else {
                signal.firePropertyChange("LOGIN-PASSWORD", null, null);
            }
        } else signal.firePropertyChange("LOGIN-USER", null, null);
        return false;
    }

    /**
     * Registers the 'user' with the provided credentials. This method locally saves with {@link KeyChain}.
     * <p>
     * After 'user' registration, proceed calling {@link #login(String, String)} for proper function.
     * <p>
     * This method will fire the following {@link java.beans.PropertyChangeEvent Events}:
     * <blockquote><pre>
     *      SIGNUP - When the sign-up process was successful.
     *      SIGNUP-USER - When the provided {@code username} is already registered.
     *      SIGNUP-PASSWORD - When the provided {@code password} doesn't meet requirements.
     * </pre></blockquote>
     *
     * @param username A {@link java.lang.String} which will represent the 'username'.
     * @param password An {@link java.lang.String} which will represents the 'password'.
     * @return A {@code boolean} which states if the sign-up process was successful.
     */
    public boolean signUp(@NotNull String username, String password) {
        if (checkPassword(password)) {
            byte[] hashWord = toHashWord(password);
            try {
                keyChain.registerKey(UserName.fresh(username), hashWord);
                signal.firePropertyChange("SIGNUP", null, null);
                return true;
            } catch (InputException e) {
                signal.firePropertyChange("SIGNUP-USER", null, null);
            }
        } else signal.firePropertyChange("SIGNUP-PASSWORD", null, null);
        return false;
    }

    /**
     * Changes users 'username'.
     * <p>
     * The 'password' is required for identification.
     *
     * @param newUsername A {@link java.lang.String} which will represent the new 'username'.
     * @param password    A {@link java.lang.String} which represents the 'password'.
     * @return A {@code boolean} which state if the operation was successful.
     */
    public boolean changeUsername(String newUsername, String password) {
        UserName userName = UserName.fresh(newUsername);
        if (keyChain.changeKey(this.username, userName, toHashWord(password))) {
            this.username = userName;
            return true;
        }
        return false;
    }

    /**
     * Changes users 'password'.
     * <p>
     * The current 'password' is required for identification.
     *
     * @param oldPassword A {@link java.lang.String} which represents the current 'password'.
     * @param newPassword A {@link java.lang.String} which will represent the new 'password'.
     * @return A {@code boolean} which state if the operation was successful.
     */
    public boolean changePassword(String oldPassword, String newPassword) {
        return keyChain.changeKeyValue(this.username, toHashWord(oldPassword), toHashWord(newPassword));
    }

    /**
     * Deletes the 'user'.
     * <p>
     * The 'password' is required for identification.
     *
     * @param password A {@link java.lang.String} which represents the 'password'.
     * @return A {@code boolean} which state if the operation was successful.
     */
    public boolean delete(String password) {
        return keyChain.deleteKey(this.username, toHashWord(password));
    }

    /**
     * Gets the {@code username} as a String.
     *
     * @return A {@link java.lang.String} which represents the 'username'.
     */
    public String getUsername() {
        return username.toString();
    }

    /**
     * Adds a listener to this instance.
     *
     * @param listener A {@link java.beans.PropertyChangeListener} which will be listening this instance.
     */
    public void addListener(@NotNull PropertyChangeListener listener) {
        signal.addPropertyChangeListener(listener);
    }

    /**
     * Removes a listener of this instance.
     *
     * @param listener A {@link java.beans.PropertyChangeListener} which will no longer be listening this instance.
     */
    public void removeListener(@Nullable PropertyChangeListener listener) {
        signal.removePropertyChangeListener(listener);
    }

    /**
     * Logs 'user' out.
     * <p>
     * Special call for user switching. It will clear all {@link java.beans.PropertyChangeListener PropertyChangeListeners}
     * and fire the following {@link java.beans.PropertyChangeEvent Event}:
     * <blockquote><pre>
     *      LOGOUT - When the 'user' is logged out.
     * </pre></blockquote>
     */
    public void logout() {
        for (PropertyChangeListener listener : signal.getPropertyChangeListeners()) removeListener(listener);
        signal.firePropertyChange("LOGOUT", this, null);
    }

    /**
     * Private method to transform a {@code password} from {@link java.lang.String} to SHA-512 hash as a {@code byte[]}.
     *
     * @param password A {@link java.lang.String} which represents the 'password'.
     * @return A {@code byte[]} which is the SHA-512 of the provided {@code password}.
     */
    private byte[] toHashWord(@NotNull String password) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA-512");
        } catch (NoSuchAlgorithmException e) {
            throw new LoginException(ERROR_HASH);
        }
        return md.digest(password.getBytes(StandardCharsets.UTF_8));
    }
}
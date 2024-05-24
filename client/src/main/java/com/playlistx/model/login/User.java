package com.playlistx.model.login;

import com.playlistx.model.Model;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* CLASS H-COUNTER: (Dear colleagues, please remember to add the hours contributed to this code!) 6.25h */

/**
 * The {@code User} class holds the login information and is responsible for 'user' handling.
 * <p>
 * This class interacts with the 'Server' to create, delete and check user credentials, and makes
 * use of {@link UserName} class as for the users 'username'.
 * <p>
 * Class features {@link PropertyChangeSupport} which fires {@link java.beans.PropertyChangeEvent Events} to its listeners, this occurs in most of related changes or with invalid inputs,
 * such as credentials.
 * <p>
 * Some methods may throw a {@link LoginException}.
 *
 * @author Sergiu Chirap
 * @version 1.0
 * @see UserName
 * @see LoginException
 * @see PropertyChangeSupport PropertyChangeSupport
 * @see java.beans.PropertyChangeEvent PropertyChangeEvent
 * @since 0.1
 */
public class User {
    /**
     * This {@link String} stores the 'Hash Algorithm' error message.
     */
    private static final String ERROR_HASH = "password hashing being corrupted. Invalid hash algorithm!";
    /**
     * This {@link String} contains the 'password' requirements as a regex.
     */
    private static final String REGEX_PASSWORD = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[-+_!@#$%^&*., ?]).+$";
    private static User instance;
    private final Model model = Model.get();
    /**
     * This class is used for {@link java.beans.PropertyChangeEvent Event} handling.
     */
    private final PropertyChangeSupport signal = new PropertyChangeSupport(this);
    /**
     * This class is responsible for the 'username' proper application.
     */
    private UserName username;
    private int ownerid;

    /**
     * Initializes a new 'user', which will carry non initialized credentials.
     * The {@code User} instance must call {@link #login(String, String)} for its proper function.
     * <p>
     * This is a private constructor, due being a {@code singleton}.
     * <p>
     * This method must only be called once.
     *
     * @throws LoginException An {@link Exception} that occurs if users couldn't be loaded.
     */
    private User() throws RemoteException, NotBoundException {
    }

    /**
     * Singleton {@code static} calling method.
     * <p>
     * Returns the current {@code instance} or a new {@code User} if none exists.
     *
     * @return A {@code User} singleton {@code instance}.
     * @throws LoginException An {@link Exception} that occurs if local users couldn't be loaded.
     */
    public static User get() throws RemoteException, NotBoundException {
        if (instance == null) instance = new User();
        return instance;
    }

    /**
     * Generates an available 'username'.
     *
     * @return A {@link String} which represents the 'username'.
     */
    public static String genUsername() {
        return UserName.fresh(null).toString();
    }

    /**
     * Checks if an 'user' already owns the provided {@code username}.
     * This method checks with the 'Server'.
     *
     * @param username A {@link String String} which represents the 'username'.
     * @return A {@code boolean} which states the availability of the {@code username}.
     * @throws LoginException An {@link Exception} that occurs if users couldn't be loaded.
     */
    public static boolean isAvailable(@NotNull String username) throws LoginException {
        try {
            return Model.get().isAvailable(username);
        } catch (IOException | NotBoundException e) {
            throw new LoginException(e.getMessage());
        }
    }

    /**
     * Checks if the provided {@code username} meets the requirements.
     *
     * @param username A {@link String String} which represents the 'username'.
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
     * @param password A {@link String String} which represents the 'password'.
     * @return A {@code boolean} which states if {@code username} meets the requirements.
     */
    public static boolean checkPassword(String password) {
        Pattern pattern = Pattern.compile(REGEX_PASSWORD);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches() && password.length() > 4;
    }

    public static String[] getAllUsers() throws LoginException{
        try {
            return Model.get().getAllUsers();
        } catch (IOException | NotBoundException e) {
            throw new LoginException(e.getMessage());
        }
    }

    /**
     * Gets total number of 'users' registered.
     *
     * @return An {@code int} which represents the total number of 'users'.
     */
    public static int totalUsers() throws LoginException {
        try {
            return Model.get().totalUsers();
        } catch (IOException | NotBoundException e) {
            throw new LoginException(e.getMessage());
        }
    }

    /**
     * Logs the 'user' in based on provided credentials. This method checks saved users with the 'Server'.
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
     * @param username A {@link String} which represents the 'username'.
     * @param password An {@link String} which represent the password.
     * @return A {@code boolean} which states if the login process was successful.
     */
    public boolean login(@NotNull String username, String password) throws LoginException {
        UserName userName;
        try {
            userName = UserName.fresh(username);
        } catch (LoginException ignore) {
            signal.firePropertyChange("LOGIN-USER", null, null);
            return false;
        }

        byte[] hashWord = toHashWord(password);
        try {
            String answer = model.login(userName.toString(), hashWord);
            signal.firePropertyChange(answer, null, null);
            if (answer.equalsIgnoreCase("LOGIN")) {
                this.username = userName;
                return true;
            } else return false;
        } catch (RemoteException e) {
            throw new LoginException(e.getMessage());
        }
    }

    /**
     * Registers the 'user' with the provided credentials. This method saves with the 'Server'.
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
     * @param username A {@link String} which will represent the 'username'.
     * @param password An {@link String} which will represents the 'password'.
     * @return A {@code boolean} which states if the sign-up process was successful.
     */
    public boolean signUp(@NotNull String username, String password) throws LoginException {
        if (checkPassword(password)) {
            UserName userName;
            try {
                userName = UserName.fresh(username);
            } catch (LoginException e) {
                signal.firePropertyChange("SIGNUP-USER", null, null);
                return false;
            }

            byte[] hashWord = toHashWord(password);
            try {
                model.signUp(userName.toString(), hashWord);
                signal.firePropertyChange("SIGNUP", null, null);
                return true;
            } catch (RemoteException e) {
                throw new LoginException(e.getMessage());
            }
        } else signal.firePropertyChange("SIGNUP-PASSWORD", null, null);
        return false;
    }

    /**
     * Changes users 'username'.
     * <p>
     * The 'password' is required for identification.
     *
     * @param newUsername A {@link String} which will represent the new 'username'.
     * @param password    A {@link String} which represents the 'password'.
     * @return A {@code boolean} which state if the operation was successful.
     */
    public boolean changeUsername(String newUsername, String password) throws LoginException {
        UserName userName = UserName.fresh(newUsername);
        try {
            if (model.changeUsername(userName.toString(), toHashWord(password))) {
                this.username = userName;
                return true;
            }
        } catch (RemoteException e) {
            throw new LoginException(e.getMessage());
        }
        return false;
    }

    /**
     * Changes users 'password'.
     * <p>
     * The current 'password' is required for identification.
     *
     * @param oldPassword A {@link String} which represents the current 'password'.
     * @param newPassword A {@link String} which will represent the new 'password'.
     * @return A {@code boolean} which state if the operation was successful.
     */
    public boolean changePassword(String oldPassword, String newPassword) {
        try {
            return model.changePassword(toHashWord(oldPassword), toHashWord(newPassword));
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Deletes the 'user'.
     * <p>
     * The 'password' is required for identification.
     *
     * @param password A {@link String} which represents the 'password'.
     * @return A {@code boolean} which state if the operation was successful.
     */
    public boolean delete(String password) {
        try {
            return model.deleteUser(toHashWord(password));
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Gets the {@code username} as a String.
     *
     * @return A {@link String} which represents the 'username'.
     */
    public String getUsername() {
        return username.toString();
    }

    /**
     * Adds a listener to this instance.
     *
     * @param listener A {@link PropertyChangeListener} which will be listening this instance.
     */
    public void addListener(@NotNull PropertyChangeListener listener) {
        signal.addPropertyChangeListener(listener);
    }

    /**
     * Removes a listener of this instance.
     *
     * @param listener A {@link PropertyChangeListener} which will no longer be listening this instance.
     */
    public void removeListener(@Nullable PropertyChangeListener listener) {
        signal.removePropertyChangeListener(listener);
    }

    /**
     * Logs 'user' out.
     * <p>
     * Special call for user switching. It will clear all {@link PropertyChangeListener PropertyChangeListeners}
     * and fire the following {@link java.beans.PropertyChangeEvent Event}:
     * <blockquote><pre>
     *      LOGOUT - When the 'user' is logged out.
     * </pre></blockquote>
     */
    public void logout() {
        signal.firePropertyChange("LOGOUT", this, null);
        for (PropertyChangeListener listener : signal.getPropertyChangeListeners()) removeListener(listener);
        try {
            model.close();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Private method to transform a {@code password} from {@link String} to SHA-512 hash as a {@code byte[]}.
     *
     * @param password A {@link String} which represents the 'password'.
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

    public int getOwnerId() {
        return this.ownerid;
    }
}
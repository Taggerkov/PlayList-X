package com.playlistx.model.proxy;

import com.playlistx.model.login.KeyChain;
import com.playlistx.model.login.LoginException;
import com.playlistx.model.login.UserName;
import org.jetbrains.annotations.Nullable;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

/**
 * Remote class, handler of RMI second layer.
 *
 * @author Sergiu Chirap, Raj
 * @version final
 * @since 0.1
 */
public class RMIClient extends UnicastRemoteObject implements Client {

    /**
     * Class log out message.
     */
    private static final String ERROR_LOGOUT = "LOGOUT";
    /**
     * Class no access message.
     */
    private static final String ERROR_ACCESS = "RESTRICTED";
    /**
     * Class users modification message.
     */
    private static final String BC_USERS = "USERS";
    /**
     * Credential manager.
     */
    private static final KeyChain keyChain = KeyChain.get();
    /**
     * Observer Pattern trigger manager.
     */
    private final PropertyChangeSupport signal = new PropertyChangeSupport(this);
    /**
     * Instance {@link PropertyChangeListener listener}.
     */
    private PropertyChangeListener session;
    /**
     * Login state check
     */
    private boolean login = false;
    /**
     * Users username. If login.
     */
    private UserName client = null;

    /**
     * Public Constructor.
     *
     * @throws RemoteException RMI Connection Error.
     * @author Sergiu Chirap
     */
    public RMIClient() throws RemoteException {
    }

    /**
     * Logs client in.
     * <blockquote>
     * <pre>
     *     "LOGIN-USER" -> Invalid username login
     *     "LOGIN-PASSWORD" -> Invalid password login
     *     "SIGNUP-USER" -> Invalid username signup
     *     "SIGNUP-PASSWORD" -> Invalid password signup
     *     "LOGIN" -> Successful login
     *     "SIGNUP" -> Successful password
     *     </pre>
     * </blockquote>
     *
     * @param username User username.
     * @param hashWord User password byte.
     * @return A String containing the result code of this process.
     * @throws RemoteException RMI Connection Error.
     * @author Sergiu Cirap
     */
    @Override
    public String login(String username, byte[] hashWord) throws RemoteException, LoginException {
        String answer;
        client = UserName.fresh(username);
        login = keyChain.checkCredentials(client, hashWord);
        if (login) {
            answer = "LOGIN";
            System.out.println("Client logged in as " + username);
        } else answer = "LOGIN-PASSWORD";
        return answer;
    }

    /**
     * Checks if a username is available in the services.
     *
     * @param username The username to check.
     * @return A boolean stating if is available.
     * @throws RemoteException RMI Connection Error.
     * @author Sergiu Chiap
     */
    @Override
    public boolean isAvailable(String username) throws RemoteException {
        return keyChain.isAvailable(username);
    }

    /**
     * Signs up a new user in the services.
     *
     * @param username New user username.
     * @param hashWord New user password bytes.
     * @return Operation success.
     * @throws RemoteException RMI Connection Error.
     * @author Sergiu Chirap
     */
    @Override
    public boolean signUp(String username, byte[] hashWord) throws RemoteException {
        return keyChain.registerKey(UserName.fresh(username), hashWord);
    }

    /**
     * Changes user username for a provided one.
     *
     * @param newUsername User new username.
     * @param hashWord    User password bytes.
     * @return Operation success.
     * @throws RemoteException RMI Connection Error.
     * @throws LoginException  User is not logged in.
     * @author Sergiu Chirap
     */
    @Override
    public boolean changeUsername(String newUsername, byte[] hashWord) throws RemoteException, LoginException {
        isLogin();
        return keyChain.changeKey(client, UserName.fresh(newUsername), hashWord);
    }

    /**
     * Changes user password for a provided one.
     *
     * @param oldPassword User password bytes.
     * @param newPassword User new password bytes.
     * @return Operation success.
     * @throws RemoteException RMI Connection Error.
     * @throws LoginException  User is not logged in.
     * @author Sergiu Chirap
     */
    @Override
    public boolean changePassword(byte[] oldPassword, byte[] newPassword) throws RemoteException, LoginException {
        isLogin();
        return keyChain.changeKeyValue(client, oldPassword, newPassword);
    }

    /**
     * Deletes user from the services.
     *
     * @param hashWord User password bytes.
     * @return Operation success.
     * @throws RemoteException RMI Connection Error.
     * @throws LoginException  User is not logged in.
     * @author Sergiu Chirap
     */
    @Override
    public boolean deleteUser(byte[] hashWord) throws RemoteException, LoginException {
        isLogin();
        broadcast(BC_USERS);
        return keyChain.deleteKey(client, hashWord);
    }

    /**
     * Gets all usernames registered in the service.
     *
     * @return All users usernames.
     * @throws RemoteException RMI Connection Error.
     * @author Sergiu Chirap
     */
    @Override
    public String[] getAllUsers() throws RemoteException {
        return keyChain.getAllUsers();
    }

    /**
     * Gets how many users are registered in the service.
     *
     * @return Users registered amount.
     * @throws RemoteException RMI Connection Error.
     * @author Sergiu Chirap
     */
    @Override
    public int totalUsers() throws RemoteException {
        return keyChain.size();
    }

    /**
     * Pings the server. An attached message is optional, else {@code null}.
     *
     * @param msg Optional message attached to ping.
     * @return The response from the server.
     * @throws RemoteException RMI Connection Error.
     * @author Sergiu Chirap
     */
    @Override
    public String echo(@Nullable String msg) throws RemoteException {
        try {
            return "echo - " + Objects.requireNonNull(msg) + " - " + LocalDateTime.now().truncatedTo(ChronoUnit.MILLIS).format(DateTimeFormatter.ofPattern("dd MMM uuuu"));
        } catch (NullPointerException e) {
            return "echo - " + LocalDateTime.now().truncatedTo(ChronoUnit.MILLIS).format(DateTimeFormatter.ofPattern("dd MMM uuuu"));
        }
    }

    /**
     * Successfully logs user out from session.
     *
     * @throws RemoteException RMI Connection Error.
     * @author Sergiu Chirap
     */
    @Override
    public void close() throws RemoteException {
        isLogin();
        System.out.println(client.toString() + " has left the session!");
        login = false;
        client = null;
        removeListener(session);
    }

    /**
     * Built-in prefab Observer pattern fire method.
     *
     * @param msg Message to be bundled with the {@link dk.via.remote.observer.RemotePropertyChangeEvent remote event}.
     * @throws RemoteException RMI Connection Error.
     * @author Sergiu Chirap
     * @see dk.via.remote.observer.RemotePropertyChangeSupport Remote Support
     * @see dk.via.remote.observer.RemotePropertyChangeListener Remote Listener
     */
    @Override
    public void broadcast(String msg) throws RemoteException {
        signal.firePropertyChange("ACTIVITY", null, msg);
    }

    /**
     * Adds a {@link dk.via.remote.observer.RemotePropertyChangeListener remote listener} to this instance.
     *
     * @param listener The listener to add to {@link dk.via.remote.observer.RemotePropertyChangeSupport remote support}
     * @throws RemoteException RMI Connection Error.
     * @author Sergiu Chirap
     * @see dk.via.remote.observer.RemotePropertyChangeEvent Remote Event
     */
    @Override
    public void addListener(PropertyChangeListener listener) throws RemoteException {
        this.session = listener;
        signal.addPropertyChangeListener(listener);
    }

    /**
     * Removes a {@link dk.via.remote.observer.RemotePropertyChangeListener remote listener} to this instance.
     *
     * @param listener The listener to remove from {@link dk.via.remote.observer.RemotePropertyChangeSupport remote support}
     * @throws RemoteException RMI Connection Error.
     * @author Sergiu Chirap
     * @see dk.via.remote.observer.RemotePropertyChangeEvent Remote Event
     */
    @Override
    public void removeListener(PropertyChangeListener listener) throws RemoteException {
        signal.removePropertyChangeListener(this.session);
    }

    /**
     * Internal check to verify users login status.
     *
     * @throws LoginException User is not logged in.
     * @author Sergiu Chirap
     */
    private void isLogin() throws LoginException {
        if (!login) throw new LoginException(ERROR_LOGOUT);
    }
}
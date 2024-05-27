package com.playlistx.model.proxy;

import com.playlistx.model.login.LoginException;
import org.jetbrains.annotations.Nullable;

import java.beans.PropertyChangeListener;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * The Client interface is responsible for the handling of the second layer session thus the server side model.
 *
 * @author Sergiu Chirap, Raj
 * @version final
 * @since 0.1
 */
public interface Client extends Remote {


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
     * @author Sergiu Chirap
     */
    String login(String username, byte[] hashWord) throws RemoteException;

    /**
     * Checks if a username is available in the services.
     *
     * @param username The username to check.
     * @return A boolean stating if is available.
     * @throws RemoteException RMI Connection Error.
     * @author Sergiu Chirap
     */
    boolean isAvailable(String username) throws RemoteException;

    /**
     * Signs up a new user in the services.
     *
     * @param username New user username.
     * @param hashWord New user password bytes.
     * @return Operation success.
     * @throws RemoteException RMI Connection Error.
     * @author Sergiu Chirap
     */
    boolean signUp(String username, byte[] hashWord) throws RemoteException;

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
    boolean changeUsername(String newUsername, byte[] hashWord) throws RemoteException, LoginException;

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
    boolean changePassword(byte[] oldPassword, byte[] newPassword) throws RemoteException, LoginException;

    /**
     * Deletes user from the services.
     *
     * @param hashWord User password bytes.
     * @return Operation success.
     * @throws RemoteException RMI Connection Error.
     * @throws LoginException  User is not logged in.
     * @author Sergiu Chirap
     */
    boolean deleteUser(byte[] hashWord) throws RemoteException, LoginException;

    /**
     * Gets all usernames registered in the service.
     *
     * @return All users usernames.
     * @throws RemoteException RMI Connection Error.
     * @author Sergiu Chirap
     */
    String[] getAllUsers() throws RemoteException;

    /**
     * Gets how many users are registered in the service.
     *
     * @return Users registered amount.
     * @throws RemoteException RMI Connection Error.
     * @author Sergiu Chirap
     */
    int totalUsers() throws RemoteException;

    /**
     * Pings the server. An attached message is optional, else {@code null}.
     *
     * @param msg Optional message attached to ping.
     * @return The response from the server.
     * @throws RemoteException RMI Connection Error.
     * @author Sergiu Chirap
     */
    String echo(@Nullable String msg) throws RemoteException;

    /**
     * Successfully logs user out from session.
     *
     * @throws RemoteException RMI Connection Error.
     * @author Sergiu Chirap
     */
    void close() throws RemoteException;

    /**
     * Built-in prefab Observer pattern fire method.
     *
     * @param msg Message to be bundled with the {@link dk.via.remote.observer.RemotePropertyChangeEvent remote event}.
     * @throws RemoteException RMI Connection Error.
     * @author Sergiu Chirap
     * @see dk.via.remote.observer.RemotePropertyChangeSupport Remote Support
     * @see dk.via.remote.observer.RemotePropertyChangeListener Remote Listener
     */
    void broadcast(String msg) throws RemoteException;

    /**
     * Adds a {@link dk.via.remote.observer.RemotePropertyChangeListener remote listener} to this instance.
     *
     * @param listener The listener to add to {@link dk.via.remote.observer.RemotePropertyChangeSupport remote support}
     * @throws RemoteException RMI Connection Error.
     * @author Sergiu Chirap
     * @see dk.via.remote.observer.RemotePropertyChangeEvent Remote Event
     */
    void addListener(PropertyChangeListener listener) throws RemoteException;

    /**
     * Removes a {@link dk.via.remote.observer.RemotePropertyChangeListener remote listener} to this instance.
     *
     * @param listener The listener to remove from {@link dk.via.remote.observer.RemotePropertyChangeSupport remote support}
     * @throws RemoteException RMI Connection Error.
     * @author Sergiu Chirap
     * @see dk.via.remote.observer.RemotePropertyChangeEvent Remote Event
     */
    void removeListener(PropertyChangeListener listener) throws RemoteException;
}
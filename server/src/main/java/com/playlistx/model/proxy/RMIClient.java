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

public class RMIClient extends UnicastRemoteObject implements Client {

    private static final String ERROR_LOGOUT = "LOGOUT";
    private static final String ERROR_ACCESS = "RESTRICTED";
    private static final String BC_USERS = "USERS";
    private static final String BC_CHATS = "CHATS";
    private static final String BC_MSG = "MSG";
    private static final KeyChain keyChain = KeyChain.get();
    private final PropertyChangeSupport signal = new PropertyChangeSupport(this);
    private PropertyChangeListener session;
    private boolean login = false;
    private UserName client = null;

    public RMIClient() throws RemoteException {
    }

    @Override
    public String login(String username, byte[] hashWord) throws RemoteException, LoginException {
        String answer;
        client = UserName.fresh(username);
        login = keyChain.checkCredentials(client, hashWord);
        if (login) {
            answer = "LOGIN";
            System.out.println("Client logged in as " + username);
        }
        else answer = "LOGIN-PASSWORD";
        return answer;
    }

    @Override
    public boolean isAvailable(String username) throws RemoteException {
        return keyChain.isAvailable(username);
    }

    @Override
    public boolean signUp(String username, byte[] hashWord) throws RemoteException {
        broadcast(BC_CHATS);
        return keyChain.registerKey(UserName.fresh(username), hashWord);
    }

    @Override
    public boolean changeUsername(String newUsername, byte[] hashWord) throws RemoteException, LoginException {
        isLogin();
        return keyChain.changeKey(client, UserName.fresh(newUsername), hashWord);
    }

    @Override
    public boolean changePassword(byte[] oldPassword, byte[] newPassword) throws RemoteException, LoginException {
        isLogin();
        return keyChain.changeKeyValue(client, oldPassword, newPassword);
    }

    @Override
    public boolean deleteUser(byte[] hashWord) throws RemoteException, LoginException {
        isLogin();
        broadcast(BC_USERS);
        return keyChain.deleteKey(client, hashWord);
    }

    @Override
    public String[] getAllUsers() throws RemoteException {
        return keyChain.getAllUsers();
    }

    @Override
    public int totalUsers() throws RemoteException {
        return keyChain.size();
    }

    @Override
    public String echo(@Nullable String msg) throws RemoteException {
        try {
            return "echo - " + Objects.requireNonNull(msg) + " - " + LocalDateTime.now().truncatedTo(ChronoUnit.MILLIS).format(DateTimeFormatter.ofPattern("dd MMM uuuu"));
        } catch (NullPointerException e) {
            return "echo - " + LocalDateTime.now().truncatedTo(ChronoUnit.MILLIS).format(DateTimeFormatter.ofPattern("dd MMM uuuu"));
        }
    }

    @Override
    public void close() throws RemoteException {
        isLogin();
        System.out.println(client.toString() + " has left the session!");
        login = false;
        client = null;
        removeListener(session);
    }

    @Override
    public void broadcast(String msg) throws RemoteException {
        signal.firePropertyChange("ACTIVITY", null, msg);
    }

    @Override
    public void addListener(PropertyChangeListener listener) throws RemoteException {
        this.session = listener;
        signal.addPropertyChangeListener(listener);
    }

    @Override
    public void removeListener(PropertyChangeListener listener) throws RemoteException {
        signal.removePropertyChangeListener(this.session);
    }

    private void isLogin() throws LoginException {
        if (!login) throw new LoginException(ERROR_LOGOUT);
    }
}
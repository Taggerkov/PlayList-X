package com.playlistx.model.login;

import com.playlistx.model.utils.FileHandler;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * The {@code KeyChain} is a serializable package-wide singleton class that takes charge of storing the credentials locally.
 * <p>
 * This class is an extension of {@link FileHandler}, being called by the 'Client' for client-side interaction. It reads {@link UserName} to ensure 'username' proper use.
 * <p>
 * Some methods may throw a {@link LoginException}.
 *
 * @author Sergiu Chirap
 * @version 1.0
 * @see KeyChain
 * @see UserName
 * @see LoginException
 * @see java.beans.PropertyChangeSupport PropertyChangeSupport
 * @see FileHandler
 * @see Serializable
 * @since 0.1
 */
public class KeyChain extends FileHandler implements Serializable {
    private static final String PATH = "server/src/main/resources/server/users.bin";
    /**
     * This {@link String} stores the 'Local I/O' error message.
     */
    private static final String ERROR_IO = "local users couldn't be loaded! Files may be outdated.";
    /**
     * This is the singleton {@code instance}.
     */
    private static KeyChain instance;
    /**
     * This {@link HashMap} is used for credential linking.
     */
    private HashMap<UserName, byte[]> keyMap = new HashMap<>();

    /**
     * Initializes the {@code instance} with local users.
     * <p>
     * This is a private constructor, due being a {@code singleton}.
     * <p>
     * This method must only be called once.
     *
     * @throws LoginException An {@link Exception} that occurs if local users couldn't be loaded.
     */
    private KeyChain() throws LoginException {
        try {
            if (checkFile(PATH)) {
                KeyChain extracted = (KeyChain) readFromBinary(PATH);
                keyMap = extracted.keyMap;
            } else save();
        } catch (Exception e) {
            throw new LoginException(ERROR_IO);
        }
    }

    /**
     * Singleton {@code static} calling method.
     * <p>
     * Returns the current {@code instance} or a new {@code KeyChain} if none exists.
     *
     * @return A {@code KeyChain} singleton {@code instance}.
     * @throws LoginException An {@link Exception} that occurs if local users couldn't be loaded.
     */
    public synchronized static KeyChain get() throws LoginException {
        if (instance == null) return instance = new KeyChain();
        return instance;
    }

    /**
     * Registers a new provided {@code key}. This method saves locally.
     *
     * @param username A {@link String} which represents the 'username'.
     * @param hashWord A {@code byte[]} which contains the hashed 'password'.
     */
    public synchronized boolean registerKey(@NotNull UserName username, byte[] hashWord) {
        if (!keyMap.containsKey(username)) {
            keyMap.put(username, hashWord);
            save();
            return true;
        } else return false;
    }

    public synchronized boolean checkCredentials(@NotNull UserName username, byte[] hashWord) throws LoginException {
        byte[] key = keyMap.get(username);
        if (key == null) throw new LoginException("LOGIN-USER");
        return Arrays.equals(keyMap.get(username), hashWord);
    }

    /**
     * Changes {@code key} variable maintaining the same linked {@code value}. Changes are saved locally.
     * <p>
     * The hashed 'password' is required for identification.
     *
     * @param oldUsername A {@link String} which represents the actual 'username'.
     * @param newUsername A {@link String} which represents the new 'username'.
     * @param hashWord    A {@code byte[]} which contains the hashed 'password'.
     */
    public synchronized boolean changeKey(@NotNull UserName oldUsername, @NotNull UserName newUsername, byte[] hashWord) {
        if (!keyMap.containsKey(newUsername) && keyMap.containsKey(oldUsername) && keyMap.remove(oldUsername, hashWord)) {
            keyMap.put(newUsername, hashWord);
            save();
            return true;
        }
        return false;
    }

    /**
     * Changes {@code key} variable maintaining the same linked {@code value}. Changes are saved locally.
     * <p>
     * The current hashed 'password' is required for identification.
     *
     * @param username    A {@link String} which represents the actual 'username'.
     * @param oldHashWord A {@code byte[]} which contains the current hashed 'password'.
     * @param newHashWord A {@code byte[]} which contains the new hashed 'password'.
     */
    public synchronized boolean changeKeyValue(@NotNull UserName username, byte[] oldHashWord, byte[] newHashWord) {
        if (keyMap.containsKey(username) && checkCredentials(username, oldHashWord)) {
            keyMap.put(username, newHashWord);
            save();
            return true;
        }
        return false;
    }

    /**
     * Deletes the provided {@code key}. Local storage affected.
     * <p>
     * The hashed 'password' is required for identification.
     *
     * @param username An {@link String} which represents the 'username'.
     * @param hashWord A {@code byte[]} which contains the hashed 'password'.
     */
    public synchronized boolean deleteKey(@NotNull UserName username, byte[] hashWord) {
        if (checkCredentials(username, hashWord)) {
            keyMap.remove(username);
            save();
            return true;
        }
        return false;
    }

    /**
     * Checks if the provided {@code key} is already registered.
     *
     * @param username An {@link String} which represents the 'username'.
     * @return A {@code boolean} which states if the {@code username} is available.
     */
    public synchronized boolean isAvailable(@NotNull String username) {
        for (UserName key : keyMap.keySet()) if (key.toString().equalsIgnoreCase(username)) return false;
        return true;
    }

    /**
     * Gets total number of {@code keys} registered.
     *
     * @return An {@code int} which represents the total number of 'users'.
     */
    public synchronized int size() {
        return keyMap.size();
    }

    public synchronized String[] getAllUsers() {
        ArrayList<String> answer = new ArrayList<>();
        for (UserName userName : keyMap.keySet()) answer.add(userName.toString());
        return answer.toArray(new String[0]);
    }

    /**
     * Locally saves itself as a binary file.
     */
    private void save() {
        writeToBinary(PATH, this);
    }
}
package com.playlistx.model.login;


import com.playlistx.model.paths.AppData;
import com.playlistx.model.utils.FileHandler;
import com.playlistx.model.utils.exceptions.InvalidFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.ArrayList;

public class KeyChain extends FileHandler implements Serializable {
    private final static String PATH = AppData.usersPath;
    private static KeyChain instance;
    private ArrayList<HashKey> passwordList = new ArrayList<>();

    private KeyChain() {
        try {
            if (checkFile(PATH)){
                KeyChain extracted = (KeyChain) readFromBinary(PATH);
                passwordList = extracted.passwordList;
            }
            else save();
        } catch (Exception e) {
            throw new InvalidFile("Couldn't read/write user file!");
        }
    }

    public static KeyChain get() {
        if (instance == null) return instance = new KeyChain();
        return instance;
    }

    public void setPasswordList(@NotNull ArrayList<HashKey> passwordList) {
        this.passwordList = passwordList;
    }

    public void addKey(@NotNull HashKey key) {
        if (!passwordList.contains(key)) {
            passwordList.add(key);
            save();
        }
    }

    public void deleteKey(@Nullable HashKey key) {
        passwordList.remove(key);
        save();
        replaceKey(null,null);
    }

    public void replaceKey(@NotNull HashKey addKey, @NotNull HashKey deleteKey) {
        addKey(addKey);
        deleteKey(deleteKey);
        save();
    }

    public @Nullable HashKey getKey(@Nullable String userName) {
        for (HashKey key : passwordList) {
            if (key.getUserName().equals(userName)) return key;
        }
        return null;
    }

    public boolean isAvailable(@NotNull String userName) {
        for (HashKey hashKey : passwordList) if (hashKey.getUserName().equals(userName)) return false;
        return true;
    }

    public int size() {
        return passwordList.size();
    }

    public void save() {
        writeToBinary(PATH, this);
    }
}

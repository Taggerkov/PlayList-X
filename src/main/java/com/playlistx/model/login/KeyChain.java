package com.playlistx.model.login;


import com.playlistx.model.paths.AppData;
import com.playlistx.model.utils.FileHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.HashMap;

class KeyChain extends FileHandler implements Serializable {
    private static KeyChain instance;
    private final String PATH;
    private HashMap<String, byte[]> keyMap = new HashMap<>();

    private KeyChain() {
        this.PATH = AppData.usersPath;
        try {
            if (checkFile(PATH)) {
                KeyChain extracted = (KeyChain) readFromBinary(PATH);
                keyMap = extracted.keyMap;
            } else save();
        } catch (Exception e) {
            throw new LoginException("Couldn't read/write user local file!");
        }
    }

    protected static KeyChain get() {
        if (instance == null) return instance = new KeyChain();
        return instance;
    }

    protected void registerKey(@NotNull UserName username, byte[] hashWord) {
        if (!keyMap.containsKey(username.toString())) {
            keyMap.put(username.toString(), hashWord);
            save();
        }
    }

    protected void addKey(@NotNull UserName username, byte[] hashWord) {
        keyMap.put(username.toString(), hashWord);
        save();
    }

    protected void deleteKey(@Nullable UserName username, int hashWord) {
        keyMap.remove(username.toString(), hashWord);
        save();
    }

    protected byte[] getKey(@Nullable UserName username) {
        return keyMap.get(username.toString());
    }

    protected boolean isAvailable(@NotNull String username) {
        for (String key : keyMap.keySet()) if (key.equalsIgnoreCase(username)) return false;
        return true;
    }

    protected int size() {
        return keyMap.size();
    }

    protected void save() {
        writeToBinary(PATH, this);
    }
}
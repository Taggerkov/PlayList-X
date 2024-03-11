package com.playlistx.model.login;


import com.playlistx.model.paths.AppData;
import com.playlistx.model.utils.FileHandler;
import com.playlistx.model.utils.exceptions.InvalidFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

class KeyChain extends FileHandler implements Serializable {
    private final static String PATH = AppData.usersPath;
    private static KeyChain instance;
    private HashMap<String, Integer> keyMap = new HashMap<>();

    private KeyChain() {
        try {
            if (checkFile(PATH)) {
                KeyChain extracted = (KeyChain) readFromBinary(PATH);
                keyMap = extracted.keyMap;
            } else save();
        } catch (Exception e) {
            throw new InvalidFile("Couldn't read/write user file!");
        }
    }

    public static KeyChain get() {
        if (instance == null) return instance = new KeyChain();
        return instance;
    }

    public void registerKey(@NotNull UserName username, int hashWord) {
        if (!keyMap.containsKey(username.toString())) {
            keyMap.put(username.toString(), hashWord);
            save();
        }
    }

    public void addKey(@NotNull UserName username, int hashWord) {
        keyMap.put(username.toString(), hashWord);
        save();
    }

    public void deleteKey(@Nullable UserName username, int hashWord) {
        keyMap.remove(username.toString(), hashWord);
        save();
    }

    public int getKey(@Nullable UserName username) {
        return keyMap.get(username.toString());
    }

    public boolean isAvailable(@NotNull String username) {
        for (String key : keyMap.keySet()) if (key.equalsIgnoreCase(username)) return false;
        return true;
    }

    public int size() {
        return keyMap.size();
    }

    public void save() {
        writeToBinary(PATH, this);
    }
}
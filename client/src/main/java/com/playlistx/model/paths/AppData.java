package com.playlistx.model.paths;

import com.playlistx.model.utils.exceptions.InputException;
import org.jetbrains.annotations.NotNull;

public enum AppData {
    USERS, TXT_SETTINGS;
    public static final String usersPath = "client/src/main/resources/app/chatty/data/users.bin";
    public static final String txtSettings = "client/TXTSettings.txt";

    public @NotNull String get() {
        switch (this) {
            case USERS -> {
                return usersPath;
            }
            case TXT_SETTINGS -> {
                return txtSettings;
            }
            default -> throw new InputException("No such file found!");
        }
    }
}
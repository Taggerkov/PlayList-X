package com.playlistx.model.paths;

import com.playlistx.model.utils.exceptions.InputException;
import org.jetbrains.annotations.NotNull;

public enum AppData {
    USERS;
    public static final String usersPath = "src/main/resources/com/playlistx/data/users.bin";

    public @NotNull String get() {
        switch (this) {
            case USERS -> {
                return usersPath;
            }
            default -> throw new InputException("No such file found!");
        }
    }
}

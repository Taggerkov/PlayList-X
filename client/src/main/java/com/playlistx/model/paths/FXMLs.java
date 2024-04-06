package com.playlistx.model.paths;

import com.playlistx.model.utils.exceptions.InputException;
import org.jetbrains.annotations.NotNull;

public enum FXMLs {
    LOGIN;
    public static final String loginPath = "/com/playlistx/fxml/login.fxml";
    public @NotNull String get() {
        switch (this) {
            case LOGIN -> {
                return loginPath;
            }
            default -> throw new InputException("No such fxml found!");
        }
    }
}

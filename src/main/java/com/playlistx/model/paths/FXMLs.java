package com.playlistx.model.paths;

import com.playlistx.model.utils.exceptions.InvalidInput;
import org.jetbrains.annotations.NotNull;

public enum FXMLs {
    LOGIN;
    public static final String loginPath = "/com/playlistx/fxml/login.fxml";
    public @NotNull String get() {
        switch (this) {
            case LOGIN -> {
                return loginPath;
            }
            default -> throw new InvalidInput("No such fxml found!");
        }
    }
}

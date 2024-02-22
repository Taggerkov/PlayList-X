package com.playlistx.utils;

import com.playlistx.utils.exceptions.FXMLException;
import org.jetbrains.annotations.NotNull;

public enum FXMLs {
    OPTION1;
    private static final String FXML_OPTION1 = "/open/stamp/fxml/(OPTION1).fxml";

    public @NotNull String get() {
        switch (this) {
            case OPTION1 -> {
                return FXML_OPTION1;
            }
            default -> throw new FXMLException();
        }
    }
}

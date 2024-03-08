package com.playlistx.model.paths;

import com.playlistx.model.utils.exceptions.InvalidInput;
import org.jetbrains.annotations.NotNull;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public enum CSS {
    LIGHT, DARK;
    public static final String lightPath = "/com/playlistx/css/light.css";
    public static final String darkPath = "/com/playlistx/css/dark.css";
    private static final String lightLogo = "file:src/main/resources/com/playlistx/img/logo-black.png";
    private static final String darkLogo = "file:src/main/resources/com/playlistx/img/logo-white.png";
    private static CSS CURRENT = DARK;

    private final PropertyChangeSupport signal = new PropertyChangeSupport(this);

    public static @NotNull CSS getCSS() {
        return CURRENT;
    }

    public static void setCSS(@NotNull CSS css) {
        switch (css) {
            case LIGHT -> {
                CURRENT = LIGHT;
                CURRENT.signal();
            }
            case DARK -> {
                CURRENT = DARK;
                CURRENT.signal();
            }
        }
    }

    public static @NotNull String getLogo() {
        switch (CURRENT) {
            case LIGHT -> {
                return lightLogo;
            }
            case DARK -> {
                return darkLogo;
            }
            default -> throw new InvalidInput("No such theme found!");
        }
    }

    public @NotNull String get() {
        switch (this) {
            case LIGHT -> {
                return lightPath;
            }
            case DARK -> {
                return darkPath;
            }
            default -> throw new InvalidInput("No such theme found!");
        }
    }

    private void signal() {
        signal.firePropertyChange("CSS", null, this);
    }

    public void addListener(PropertyChangeListener listener) {
        signal.addPropertyChangeListener(listener);
    }
}

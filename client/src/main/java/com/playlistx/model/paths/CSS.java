package com.playlistx.model.paths;

import org.jetbrains.annotations.NotNull;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public enum CSS {
    LIGHT, DARK;
    private final static CSSContainer light = new CSSContainer("/com/playlistx/css/light.css", "file:client/src/main/resources/com/playlistx/img/logo.png"),
            dark = new CSSContainer("/com/playlistx/css/dark.css", "file:client/src/main/resources/com/playlistx/img/logo.png");
    private static CSS CURRENT = DARK;
    private final PropertyChangeSupport signal = new PropertyChangeSupport(this);

    public static @NotNull CSS getCSS() {
        return CURRENT;
    }

    public static void setCSS(@NotNull CSS css) {
        switch (css) {
            case LIGHT -> {
                LIGHT.signal();
                CURRENT = LIGHT;
            }
            case DARK -> {
                DARK.signal();
                CURRENT = DARK;
            }
        }
    }

    public static @NotNull String path() {
        return CURRENT.getPath();
    }

    public static @NotNull String logo() {
        return CURRENT.getLogo();
    }

    public @NotNull String getPath() {
        return switch (this) {
            case LIGHT -> light.path();
            case DARK -> dark.path();
        };
    }

    public @NotNull String getLogo() {
        return switch (this) {
            case LIGHT -> light.logo();
            case DARK -> dark.logo();
        };
    }

    private void signal() {
        signal.firePropertyChange("CSS", CURRENT, this);
    }

    public void addListener(PropertyChangeListener listener) {
        signal.addPropertyChangeListener(listener);
    }

    private record CSSContainer(String path, String logo) {
    }
}
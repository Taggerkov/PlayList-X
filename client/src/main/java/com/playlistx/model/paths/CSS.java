package com.playlistx.model.paths;

import com.playlistx.view.ViewHandler;
import org.jetbrains.annotations.NotNull;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public enum CSS {
    LIGHT, DARK, CHERRY, THUNDER, PINKY;
    private final static CSSContainer light = new CSSContainer("/com/playlistx/css/light.css", "file:client/src/main/resources/com/playlistx/img/logo.png"),
            dark = new CSSContainer("/com/playlistx/css/dark.css", "file:client/src/main/resources/com/playlistx/img/logo.png"),
            cherry = new CSSContainer("/com/playlistx/css/cherry.css", "file:client/src/main/resources/com/playlistx/img/logo.png"),
    thunder = new CSSContainer("/com/playlistx/css/thunder.css", "file:client/src/main/resources/com/playlistx/img/logo.png"),
    pinky = new CSSContainer("/com/playlistx/css/pinky.css", "file:client/src/main/resources/com/playlistx/img/logo.png");
    private static final PropertyChangeSupport signal = new PropertyChangeSupport(CSS.class);
    private static CSS CURRENT = DARK;

    public static @NotNull CSS getCSS() {
        return CURRENT;
    }

    public static void setCSS(@NotNull CSS css) {
        switch (css) {
            case LIGHT -> {
                CURRENT = LIGHT;
                LIGHT.signal();
            }
            case DARK -> {
                CURRENT = DARK;
                DARK.signal();
            }
            case CHERRY -> {
                CURRENT = CHERRY;
                CHERRY.signal();
            }
            case THUNDER -> {
                CURRENT = THUNDER;
                THUNDER.signal();
            }
            case PINKY -> {
                CURRENT = PINKY;
                PINKY.signal();
            }
        }
    }

    public static @NotNull String path() {
        return CURRENT.getPath();
    }

    public static @NotNull String logo() {
        return CURRENT.getLogo();
    }

    public static void addListener(PropertyChangeListener listener) {
        signal.addPropertyChangeListener(listener);
    }

    public @NotNull String getPath() {
        return switch (this) {
            case LIGHT -> light.path();
            case DARK -> dark.path();
            case CHERRY -> cherry.path();
            case THUNDER -> thunder.path();
            case PINKY -> pinky.path();
        };
    }

    public @NotNull String getLogo() {
        return switch (this) {
            case LIGHT -> light.logo();
            case DARK -> dark.logo();
            case CHERRY -> cherry.logo();
            case THUNDER -> thunder.logo();
            case PINKY -> pinky.logo();
        };
    }

    private void signal() {
        signal.firePropertyChange("CSS", CURRENT, this);
        ViewHandler.get().reloadCSS();
    }

    private record CSSContainer(String path, String logo) {
    }
}
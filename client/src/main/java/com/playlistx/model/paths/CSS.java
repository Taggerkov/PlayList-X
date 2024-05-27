package com.playlistx.model.paths;

import com.playlistx.view.ViewHandler;
import org.jetbrains.annotations.NotNull;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * Enum holding all available CSSs and their respective paths while easing their use around the application.
 * @author Sergiu Chirap
 * @since 0.1
 * @version final
 */
public enum CSS {
    LIGHT, DARK, CHERRY, THUNDER, PINKY;
    /**
     * CSS Container that holds their paths and respective logos.
     * @see CSSContainer
     */
    private final static CSSContainer light = new CSSContainer("/com/playlistx/css/light.css", "file:client/src/main/resources/com/playlistx/img/logo.png"),
            dark = new CSSContainer("/com/playlistx/css/dark.css", "file:client/src/main/resources/com/playlistx/img/logo.png"),
            cherry = new CSSContainer("/com/playlistx/css/cherry.css", "file:client/src/main/resources/com/playlistx/img/logo.png"),
    thunder = new CSSContainer("/com/playlistx/css/thunder.css", "file:client/src/main/resources/com/playlistx/img/logo.png"),
    pinky = new CSSContainer("/com/playlistx/css/pinky.css", "file:client/src/main/resources/com/playlistx/img/logo.png");
    /**
     * Observer Pattern trigger manager.
     */
    private static final PropertyChangeSupport signal = new PropertyChangeSupport(CSS.class);
    /**
     * Current CSS variable.
     */
    private static CSS CURRENT = DARK;

    /**
     * Gets {@link #CURRENT}.
     * @return Current CSS variable.
     */
    public static @NotNull CSS getCSS() {
        return CURRENT;
    }

    /**
     * Sets {@link #CURRENT}.
     * @param css CSS to set as current.
     */
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

    /**
     * Gets the path of {@link #CURRENT}.
     * @return The path of the current CSS.
     */
    public static @NotNull String path() {
        return CURRENT.getPath();
    }

    /**
     * Gets the logo path of {@link #CURRENT}.
     * @return The logo path of the current CSS.
     */
    public static @NotNull String logo() {
        return CURRENT.getLogo();
    }

    /**
     * Gets the path of {@code this} CSS.
     * @return The path of {@code this} CSS.
     */
    public @NotNull String getPath() {
        return switch (this) {
            case LIGHT -> light.path();
            case DARK -> dark.path();
            case CHERRY -> cherry.path();
            case THUNDER -> thunder.path();
            case PINKY -> pinky.path();
        };
    }

    /**
     * Gets the logo path of {@code this} CSS.
     * @return The logo path of {@code this} CSS.
     */
    public @NotNull String getLogo() {
        return switch (this) {
            case LIGHT -> light.logo();
            case DARK -> dark.logo();
            case CHERRY -> cherry.logo();
            case THUNDER -> thunder.logo();
            case PINKY -> pinky.logo();
        };
    }

    /**
     * Fires a {@link java.beans.PropertyChangeEvent PropertyChangeEvent} to all the {@link PropertyChangeListener listeners}.
     */
    private void signal() {
        signal.firePropertyChange("CSS", CURRENT, this);
        ViewHandler.get().reloadCSS();
    }

    /**
     * Adds a {@link PropertyChangeListener} to this class {@link PropertyChangeSupport}.
     * @param listener The listener to be added to this class support.
     */
    public static void addListener(PropertyChangeListener listener) {
        signal.addPropertyChangeListener(listener);
    }

    /**
     * Private record Container Class to contain both CSS path and respective logo in the same variable.
     * @param path Path to its respective CSS.
     * @param logo Logo path to its CSS.
     */
    private record CSSContainer(String path, String logo) {
    }
}
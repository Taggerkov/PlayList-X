package com.playlistx.view;

import javafx.scene.Scene;
import org.jetbrains.annotations.NotNull;

/**
 * {@code Controller} classes are responsible for their {@code FXML} holding and function.
 * <br> All Controller classes are {@code Singleton}s.
 *
 * @author Sergiu Chirap
 * @version final
 * @since 0.6
 */
interface Controller {
    /**
     * Sets the {@link Scene} for this {@code Controller}.
     * <br> Not all {@code Controller}s needs a {@code scene}.
     *
     * @param scene The {@link Scene} of the class.
     */
    void init(@NotNull Scene scene);

    /**
     * Gets their respective {@code FXML} location.
     *
     * @return A {@code String} which contains the location of the respective {@code Controller}
     */
    String getFXML();

    /**
     * Gets their respective {@link Scene}.
     *
     * @return The {@link Scene} of the class.
     */
    Scene getScene();
}
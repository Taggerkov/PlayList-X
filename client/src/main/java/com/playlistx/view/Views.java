package com.playlistx.view;

/**
 * {@code Enum} class Views is used for easy access through all the UI.
 *
 * @author Sergiu Chirap
 * @version 1.0
 * @see java.lang.Enum
 * @since 0.4
 */
public enum Views {
    LOGIN, HOME, HOME_INIT, ALL_PLAYLISTS, PLAYLIST, SONGLIST, SONGLIST_SELECT;

    /**
     * A request to the {@link ViewHandler} to display the chosen {@link Views} will be sent.
     *
     * @see ViewHandler
     */
    public void show() {
        ViewHandler.get().display(this);
    }
}
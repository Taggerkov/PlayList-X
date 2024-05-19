package com.playlistx.view;

import java.util.Objects;

public enum Views {
    LOGIN, HOME, HOME_INIT, ALL_PLAYLISTS, PLAYLIST , SONGLIST, SONGLIST_SELECT , USER, SETTINGS;

    public void show() {
        try {
            Objects.requireNonNull(ViewHandler.get()).display(this);
        } catch (NullPointerException e) {
            ViewHandler.popUp(ViewHandler.Notify.ACCESS, "An error occurred while trying to display the view.");
        }
    }
}
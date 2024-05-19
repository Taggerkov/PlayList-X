package com.playlistx.view;

import java.util.Objects;

public enum Views {
    LOGIN, HOME, HOME_INIT, ALL_PLAYLISTS, PLAYLIST, SONGLIST, SONGLIST_SELECT, USER;

    public void show() {
        ViewHandler.get().display(this);
    }
}
package com.playlistx.model.music;
import java.util.List;

public interface Explorable {
    List<Song> exploreSongs();
    List<Playlist> explorePlaylists();

}

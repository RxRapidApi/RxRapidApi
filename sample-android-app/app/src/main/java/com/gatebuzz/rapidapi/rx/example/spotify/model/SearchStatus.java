package com.gatebuzz.rapidapi.rx.example.spotify.model;

public class SearchStatus {
    public static final int NOT_RUNNING = 0;
    public static final int SKIPPED = 1;
    public static final int STARTED = 2;
    public static final int FINISHED = 3;
    
    public int albums;
    public int artists;
    public int playlists;
    public int tracks;

    public SearchStatus(int albums, int artists, int playlists, int tracks) {
        this.albums = albums;
        this.artists = artists;
        this.playlists = playlists;
        this.tracks = tracks;
    }

    public SearchStatus() {
        this(NOT_RUNNING, NOT_RUNNING, NOT_RUNNING, NOT_RUNNING);
    }


    public void finishAlbums() {
        albums = FINISHED;
    }

    public void finishArtists() {
        albums = FINISHED;
    }

    public void finishPlaylists() {
        albums = FINISHED;
    }

    public void finishTracks() {
        albums = FINISHED;
    }

    public boolean isRunning() {
        return albums+artists+playlists+tracks > 0;
    }
}

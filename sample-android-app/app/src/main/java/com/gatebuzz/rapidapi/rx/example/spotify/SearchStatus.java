package com.gatebuzz.rapidapi.rx.example.spotify;

class SearchStatus {
    static final int NOT_RUNNING = 0;
    static final int SKIPPED = 1;
    static final int STARTED = 2;
    static final int FINISHED = 3;
    
    int albums;
    int artists;
    int playlists;
    int tracks;

    SearchStatus(int albums, int artists, int playlists, int tracks) {
        this.albums = albums;
        this.artists = artists;
        this.playlists = playlists;
        this.tracks = tracks;
    }

    SearchStatus() {
        this(NOT_RUNNING, NOT_RUNNING, NOT_RUNNING, NOT_RUNNING);
    }


    void finishAlbums() {
        albums = FINISHED;
    }

    void finishArtists() {
        albums = FINISHED;
    }

    void finishPlaylists() {
        albums = FINISHED;
    }

    void finishTracks() {
        albums = FINISHED;
    }

    boolean isRunning() {
        return albums+artists+playlists+tracks > 0;
    }
}

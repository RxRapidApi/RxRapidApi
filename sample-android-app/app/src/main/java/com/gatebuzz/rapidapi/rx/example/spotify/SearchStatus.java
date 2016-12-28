package com.gatebuzz.rapidapi.rx.example.spotify;

public class SearchStatus {
    public Boolean albums;
    public Boolean artists;
    public Boolean playlists;
    public Boolean tracks;

    public SearchStatus(Boolean albums, Boolean artists, Boolean playlists, Boolean tracks) {
        this.albums = albums;
        this.artists = artists;
        this.playlists = playlists;
        this.tracks = tracks;
    }

    public SearchStatus() {
        this(null, null, null, null);
    }
}

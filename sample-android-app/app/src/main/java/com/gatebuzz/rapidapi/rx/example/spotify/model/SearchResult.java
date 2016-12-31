package com.gatebuzz.rapidapi.rx.example.spotify.model;

import java.util.Collections;
import java.util.List;

public class SearchResult {
    public final List<Album> albums;
    public final List<Artist> artists;
    public final List<Playlist> playlists;
    public final List<Track> tracks;

    public SearchResult() {
        this(Collections.emptyList(), Collections.emptyList(), Collections.emptyList(), Collections.emptyList());
    }

    public SearchResult(List<Album> albums, List<Artist> artists, List<Playlist> playlists, List<Track> tracks) {
        this.albums = albums != null ? albums : Collections.emptyList();
        this.artists = artists != null ? artists : Collections.emptyList();
        this.playlists = playlists != null ? playlists : Collections.emptyList();
        this.tracks = tracks != null ? tracks : Collections.emptyList();
    }
}

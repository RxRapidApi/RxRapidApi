package com.gatebuzz.rapidapi.rx.example.spotify.model;

import android.util.Log;

import java.util.List;

public class SearchResult {
    private final List<Album> albumResult;
    private final List<Artist> artistResult;
    private final List<Playlist> playlistResult;
    private final List<Track> trackResult;

    public SearchResult(List<Album> albumResult, List<Artist> artistResult, List<Playlist> playlistResult, List<Track> trackResult) {
        this.albumResult = albumResult;
        this.artistResult = artistResult;
        this.playlistResult = playlistResult;
        this.trackResult = trackResult;

        Log.e("Example", "### Result: thread name=" + Thread.currentThread().getName() + " - id=" + Thread.currentThread().getId());
    }
}

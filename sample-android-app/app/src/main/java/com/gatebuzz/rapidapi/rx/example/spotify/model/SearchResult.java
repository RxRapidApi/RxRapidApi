package com.gatebuzz.rapidapi.rx.example.spotify.model;

import android.util.Log;

import java.util.List;

public class SearchResult {
    final List<Album> albumResult;
    final List<Artist> artistResult;
    final List<Playlist> playlistResult;
    final List<Track> trackResult;

    public SearchResult(List<Album> albumResult, List<Artist> artistResult, List<Playlist> playlistResult, List<Track> trackResult) {
        this.albumResult = albumResult;
        this.artistResult = artistResult;
        this.playlistResult = playlistResult;
        this.trackResult = trackResult;

        Log.e("Example", "### Result: thread name=" + Thread.currentThread().getName() + " - id=" + Thread.currentThread().getId());
    }
}

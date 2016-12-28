package com.gatebuzz.rapidapi.rx.example.spotify;

import java.util.Map;

public class SearchResult {
    public final Map<String, Object> albumResult;
    public final Map<String, Object> artistResult;
    public final Map<String, Object> playlistResult;
    public final Map<String, Object> trackResult;

    public SearchResult(Map<String, Object> albumResult, Map<String, Object> artistResult, Map<String, Object> playlistResult, Map<String, Object> trackResult) {
        this.albumResult = albumResult;
        this.artistResult = artistResult;
        this.playlistResult = playlistResult;
        this.trackResult = trackResult;
    }
}

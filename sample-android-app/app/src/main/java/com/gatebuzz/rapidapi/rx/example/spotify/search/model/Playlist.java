package com.gatebuzz.rapidapi.rx.example.spotify.search.model;

public class Playlist extends BaseSearchResult {
    public boolean collaborative;
    public PlaylistTrack tracks;
    public Owner owner;

    public static class Owner {
        public String id;
    }

    public static class PlaylistTrack {
        public String href;
        public int total;
    }
}

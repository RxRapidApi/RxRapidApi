package com.gatebuzz.rapidapi.rx.example.spotify.search.model;

import java.util.List;

public class Playlist extends BaseSearchResult {
    public boolean collaborative;
    public List<PlaylistTrack> tracks;
    public Owner owner;

    public static class Owner {
        public String id;
    }

    public static class PlaylistTrack {
        public String href;
        public int total;
    }
}

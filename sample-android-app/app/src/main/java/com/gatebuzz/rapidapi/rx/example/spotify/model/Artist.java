package com.gatebuzz.rapidapi.rx.example.spotify.model;

import java.util.List;

public class Artist extends BaseSearchResult {
    public int popularity;
    public Followers followers;
    public List<String> genres;

    public int getFollowers() {
        return followers != null ? followers.total : 0;
    }

    private class Followers {
        int total;
    }
}

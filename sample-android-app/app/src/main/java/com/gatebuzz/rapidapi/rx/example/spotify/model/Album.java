package com.gatebuzz.rapidapi.rx.example.spotify.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Album extends BaseSearchResult {
    public List<Artist> artists;
    @SerializedName("available_markets")
    public List<String> markets;
}

package com.gatebuzz.rapidapi.rx.example.spotify.search.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Track extends BaseSearchResult {
    public Album album;
    public List<Artist> artists;
    public boolean explicit;
    public int popularity;
    @SerializedName("track_number")
    public int trackNumber;
    @SerializedName("disc_number")
    public int discNumber;
    @SerializedName("duration_ms")
    public long durationInMilliseconds;
    @SerializedName("available_markets")
    public List<String> markets;

    @Override
    public Image smallestImage() {
        return album.smallestImage();
    }
}

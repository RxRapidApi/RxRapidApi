package com.gatebuzz.rapidapi.rx.example.spotify.model;

import android.util.Log;

import java.util.List;
import java.util.Map;

public class Artist {
    public final String id;
    public final String name;
    public final Double popularity;
    public final List<Image> images;
    public final int followers;

    @SuppressWarnings("unchecked")
    public Artist(Map<String, Object> serviceResult) {
        Log.e("Example", "A: " + serviceResult);
        id = (String) serviceResult.get("id");
        name = (String) serviceResult.get("name");
        popularity = (Double) serviceResult.get("popularity");
        images = Image.extractImages(serviceResult);
        Map<String, Object> followersMap = (Map<String, Object>) serviceResult.get("followers");
        followers = followersMap != null ? ((Double)followersMap.get("total")).intValue() : 0;
    }
}

package com.gatebuzz.rapidapi.rx.example.spotify.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class Album {
    public final String id;
    public final String name;
    public final List<Artist> artists;
    public final List<String> markets;
    public final List<Image> images;

    @SuppressWarnings("unchecked")
    public Album(Map<String, Object> serviceResult) {
        id = (String) serviceResult.get("id");
        name = (String) serviceResult.get("name");

        List<String> availableMarkets = (List<String>) serviceResult.get("available_markets");
        markets = (availableMarkets == null) ? Collections.emptyList() : new ArrayList<>(availableMarkets);

        List<Map<String, Object>> artistsAsMap = (List<Map<String, Object>>) serviceResult.get("artists");
        artists = new ArrayList<>();
        for (int i = 0; artistsAsMap != null && i < artistsAsMap.size(); i++) {
            artists.add(new Artist(artistsAsMap.get(i)));
        }

        images = Image.extractImages(serviceResult);
    }

}

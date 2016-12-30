package com.gatebuzz.rapidapi.rx.example.spotify.model;

import java.util.List;

public class Artist {
    public String id;
    public String name;
    public Double popularity;
    public List<Image> images;
    public Followers followers;

    private class Followers {
        int total;
    }
}

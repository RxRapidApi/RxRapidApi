package com.gatebuzz.rapidapi.rx.example.hackernews;

public class Story {
    public String by;
    public String url;
    public String title;
    public long time;

    public Story(String by, String url, String title, long time) {
        this.by = by;
        this.url = url;
        this.title = title;
        this.time = time;
    }
}

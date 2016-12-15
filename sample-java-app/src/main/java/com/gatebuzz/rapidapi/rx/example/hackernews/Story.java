package com.gatebuzz.rapidapi.rx.example.hackernews;

import java.util.Map;

public class Story {
    private String by;
    private int descendants;
    private long id;
    private double score;
    private String title;
    private String type;
    private String url;

    public Story(Map<String, Object> result) {
        this.by = (String) result.get("by");
        this.descendants = ((Double) result.get("descendants")).intValue();
        this.id = ((Double) result.get("id")).longValue();
        this.score = (Double) result.get("score");
        this.title = (String) result.get("title");
        this.type = (String) result.get("type");
        this.url = (String) result.get("url");
    }

    @Override
    public String toString() {
        return "Story: {\n" +
                "  by='" + by + '\'' +
                ", \n  descendants=" + descendants +
                ", \n  id=" + id +
                ", \n  score=" + score +
                ", \n  title='" + title + '\'' +
                ", \n  type='" + type + '\'' +
                ", \n  url='" + url + '\'' +
                "\n}\n";
    }
}

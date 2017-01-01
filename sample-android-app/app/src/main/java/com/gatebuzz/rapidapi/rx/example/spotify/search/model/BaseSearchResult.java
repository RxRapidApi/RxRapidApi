package com.gatebuzz.rapidapi.rx.example.spotify.search.model;

import java.util.List;

class BaseSearchResult {
    public String id;
    public String name;
    public List<Image> images;

    public Image smallestImage() {
        if (images == null || images.isEmpty()) {
            return null;
        }

        int w = Integer.MAX_VALUE;
        int index = -1;
        for (int i = 0; i < images.size(); i++) {
            Image image = images.get(i);
            if (image.width < w) {
                index = i;
                w = image.width;
            }
        }

        if (index == -1) {
            return null;
        }

        return images.get(index);
    }
}

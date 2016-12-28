package com.gatebuzz.rapidapi.rx.example.spotify.model;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Image {
    public final String url;
    public final int width;
    public final int height;

    public Image(Map<String, Object> serviceResult) {
        url = (String) serviceResult.get("url");
        width = ((Double)serviceResult.get("width")).intValue();
        height = ((Double)serviceResult.get("height")).intValue();
    }

    @NonNull
    @SuppressWarnings("unchecked")
    static List<Image> extractImages(Map<String, Object> serviceResult) {
        List<Map<String, Object>> imagesAsMap = (List<Map<String, Object>>) serviceResult.get("images");
        List<Image> list = new ArrayList<>();
        for (int i=0; imagesAsMap != null && i<imagesAsMap.size(); i++) {
            list.add(new Image(imagesAsMap.get(i)));
        }
        return list;
    }
}

package com.gatebuzz.rapidapi.rx.example.hackernews;

import com.gatebuzz.rapidapi.rx.ApiPackage;
import com.gatebuzz.rapidapi.rx.Named;
import com.gatebuzz.rapidapi.rx.Required;

import java.util.Map;

import rx.Observable;

@ApiPackage("HackerNews")
public interface HackerNewsApi {
    Observable<Map<String, Object>> getNewStories();

    Observable<Map<String, Object>> getBestStories();

    Observable<Map<String, Object>> getAskStories();

    Observable<Map<String, Object>> getShowStories();

    Observable<Map<String, Object>> getJobStories();

    Observable<Map<String, Object>> getUpdates();

    Observable<Map<String, Object>> getUser(@Required @Named("username") String username);

    Observable<Map<String, Object>> getItem(@Required @Named("itemId") Long item);
}

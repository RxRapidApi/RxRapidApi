package com.gatebuzz.rapidapi.rx.example;

import com.gatebuzz.rapidapi.rx.ApiPackage;
import com.gatebuzz.rapidapi.rx.Named;

import java.util.Map;

import rx.Single;

@ApiPackage("HackerNews")
public interface HackerNewsApi {
    Single<Map<String, Object>> getNewStories();

    Single<Map<String, Object>> getBestStories();

    Single<Map<String, Object>> getAskStories();

    Single<Map<String, Object>> getShowStories();

    Single<Map<String, Object>> getJobStories();

    Single<Map<String, Object>> getUpdates();

    Single<Map<String, Object>> getUser(@Named("username") String username);

    Single<Map<String, Object>> getItem(@Named("itemId") Long item);
}

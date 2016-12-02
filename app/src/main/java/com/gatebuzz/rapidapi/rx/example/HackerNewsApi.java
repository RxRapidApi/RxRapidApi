package com.gatebuzz.rapidapi.rx.example;

import com.gatebuzz.rapidapi.rx.ApiPackage;
import com.gatebuzz.rapidapi.rx.Application;
import com.gatebuzz.rapidapi.rx.Named;

import java.util.Map;

import rx.Observable;

@Application(project = BuildConfig.PROJECT, key = BuildConfig.API_KEY)
public interface HackerNewsApi {
    @ApiPackage("HackerNews")
    Observable<Map<String, Object>> getNewStories();

    @ApiPackage("HackerNews")
    Observable<Map<String, Object>> getBestStories();

    @ApiPackage("HackerNews")
    Observable<Map<String, Object>> getAskStories();

    @ApiPackage("HackerNews")
    Observable<Map<String, Object>> getShowStories();

    @ApiPackage("HackerNews")
    Observable<Map<String, Object>> getJobStories();

    @ApiPackage("HackerNews")
    Observable<Map<String, Object>> getUpdates();

    @ApiPackage("HackerNews")
    Observable<Map<String, Object>> getUser(
            @Named("username") String username
    );

    @ApiPackage("HackerNews")
    Observable<Map<String, Object>> getItem(
            @Named("itemId") String username
    );
}

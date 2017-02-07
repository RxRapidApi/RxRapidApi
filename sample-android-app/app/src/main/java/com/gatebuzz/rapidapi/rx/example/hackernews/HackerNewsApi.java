package com.gatebuzz.rapidapi.rx.example.hackernews;

import com.gatebuzz.rapidapi.rx.ApiPackage;
import com.gatebuzz.rapidapi.rx.Named;
import com.gatebuzz.rapidapi.rx.Required;

import java.util.List;
import java.util.Map;

import rx.Observable;

@ApiPackage("HackerNews")
public interface HackerNewsApi {
    //region Lists of categorized item ids
    Observable<List<Long>> getNewStories();

    Observable<List<Long>> getBestStories();

    Observable<List<Long>> getAskStories();

    Observable<List<Long>> getShowStories();

    Observable<List<Long>> getJobStories();

    Observable<List<Long>> getUpdates();
    // endregion

    Observable<Map<String, Object>> getUser(@Required @Named("username") String username);

    Observable<Map<String, Object>> getItem(@Required @Named("itemId") Long item);
}

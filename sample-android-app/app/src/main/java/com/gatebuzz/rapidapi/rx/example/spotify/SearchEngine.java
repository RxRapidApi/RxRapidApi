package com.gatebuzz.rapidapi.rx.example.spotify;

import android.support.annotation.NonNull;

import com.gatebuzz.rapidapi.rx.example.spotify.SpotifyApi.SearchResponseEnvelope;
import com.gatebuzz.rapidapi.rx.example.spotify.model.SearchResult;
import com.gatebuzz.rapidapi.rx.example.spotify.model.SearchStatus;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import rx.Observable;
import rx.Statement;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subjects.BehaviorSubject;

public class SearchEngine {
    private final BehaviorSubject<SearchStatus> statusSubject;
    private final BehaviorSubject<SearchResult> resultsSubject = BehaviorSubject.create();
    private final SpotifyApi spotifyApi;
    private SearchStatus currentSearch;

    public SearchEngine(SpotifyApi spotifyApi) {
        this.spotifyApi = spotifyApi;
        this.currentSearch = new SearchStatus();
        this.statusSubject = BehaviorSubject.create(currentSearch);
    }

    public Observable<SearchStatus> getStatus() {
        return statusSubject;
    }

    public Observable<SearchResult> getResults() {
        return resultsSubject;
    }

    public void clearStatus() {
        currentSearch = new SearchStatus();
        statusSubject.onNext(currentSearch);
    }

    public void startSearch(String search, boolean albums, boolean artists, boolean playlists, boolean tracks) {
        currentSearch = new SearchStatus(
                albums ? SearchStatus.STARTED : SearchStatus.SKIPPED,
                artists ? SearchStatus.STARTED : SearchStatus.SKIPPED,
                playlists ? SearchStatus.STARTED : SearchStatus.SKIPPED,
                tracks ? SearchStatus.STARTED : SearchStatus.SKIPPED);
        statusSubject.onNext(currentSearch);

        Observable.combineLatest(
                conditionallySearchFor(albums, spotifyApi.quickSearchAlbums(search), r -> currentSearch.finishAlbums()),
                conditionallySearchFor(artists, spotifyApi.quickSearchArtists(search), r -> currentSearch.finishArtists()),
                conditionallySearchFor(playlists, spotifyApi.quickSearchPlaylists(search), r -> currentSearch.finishPlaylists()),
                conditionallySearchFor(tracks, spotifyApi.quickSearchTracks(search), r -> currentSearch.finishTracks()),
                SearchResult::new)
                .subscribeOn(Schedulers.newThread())
                .doOnNext(searchResult -> clearStatus())
                .subscribe(resultsSubject::onNext);
    }

    @NonNull
    private <T extends SearchResponseEnvelope<R>, R> Observable<List<R>> conditionallySearchFor(
            boolean shouldRun, Observable<T> apiCall, Action1<T> statusUpdateAction) {
        Observable<List<R>> unwrappedApiData = apiCall
                .doOnNext(statusUpdateAction)
                .doOnNext(r -> statusSubject.onNext(currentSearch))
                .map(SearchResponseEnvelope::getData)
                .onErrorResumeNext(t -> Observable.just(Collections.emptyList()));

        return Statement.ifThen(() -> shouldRun, unwrappedApiData, Observable.just(Collections.<R>emptyList()));
    }
}

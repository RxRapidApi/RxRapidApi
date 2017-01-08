package com.gatebuzz.rapidapi.rx.example.spotify.search;

import android.support.annotation.NonNull;

import com.gatebuzz.rapidapi.rx.example.spotify.search.model.SearchResult;
import com.gatebuzz.rapidapi.rx.example.spotify.search.model.SearchStatus;

import java.util.Collections;
import java.util.List;

import rx.Observable;
import rx.Statement;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subjects.BehaviorSubject;

public class SearchEngine {
    private final BehaviorSubject<SearchStatus> statusSubject;
    private final BehaviorSubject<SearchResult> resultsSubject;
    private final SpotifySearchApi spotifyApi;
    private SearchStatus currentSearchStatus;
    private SearchResult currentSearchResult;

    public SearchEngine(SpotifySearchApi spotifyApi) {
        this.spotifyApi = spotifyApi;
        this.currentSearchStatus = new SearchStatus();
        this.currentSearchResult = new SearchResult();
        this.statusSubject = BehaviorSubject.create(currentSearchStatus);
        resultsSubject = BehaviorSubject.create(currentSearchResult);
    }

    Observable<SearchStatus> getStatus() {
        return statusSubject;
    }

    Observable<SearchResult> getResults() {
        return resultsSubject;
    }

    public SearchResult getSearchResult() {
        return currentSearchResult;
    }

    private void clearStatus() {
        currentSearchStatus = new SearchStatus();
        statusSubject.onNext(currentSearchStatus);
    }

    void startSearch(String search, boolean albums, boolean artists, boolean playlists, boolean tracks) {
        currentSearchResult = new SearchResult();
        resultsSubject.onNext(currentSearchResult);

        currentSearchStatus = new SearchStatus(
                albums ? SearchStatus.STARTED : SearchStatus.SKIPPED,
                artists ? SearchStatus.STARTED : SearchStatus.SKIPPED,
                playlists ? SearchStatus.STARTED : SearchStatus.SKIPPED,
                tracks ? SearchStatus.STARTED : SearchStatus.SKIPPED);
        statusSubject.onNext(currentSearchStatus);

        Observable.combineLatest(
                conditionallySearchFor(albums, spotifyApi.searchAlbums(search), r -> currentSearchStatus.finishAlbums()),
                conditionallySearchFor(artists, spotifyApi.searchArtists(search), r -> currentSearchStatus.finishArtists()),
                conditionallySearchFor(playlists, spotifyApi.searchPlaylists(search), r -> currentSearchStatus.finishPlaylists()),
                conditionallySearchFor(tracks, spotifyApi.searchTracks(search), r -> currentSearchStatus.finishTracks()),
                SearchResult::new)
                .subscribeOn(Schedulers.newThread())
                .doOnNext(searchResult -> clearStatus())
                .doOnNext(sr -> currentSearchResult = sr)
                .subscribe(resultsSubject::onNext);
    }

    @NonNull
    private <T extends SpotifySearchApi.SearchResponseEnvelope<R>, R> Observable<List<R>> conditionallySearchFor(
            boolean shouldRun, Observable<T> apiCall, Action1<T> statusUpdateAction) {
        Observable<List<R>> unwrappedApiData = apiCall
                .doOnNext(statusUpdateAction)
                .doOnNext(r -> statusSubject.onNext(currentSearchStatus))
                .map(SpotifySearchApi.SearchResponseEnvelope::getData)
                .onErrorResumeNext(t -> Observable.just(Collections.emptyList()));

        return Statement.ifThen(() -> shouldRun, unwrappedApiData, Observable.just(Collections.<R>emptyList()));
    }
}

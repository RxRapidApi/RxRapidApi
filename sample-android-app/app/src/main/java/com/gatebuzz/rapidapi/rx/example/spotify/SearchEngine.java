package com.gatebuzz.rapidapi.rx.example.spotify;

import android.util.Log;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subjects.BehaviorSubject;

import static rx.Statement.ifThen;

public class SearchEngine {
    private final BehaviorSubject<SearchStatus> statusSubject;
    private final BehaviorSubject<SearchResult> resultsSubject = BehaviorSubject.create();
    private final SpotifyApi spotifyApi;
    private SearchStatus currentSearch;

    public SearchEngine(SpotifyApi spotifyApi) {
        this.currentSearch = new SearchStatus();
        this.spotifyApi = spotifyApi;
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

        Log.e("Example", "### Before: thread name=" + Thread.currentThread().getName() + " - id=" + Thread.currentThread().getId());
        Observable.combineLatest(
                search(albums, spotifyApi.searchAlbums(search, null, null, null), currentSearch::finishAlbums),
                search(artists, spotifyApi.searchArtists(search, null, null, null), currentSearch::finishArtists),
                search(playlists, spotifyApi.searchPlaylists(search, null, null, null), currentSearch::finishPlaylists),
                search(tracks, spotifyApi.searchTracks(search, null, null, null), currentSearch::finishTracks),
                SearchResult::new)
                .doOnNext(searchResult -> clearStatus())
                .subscribe(resultsSubject::onNext);
    }

    private Observable<Map<String, Object>> search(boolean shouldRun, Observable<Map<String, Object>> serviceCall, final Action0 statusUpdateOnFinish) {
        Log.e("Example", "###  Setup: thread name=" + Thread.currentThread().getName() + " - id=" + Thread.currentThread().getId());
        Observable<Map<String, Object>> then = serviceCall
                .onErrorResumeNext(t -> Observable.just(new HashMap<>()))
                .doOnNext(r -> Log.e("Example", "###   Call: thread name=" + Thread.currentThread().getName() + " - id=" + Thread.currentThread().getId()))
                .doOnNext(r -> statusUpdateOnFinish.call())
                .doOnNext(r -> statusSubject.onNext(currentSearch));

        Observable<HashMap<String, Object>> orElse = Observable.just(new HashMap<>());

        return ifThen(() -> shouldRun, then, orElse);
    }

}

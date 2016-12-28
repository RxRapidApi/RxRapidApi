package com.gatebuzz.rapidapi.rx.example.spotify;

import android.support.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;
import rx.functions.Action1;
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
        currentSearch = new SearchStatus(albums, artists, playlists, tracks);
        statusSubject.onNext(currentSearch);

        Observable.combineLatest(
                ifThen(() -> albums, searchAndHandleErrors(spotifyApi.searchAlbums(search, null, null, null), o -> currentSearch.albums = false), empty()),
                ifThen(() -> artists, searchAndHandleErrors(spotifyApi.searchArtists(search, null, null, null), o -> currentSearch.artists = false), empty()),
                ifThen(() -> playlists, searchAndHandleErrors(spotifyApi.searchPlaylists(search, null, null, null), o -> currentSearch.playlists = false), empty()),
                ifThen(() -> tracks, searchAndHandleErrors(spotifyApi.searchTracks(search, null, null, null), o -> currentSearch.tracks = false), empty()),
                SearchResult::new)
                .doOnNext(searchResult -> clearStatus())
                .subscribe(resultsSubject::onNext);
    }

    @NonNull
    private Observable<HashMap<String, Object>> empty() {
        return Observable.just(new HashMap<>());
    }

    @NonNull
    private Observable<Map<String, Object>> searchAndHandleErrors(Observable<Map<String, Object>> searchObservable, Action1<Map<String, Object>> statusUpdate) {
        return searchObservable
                .subscribeOn(Schedulers.newThread())
                .onErrorResumeNext(t -> empty())
                .doOnNext(statusUpdate)
                .doOnNext(stringObjectMap -> statusSubject.onNext(currentSearch));
    }
}

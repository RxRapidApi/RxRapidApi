package com.gatebuzz.rapidapi.rx.example.spotify;

import com.gatebuzz.rapidapi.rx.example.spotify.model.Album;
import com.gatebuzz.rapidapi.rx.example.spotify.model.Artist;
import com.gatebuzz.rapidapi.rx.example.spotify.model.Playlist;
import com.gatebuzz.rapidapi.rx.example.spotify.model.SearchResult;
import com.gatebuzz.rapidapi.rx.example.spotify.model.SearchStatus;
import com.gatebuzz.rapidapi.rx.example.spotify.model.Track;
import com.gatebuzz.rapidapi.rx.utils.DrillDown;
import com.gatebuzz.rapidapi.rx.utils.SuccessMapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.functions.Action0;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subjects.BehaviorSubject;

import static rx.Statement.ifThen;

public class SearchEngine {
    private static final HashMap<String, Object> FAKE_RESULT = new HashMap<String, Object>() {{
        put("success", new HashMap<>());
    }};

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
                search(albums, spotifyApi.searchAlbums(search, null, null, null), currentSearch::finishAlbums, "albums", Album::new),
                search(artists, spotifyApi.searchArtists(search, null, null, null), currentSearch::finishArtists, "artists", Artist::new),
                search(playlists, spotifyApi.searchPlaylists(search, null, null, null), currentSearch::finishPlaylists, "playlists", Playlist::new),
                search(tracks, spotifyApi.searchTracks(search, null, null, null), currentSearch::finishTracks, "tracks", Track::new),
                SearchResult::new)
                .subscribeOn(Schedulers.newThread())
                .doOnNext(searchResult -> clearStatus())
                .subscribe(resultsSubject::onNext);
    }

    @SuppressWarnings("unchecked")
    private <T> Observable<List<T>> search(final boolean shouldRun,
                                           final Observable<Map<String, Object>> serviceCall,
                                           final Action0 statusUpdateOnFinish,
                                           final String resultName,
                                           final Func1<Map<String, Object>, T> transformer) {
        Observable<List<T>> then = serviceCall
                .onErrorResumeNext(throwable -> Observable.just(FAKE_RESULT))
                .doOnNext(r -> statusUpdateOnFinish.call())
                .doOnNext(r -> statusSubject.onNext(currentSearch))
                .map(new SuccessMapper())
                .map(new DrillDown(resultName))
                .flatMap(new ItemsMapper())
                .map(transformer).toList();

        Observable<List<T>> orElse = Observable.just(new ArrayList<>());

        return ifThen(() -> shouldRun, then, orElse);
    }

}

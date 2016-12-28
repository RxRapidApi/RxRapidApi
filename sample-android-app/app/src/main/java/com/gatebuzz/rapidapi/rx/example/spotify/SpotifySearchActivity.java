package com.gatebuzz.rapidapi.rx.example.spotify;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ViewFlipper;

import com.gatebuzz.rapidapi.rx.example.ExampleApplication;
import com.gatebuzz.rapidapi.rx.example.ManagedSubscriptionsActivity;
import com.gatebuzz.rapidapi.rx.example.R;
import com.gatebuzz.rapidapi.rx.example.spotify.model.SearchResult;
import com.gatebuzz.rapidapi.rx.example.spotify.model.SearchStatus;
import com.jakewharton.rxbinding.view.RxView;

import rx.android.schedulers.AndroidSchedulers;

public class SpotifySearchActivity extends ManagedSubscriptionsActivity {

    private SearchEngine searchEngine;

    public static void launch(Context context) {
        context.startActivity(new Intent(context, SpotifySearchActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spotify_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        searchEngine = ((ExampleApplication) getApplication()).getSpotifySearchEngine();
    }

    @Override
    protected void onResume() {
        super.onResume();
        subscriptions.addAll(
                RxView.clicks(findViewById(R.id.fab)).subscribe(v -> doSearch()),
                searchEngine.getStatus().observeOn(AndroidSchedulers.mainThread()).subscribe(this::displayStatus),
                searchEngine.getResults().observeOn(AndroidSchedulers.mainThread()).subscribe(this::displayResults)
        );
    }

    private void displayResults(SearchResult searchResult) {
    }

    private void displayStatus(SearchStatus searchStatus) {
        showSpinner(searchStatus.albums, R.id.album_progress, R.id.album_progress_icon, R.id.album_progress_spinner);
        showSpinner(searchStatus.artists, R.id.artists_progress, R.id.artists_progress_icon, R.id.artists_progress_spinner);
        showSpinner(searchStatus.playlists, R.id.playlists_progress, R.id.playlists_progress_icon, R.id.playlists_progress_spinner);
        showSpinner(searchStatus.tracks, R.id.tracks_progress, R.id.tracks_progress_icon, R.id.tracks_progress_spinner);
        findViewById(R.id.fab).setVisibility(searchStatus.isRunning() ? View.INVISIBLE : View.VISIBLE);
    }

    private void showSpinner(int value, int flipperId, int finishedId, int spinnerId) {
        ((ViewFlipper) findViewById(flipperId)).setDisplayedChild(value == SearchStatus.NOT_RUNNING ? 0 : 1);
        findViewById(spinnerId).setVisibility(value == SearchStatus.STARTED ? View.VISIBLE : value == SearchStatus.FINISHED ? View.GONE : View.INVISIBLE);
        findViewById(finishedId).setVisibility(value == SearchStatus.FINISHED ? View.VISIBLE : View.GONE);
    }

    private void doSearch() {
        searchEngine.startSearch(
                ((TextInputEditText) findViewById(R.id.search_terms)).getText().toString(),
                ((CheckBox) findViewById(R.id.albums_check)).isChecked(),
                ((CheckBox) findViewById(R.id.artists_check)).isChecked(),
                ((CheckBox) findViewById(R.id.playlists_check)).isChecked(),
                ((CheckBox) findViewById(R.id.tracks_check)).isChecked()
        );
    }
}

package com.gatebuzz.rapidapi.rx.example.spotify;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ViewFlipper;

import com.gatebuzz.rapidapi.rx.example.ExampleApplication;
import com.gatebuzz.rapidapi.rx.example.ManagedSubscriptionsActivity;
import com.gatebuzz.rapidapi.rx.example.R;
import com.jakewharton.rxbinding.view.RxView;

import rx.android.schedulers.AndroidSchedulers;

public class SpotifyMenuActivity extends ManagedSubscriptionsActivity {

    private SearchEngine searchEngine;

    public static void launch(Context context) {
        context.startActivity(new Intent(context, SpotifyMenuActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spotify_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        searchEngine = new SearchEngine(((ExampleApplication) getApplication()).getSpotifyApi());
        searchEngine.clearStatus();
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
        showSpinner(searchStatus.albums, R.id.album_progress, R.id.album_progress_spinner);
        showSpinner(searchStatus.artists, R.id.artists_progress, R.id.artists_progress_spinner);
        showSpinner(searchStatus.playlists, R.id.playlists_progress, R.id.playlists_progress_spinner);
        showSpinner(searchStatus.tracks, R.id.tracks_progress, R.id.tracks_progress_spinner);
    }

    private void showSpinner(Boolean value, int flipperId, int spinnerId) {
        ((ViewFlipper) findViewById(flipperId)).setDisplayedChild(value != null ? 1 : 0);
        findViewById(spinnerId).setVisibility(Boolean.TRUE.equals(value) ? View.VISIBLE : View.INVISIBLE);
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

package com.gatebuzz.rapidapi.rx.example.spotify.search;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.bumptech.glide.Glide;
import com.gatebuzz.rapidapi.rx.example.ExampleApplication;
import com.gatebuzz.rapidapi.rx.example.ManagedSubscriptionsActivity;
import com.gatebuzz.rapidapi.rx.example.R;
import com.gatebuzz.rapidapi.rx.example.spotify.search.model.Album;
import com.gatebuzz.rapidapi.rx.example.spotify.search.model.Artist;
import com.gatebuzz.rapidapi.rx.example.spotify.search.model.Image;
import com.gatebuzz.rapidapi.rx.example.spotify.search.model.Playlist;
import com.gatebuzz.rapidapi.rx.example.spotify.search.model.SearchResult;
import com.gatebuzz.rapidapi.rx.example.spotify.search.model.SearchStatus;
import com.gatebuzz.rapidapi.rx.example.spotify.search.model.Track;
import com.jakewharton.rxbinding.view.RxView;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

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
        LayoutInflater layoutInflater = LayoutInflater.from(this);

        LinearLayout container = (LinearLayout) findViewById(R.id.top_results);
        container.removeAllViews();

        if (!searchResult.albums.isEmpty()) {
            addTitle(layoutInflater, container, R.string.albums);
            int size = searchResult.albums.size();
            for (int i = 0; i < 3 && i < size; i++) {
                Album album = searchResult.albums.get(i);
                addRow(layoutInflater, container, album.name, album.artists.isEmpty(), album.artists.isEmpty() ? "" : album.artists.get(0).name, album.smallestImage());
            }
            if (size > 3) {
                addMore(layoutInflater, container, ResultsActivity.ALBUMS);
            }
        }

        if (!searchResult.artists.isEmpty()) {
            addTitle(layoutInflater, container, R.string.artists);
            int size = searchResult.artists.size();
            for (int i = 0; i < 3 && i < size; i++) {
                Artist artist = searchResult.artists.get(i);
                addRow(layoutInflater, container, artist.name, true, "", artist.smallestImage());
            }
            if (size > 3) {
                addMore(layoutInflater, container, ResultsActivity.ARTISTS);
            }
        }

        if (!searchResult.playlists.isEmpty()) {
            addTitle(layoutInflater, container, R.string.playlists);
            int size = searchResult.playlists.size();
            for (int i = 0; i < 3 && i < size; i++) {
                Playlist playlist = searchResult.playlists.get(i);
                addRow(layoutInflater, container, playlist.name, true, "", playlist.smallestImage());
            }
            if (size > 3) {
                addMore(layoutInflater, container, ResultsActivity.PLAYLISTS);
            }
        }

        if (!searchResult.tracks.isEmpty()) {
            addTitle(layoutInflater, container, R.string.tracks);
            int size = searchResult.tracks.size();
            for (int i = 0; i < 3 && i < size; i++) {
                Track track = searchResult.tracks.get(i);
                addRow(layoutInflater, container, track.name, track.artists.isEmpty(), track.artists.isEmpty() ? "" : track.artists.get(0).name, track.smallestImage());
            }
            if (size > 3) {
                addMore(layoutInflater, container, ResultsActivity.TRACKS);
            }
        }
    }

    private void addRow(LayoutInflater layoutInflater, LinearLayout container, String lineOneText, boolean lineTwoEmpty, String lineTwoText, Image smallest) {
        View row = layoutInflater.inflate(R.layout.search_result, container, false);
        TextView line1 = (TextView) row.findViewById(R.id.line_one);
        line1.setText(lineOneText);
        TextView line2 = (TextView) row.findViewById(R.id.line_two);
        line2.setVisibility(lineTwoEmpty ? View.INVISIBLE : View.VISIBLE);
        if (!lineTwoEmpty) {
            line2.setText(lineTwoText);
        }
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 0, 0, 32);
        container.addView(row, params);

        if (smallest != null) {
            Glide.with(this).load(smallest.url).into((ImageView) row.findViewById(R.id.result_image));
        }
    }

    private void addMore(LayoutInflater layoutInflater, LinearLayout container, @ResultsActivity.SearchType int searchType) {
        View moreView = layoutInflater.inflate(R.layout.search_more, container, false);
        subscriptions.add(RxView.clicks(moreView).subscribe(v -> ResultsActivity.launch(SpotifySearchActivity.this, searchType)));
        container.addView(moreView, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
    }

    private void addTitle(LayoutInflater layoutInflater, LinearLayout container, int titleTextId) {
        View title = layoutInflater.inflate(R.layout.search_title, container, false);
        ((TextView) title.findViewById(R.id.line_one)).setText(titleTextId);
        container.addView(title, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
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

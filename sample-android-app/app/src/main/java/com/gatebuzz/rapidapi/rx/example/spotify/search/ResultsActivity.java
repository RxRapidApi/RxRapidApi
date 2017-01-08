package com.gatebuzz.rapidapi.rx.example.spotify.search;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.gatebuzz.rapidapi.rx.example.ExampleApplication;
import com.gatebuzz.rapidapi.rx.example.ManagedSubscriptionsActivity;
import com.gatebuzz.rapidapi.rx.example.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class ResultsActivity extends ManagedSubscriptionsActivity {

    public static final int ALBUMS = 0;
    public static final int ARTISTS = 1;
    public static final int PLAYLISTS = 2;
    public static final int TRACKS = 3;
    private static final int[] TITLES = {
            R.string.albums, R.string.artists, R.string.playlists, R.string.tracks
    };

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({ALBUMS, ARTISTS, PLAYLISTS, TRACKS})
    @interface SearchType {}

    private int searchType;

    public static void launch(Context context, @SearchType int searchType) {
        Intent intent = new Intent(context, ResultsActivity.class);
        intent.putExtra("searchType", searchType);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        searchType = getIntent().getIntExtra("searchType", ALBUMS);
        CollapsingToolbarLayout toolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        toolbarLayout.setTitle(getString(TITLES[searchType]));

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.results);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(ResultsAdapterFactory.create(searchType, ((ExampleApplication)getApplication()).getSpotifySearchEngine()));
    }
}

package com.gatebuzz.rapidapi.rx.example;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.gatebuzz.rapidapi.rx.example.hackernews.HackerNewsActivity;
import com.gatebuzz.rapidapi.rx.example.nasa.NasaActivity;
import com.gatebuzz.rapidapi.rx.example.spotify.search.SpotifySearchActivity;
import com.jakewharton.rxbinding.view.RxView;

public class MainActivity extends ManagedSubscriptionsActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    protected void onResume() {
        super.onResume();
        subscriptions.addAll(
                RxView.clicks(findViewById(R.id.spotify)).subscribe(aVoid -> SpotifySearchActivity.launch(MainActivity.this)),
                RxView.clicks(findViewById(R.id.nasa)).subscribe(aVoid -> NasaActivity.launch(MainActivity.this)),
                RxView.clicks(findViewById(R.id.hackernews)).subscribe(aVoid -> HackerNewsActivity.launch(MainActivity.this))
        );
    }

}

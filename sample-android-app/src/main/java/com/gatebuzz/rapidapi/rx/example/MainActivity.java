package com.gatebuzz.rapidapi.rx.example;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.gatebuzz.rapidapi.rx.RxRapidApiBuilder;

import java.util.Map;

import rx.Single;
import rx.SingleSubscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.gatebuzz.rapidapi.rx.example.BuildConfig.API_KEY;
import static com.gatebuzz.rapidapi.rx.example.BuildConfig.PROJECT;

@SuppressWarnings("unused")
public class MainActivity extends AppCompatActivity {

    public static final String SPOTIFY_PUBLIC_API = "SpotifyPublicAPI";
    @SuppressWarnings("FieldCanBeLocal")
    private NasaApi nasaApi;
    @SuppressWarnings("FieldCanBeLocal")
    private SpotifyApi spotifyApi;
    @SuppressWarnings("FieldCanBeLocal")
    private ZillowApi zillowApi;
    @SuppressWarnings("FieldCanBeLocal")
    private HackerNewsApi hackerNewsApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Bare bones - no additional configuration needed
        zillowApi = RxRapidApiBuilder.from(ZillowApi.class);

        // ApiPackage at the class level to avoid noise
        nasaApi = new RxRapidApiBuilder()
                .endpoint(NasaApi.class)
                .build();

        // ApiPackage specified by the builder
        spotifyApi = new RxRapidApiBuilder()
                .endpoint(SpotifyApi.class)
                .apiPackage(SPOTIFY_PUBLIC_API)
                .build();

        // Builder supplied project/key for an interface that could be shared
        hackerNewsApi = new RxRapidApiBuilder()
                .application(PROJECT, API_KEY)
                .endpoint(HackerNewsApi.class)
                .build();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(view -> tryTheApi());
    }

    private void tryTheApi() {
        Single<Map<String, Object>> foo = hackerNewsApi.getBestStories();

        foo.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleSubscriber<Map<String, Object>>() {
                    @Override
                    public void onSuccess(Map<String, Object> value) {
                        Log.e("Example", "onSuccess - value = " + value);
                    }

                    @Override
                    public void onError(Throwable error) {
                        Log.e("Example", "onError", error);
                    }
                });
    }

}

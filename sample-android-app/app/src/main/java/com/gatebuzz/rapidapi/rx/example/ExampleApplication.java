package com.gatebuzz.rapidapi.rx.example;

import android.app.Application;

import com.gatebuzz.rapidapi.rx.RxRapidApiBuilder;
import com.gatebuzz.rapidapi.rx.example.spotify.SpotifyApi;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

import static com.gatebuzz.rapidapi.rx.example.BuildConfig.API_KEY;
import static com.gatebuzz.rapidapi.rx.example.BuildConfig.PROJECT;
import static com.gatebuzz.rapidapi.rx.example.BuildConfig.ZILLOW_API_KEY;

public class ExampleApplication extends Application {
    private static final String SPOTIFY_PUBLIC_API = "SpotifyPublicAPI";

    private NasaApi nasaApi;
    private ZillowApi zillowApi;
    private HackerNewsApi hackerNewsApi;
    private SpotifyApi spotifyApi;

    @Override
    public void onCreate() {
        super.onCreate();
        // Define the builder once, so that underlying OkHttp / gson can be shared across all
        // api services.
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        RxRapidApiBuilder apiBuilder = new RxRapidApiBuilder()
                .application(PROJECT, API_KEY)
                .okHttpClient(new OkHttpClient.Builder().addInterceptor(interceptor).build());

        // Pass a default Zillow Web Service Id parameter to every method
        zillowApi = apiBuilder
                .defaultValue("zwsId", ZILLOW_API_KEY)
                .endpoint(ZillowApi.class)
                .build();

        // ApiPackage at the class level to avoid noise
        nasaApi = apiBuilder.endpoint(NasaApi.class).build();

        // Builder supplied project/key for an interface that could be shared
        hackerNewsApi = apiBuilder.endpoint(HackerNewsApi.class).build();

        // Application & ApiPackage can be specified by the builder overriding the annotations
        spotifyApi = apiBuilder
                .endpoint(SpotifyApi.class)
                .apiPackage(SPOTIFY_PUBLIC_API)
                .build();
    }

    public NasaApi getNasaApi() {
        return nasaApi;
    }

    public ZillowApi getZillowApi() {
        return zillowApi;
    }

    public HackerNewsApi getHackerNewsApi() {
        return hackerNewsApi;
    }

    public SpotifyApi getSpotifyApi() {
        return spotifyApi;
    }
}

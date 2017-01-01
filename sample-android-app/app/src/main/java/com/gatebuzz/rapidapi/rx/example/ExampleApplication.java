package com.gatebuzz.rapidapi.rx.example;

import android.app.Application;

import com.gatebuzz.rapidapi.rx.RxRapidApiBuilder;
import com.gatebuzz.rapidapi.rx.example.hackernews.HackerNewsApi;
import com.gatebuzz.rapidapi.rx.example.nasa.NasaApi;
import com.gatebuzz.rapidapi.rx.example.spotify.search.SearchEngine;
import com.gatebuzz.rapidapi.rx.example.spotify.search.SpotifySearchApi;
import com.gatebuzz.rapidapi.rx.example.zillow.ZillowApi;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

import static com.gatebuzz.rapidapi.rx.example.BuildConfig.API_KEY;
import static com.gatebuzz.rapidapi.rx.example.BuildConfig.NASA_API_KEY;
import static com.gatebuzz.rapidapi.rx.example.BuildConfig.PROJECT;
import static com.gatebuzz.rapidapi.rx.example.BuildConfig.ZILLOW_API_KEY;

public class ExampleApplication extends Application {
    public static final String ZWS_ID = "zwsId";
    public static final String API_KEY = "apiKey";

    private NasaApi nasaApi;
    private ZillowApi zillowApi;
    private HackerNewsApi hackerNewsApi;
    private SearchEngine spotifySearchEngine;

    @Override
    public void onCreate() {
        super.onCreate();
        // Define the builder once, so that underlying OkHttp / Gson can be shared across all
        // api services.  Note: this is work you would normally do in your dependency injection
        // modules, declaring the dependencies as singletons.
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        RxRapidApiBuilder apiBuilder = new RxRapidApiBuilder()
                .application(PROJECT, BuildConfig.API_KEY)
                .okHttpClient(new OkHttpClient.Builder()
                        .connectTimeout(30, TimeUnit.SECONDS)
                        .readTimeout(30, TimeUnit.SECONDS)
                        .addInterceptor(interceptor).build());

        // Pass a default Zillow Web Service Id parameter to every method
        zillowApi = apiBuilder
                .defaultValue(ZWS_ID, ZILLOW_API_KEY)
                .endpoint(ZillowApi.class)
                .build();

        // Pass a default api token parameter to every method
        nasaApi = apiBuilder
                .defaultValue(API_KEY, NASA_API_KEY)
                .endpoint(NasaApi.class)
                .build();

        // No api keys needed
        hackerNewsApi = apiBuilder
                .endpoint(HackerNewsApi.class)
                .build();

        // Wrap the api in the search engine that uses it
        spotifySearchEngine = new SearchEngine(apiBuilder
                .endpoint(SpotifySearchApi.class)
                .build());
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

    public SearchEngine getSpotifySearchEngine() {
        return spotifySearchEngine;
    }
}

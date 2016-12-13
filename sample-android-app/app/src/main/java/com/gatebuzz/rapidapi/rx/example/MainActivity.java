package com.gatebuzz.rapidapi.rx.example;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.gatebuzz.rapidapi.rx.FailedCallException;
import com.gatebuzz.rapidapi.rx.RxRapidApiBuilder;

import java.util.Map;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.gatebuzz.rapidapi.rx.example.BuildConfig.API_KEY;
import static com.gatebuzz.rapidapi.rx.example.BuildConfig.PROJECT;
import static com.gatebuzz.rapidapi.rx.example.BuildConfig.ZILLOW_API_KEY;

@SuppressWarnings("unused")
public class MainActivity extends AppCompatActivity {

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

        // Pass a default Zillow Web Service Id parameter to every method
        zillowApi = new RxRapidApiBuilder()
                .application(PROJECT, API_KEY)
                .defaultValue("zwsId", ZILLOW_API_KEY)
                .endpoint(ZillowApi.class)
                .build();

        // ApiPackage at the class level to avoid noise
        nasaApi = new RxRapidApiBuilder()
                .endpoint(NasaApi.class)
                .build();

        // ApiPackage specified by the builder
        spotifyApi = new RxRapidApiBuilder()
                .endpoint(SpotifyApi.class)
                .apiPackage("SpotifyPublicAPI")
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
        Observable<Map<String, Object>> foo = zillowApi.getDeepSearchResults("", "2039 Yale Avenue", "63143");

        foo.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Map<String, Object>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("RxRapidApi", "Error calling service", e);
                        if (e instanceof FailedCallException) {
                            Log.e("RxRapidApi", "Error response:" + ((FailedCallException) e).getResponse());
                        }
                    }

                    @Override
                    public void onNext(Map<String, Object> result) {
                        Log.e("RxRapidApi", "Service result:" + result);
                    }
                });
    }

}

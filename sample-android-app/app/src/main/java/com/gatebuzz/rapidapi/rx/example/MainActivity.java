package com.gatebuzz.rapidapi.rx.example;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.gatebuzz.rapidapi.rx.FailedCallException;
import com.gatebuzz.rapidapi.rx.example.spotify.search.SpotifySearchActivity;
import com.jakewharton.rxbinding.view.RxView;

import java.util.Map;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

@SuppressWarnings("unused")
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
                RxView.clicks(findViewById(R.id.fab)).subscribe(x -> tryTheApi())
        );
    }

    private void tryTheApi() {
        ZillowApi zillowApi = ((ExampleApplication) getApplication()).getZillowApi();

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

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

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tryTheApi();
            }
        });
    }

    private void tryTheApi() {
        Single<Map<String, Object>> foo = RxRapidApiBuilder.from(HackerNewsApi.class).getBestStories();

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

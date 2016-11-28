package com.gatebuzz.rapidapi.rx.example;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.gatebuzz.rapidapi.rx.FailedCallException;
import com.gatebuzz.rapidapi.rx.RxRapidApiBuilder;

import java.util.Map;

import rx.Subscriber;
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
        RxRapidApiBuilder.from(ZillowApi.class).getSearchResults("", BuildConfig.ZILLOW_API_KEY, "2039 yale avenue", "63143")
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Map<String, Object>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e instanceof FailedCallException) {
                            Map<String, Object> response = ((FailedCallException) e).getResponse();
                            Log.e("Example", "Call failed: " + response);
                        } else {
                            Log.e("Example", "Call failed badly.", e);
                        }
                    }

                    @Override
                    public void onNext(Map<String, Object> response) {
                        Log.e("Example", "Call success: " + response.get("success"));
                        Log.e("Example", "type: " + response.get("success").getClass().getSimpleName());
                    }
                });
    }

}

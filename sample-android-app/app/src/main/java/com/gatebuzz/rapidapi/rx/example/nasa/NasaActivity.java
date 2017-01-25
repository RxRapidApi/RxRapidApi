package com.gatebuzz.rapidapi.rx.example.nasa;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gatebuzz.rapidapi.rx.example.ExampleApplication;
import com.gatebuzz.rapidapi.rx.example.R;

import java.util.Map;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class NasaActivity extends AppCompatActivity {

    public static void launch(Context context) {
        context.startActivity(new Intent(context, NasaActivity.class));
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nasa);
        ((ExampleApplication) getApplication()).getNasaApi()
                .getPictureOfTheDay(null, null)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(stringObjectMap -> {
                    Map<String, Object> successMap = (Map<String, Object>) stringObjectMap.get("success");
                    Glide.with(NasaActivity.this).load((String) successMap.get("url")).into((ImageView) findViewById(R.id.image));
                    ((TextView)findViewById(R.id.title)).setText((String)successMap.get("title"));
                    ((TextView)findViewById(R.id.description)).setText((String)successMap.get("explanation"));
                    ((TextView)findViewById(R.id.copyright)).setText((String)successMap.get("copyright"));
                    findViewById(R.id.copyright_container).setVisibility(View.VISIBLE);
                    findViewById(R.id.progress).setVisibility(View.GONE);
                });
    }
}

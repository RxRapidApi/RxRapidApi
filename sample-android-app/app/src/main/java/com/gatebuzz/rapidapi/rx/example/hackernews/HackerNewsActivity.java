package com.gatebuzz.rapidapi.rx.example.hackernews;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.gatebuzz.rapidapi.rx.example.R;

public class HackerNewsActivity extends AppCompatActivity {

    public static void launch(Context context) {
        context.startActivity(new Intent(context, HackerNewsActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hacker_news);
    }
}

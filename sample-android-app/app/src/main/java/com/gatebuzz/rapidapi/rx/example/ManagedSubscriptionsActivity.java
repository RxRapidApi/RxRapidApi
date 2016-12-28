package com.gatebuzz.rapidapi.rx.example;


import android.support.v7.app.AppCompatActivity;

import rx.subscriptions.CompositeSubscription;

public abstract class ManagedSubscriptionsActivity extends AppCompatActivity {
    protected CompositeSubscription subscriptions;

    @Override
    protected void onResume() {
        super.onResume();
        subscriptions = new CompositeSubscription();
    }

    @Override
    protected void onPause() {
        subscriptions.unsubscribe();
        super.onPause();
    }
}

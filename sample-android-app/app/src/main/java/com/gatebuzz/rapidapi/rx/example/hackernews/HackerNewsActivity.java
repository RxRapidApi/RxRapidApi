package com.gatebuzz.rapidapi.rx.example.hackernews;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gatebuzz.rapidapi.rx.example.ExampleApplication;
import com.gatebuzz.rapidapi.rx.example.R;
import com.gatebuzz.rapidapi.rx.example.spotify.search.SearchResultViewHolder;
import com.gatebuzz.rapidapi.rx.utils.SuccessMapper;

import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class HackerNewsActivity extends AppCompatActivity {

    public static void launch(Context context) {
        context.startActivity(new Intent(context, HackerNewsActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hacker_news);
        loadTopFiveNewStories();
    }

    private void loadTopFiveNewStories() {
        HackerNewsApi api = ((ExampleApplication) getApplication()).getHackerNewsApi();
        api.getNewStories()
                .subscribeOn(Schedulers.newThread())
                .flatMap(Observable::from)
                .take(5)
                .flatMap(api::getItem)
                .map(new SuccessMapper<Map<String, Object>>())
                .toList()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::displayStories);
    }

    private void displayStories(final List<Map<String, Object>> stories) {
        findViewById(R.id.progress).setVisibility(View.GONE);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.results);
        recyclerView.setLayoutManager(new LinearLayoutManager(HackerNewsActivity.this));
        recyclerView.setAdapter(new RecyclerView.Adapter<Holder>() {
            @Override
            public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
                return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.hackernews_search_result, parent, false));
            }

            @Override
            public void onBindViewHolder(Holder holder, int position) {
                holder.bind(stories.get(position));
            }

            @Override
            public int getItemCount() {
                return stories.size();
            }
        });
    }

    private class Holder extends SearchResultViewHolder {
        Holder(View itemView) {
            super(itemView);
        }

        void bind(Map<String, Object> story) {
            image.setVisibility(View.GONE);

            lineOne.setText((String) story.get("title"));
            lineTwo.setText((String) story.get("by"));

            itemView.setOnClickListener(v -> {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse((String) story.get("url")));
                getApplicationContext().startActivity(i);
            });
        }
    }
}

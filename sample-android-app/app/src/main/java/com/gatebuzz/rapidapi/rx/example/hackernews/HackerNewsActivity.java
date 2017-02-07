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
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class HackerNewsActivity extends AppCompatActivity {

    public static void launch(Context context) {
        context.startActivity(new Intent(context, HackerNewsActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hacker_news);
        HackerNewsApi api = ((ExampleApplication) getApplication()).getHackerNewsApi();
        api.getNewStories()
                .subscribeOn(Schedulers.newThread())
                .flatMap(new Func1<List<Long>, Observable<Long>>() {
                    @Override
                    public Observable<Long> call(List<Long> longs) {
                        return Observable.from(longs);
                    }
                })
                .take(5)
                .flatMap(new Func1<Long, Observable<Map<String, Object>>>() {
                    @Override
                    public Observable<Map<String, Object>> call(Long id) {
                        return api.getItem(id);
                    }
                })
                .map(new SuccessMapper<Map<String, Object>>())
                .map(m -> new Story((String) m.get("by"), (String) m.get("url"), (String) m.get("title"), ((Double) m.get("time")).longValue()))
                .toList()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::displayStories);
    }

    private void displayStories(final List<Story> stories) {
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
        public Holder(View itemView) {
            super(itemView);
        }

        public void bind(Story story) {
            image.setVisibility(View.GONE);

            lineOne.setText(story.title);
            lineTwo.setText(story.by);

            itemView.setOnClickListener(v -> {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(story.url));
                getApplicationContext().startActivity(i);
            });
        }
    }
}

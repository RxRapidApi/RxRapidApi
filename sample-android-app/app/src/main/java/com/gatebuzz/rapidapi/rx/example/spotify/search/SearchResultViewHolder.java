package com.gatebuzz.rapidapi.rx.example.spotify.search;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gatebuzz.rapidapi.rx.example.R;

public class SearchResultViewHolder extends RecyclerView.ViewHolder {
    public ImageView image;
    public TextView lineOne;
    public TextView lineTwo;

    public SearchResultViewHolder(View itemView) {
        super(itemView);
        image = (ImageView) itemView.findViewById(R.id.result_image);
        lineOne = (TextView) itemView.findViewById(R.id.line_one);
        lineTwo = (TextView) itemView.findViewById(R.id.line_two);
    }
}

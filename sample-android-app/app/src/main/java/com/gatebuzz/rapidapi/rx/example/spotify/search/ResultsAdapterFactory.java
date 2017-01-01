package com.gatebuzz.rapidapi.rx.example.spotify.search;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gatebuzz.rapidapi.rx.example.R;
import com.gatebuzz.rapidapi.rx.example.spotify.search.model.Album;
import com.gatebuzz.rapidapi.rx.example.spotify.search.model.Artist;
import com.gatebuzz.rapidapi.rx.example.spotify.search.model.Image;
import com.gatebuzz.rapidapi.rx.example.spotify.search.model.Playlist;
import com.gatebuzz.rapidapi.rx.example.spotify.search.model.SearchResult;
import com.gatebuzz.rapidapi.rx.example.spotify.search.model.Track;

class ResultsAdapterFactory {
    static RecyclerView.Adapter create(int searchType, SearchEngine searchEngine) {
        switch (searchType) {
            case ResultsActivity.ALBUMS:
                return new AlbumsAdapter(searchEngine.getSearchResult());
            case ResultsActivity.ARTISTS:
                return new ArtistsAdapter(searchEngine.getSearchResult());
            case ResultsActivity.PLAYLISTS:
                return new PlaylistsAdapter(searchEngine.getSearchResult());
            case ResultsActivity.TRACKS:
                return new TracksAdapter(searchEngine.getSearchResult());
        }
        return null;
    }

    private abstract static class BaseAdapter extends RecyclerView.Adapter<SearchResultViewHolder> {
        final SearchResult searchResult;

        BaseAdapter(SearchResult searchResult) {
            this.searchResult = searchResult;
        }

        @Override
        public SearchResultViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new SearchResultViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.search_result, parent, false));
        }

        void bindImage(SearchResultViewHolder holder, Image image) {
            if (image != null && image.url != null) {
                Glide.with(holder.itemView.getContext()).load(image.url).into(holder.image);
                holder.image.setVisibility(View.VISIBLE);
            } else {
                holder.image.setVisibility(View.INVISIBLE);
            }
        }
    }

    private static class AlbumsAdapter extends BaseAdapter {
        private AlbumsAdapter(SearchResult searchResult) {
            super(searchResult);
        }

        @Override
        public void onBindViewHolder(SearchResultViewHolder holder, int position) {
            Album album = searchResult.albums.get(position);
            holder.lineOne.setText(album.name);
            if (album.artists == null || album.artists.isEmpty()) {
                holder.lineTwo.setVisibility(View.INVISIBLE);
            } else {
                holder.lineTwo.setVisibility(View.VISIBLE);
                holder.lineTwo.setText(album.artists.get(0).name);
            }
            bindImage(holder, album.smallestImage());
        }

        @Override
        public int getItemCount() {
            return searchResult.albums.size();
        }
    }

    private static class ArtistsAdapter extends BaseAdapter {
        private ArtistsAdapter(SearchResult searchResult) {
            super(searchResult);
        }

        @Override
        public void onBindViewHolder(SearchResultViewHolder holder, int position) {
            Artist artist = searchResult.artists.get(position);
            holder.lineOne.setText(artist.name);
            holder.lineTwo.setVisibility(View.INVISIBLE);
            bindImage(holder, artist.smallestImage());
        }

        @Override
        public int getItemCount() {
            return searchResult.artists.size();
        }
    }

    private static class PlaylistsAdapter extends BaseAdapter {
        private PlaylistsAdapter(SearchResult searchResult) {
            super(searchResult);
        }

        @Override
        public void onBindViewHolder(SearchResultViewHolder holder, int position) {
            Playlist playlist = searchResult.playlists.get(position);
            holder.lineOne.setText(playlist.name);
            holder.lineTwo.setVisibility(View.INVISIBLE);
            bindImage(holder, playlist.smallestImage());
        }

        @Override
        public int getItemCount() {
            return searchResult.playlists.size();
        }
    }

    private static class TracksAdapter extends BaseAdapter {
        private TracksAdapter(SearchResult searchResult) {
            super(searchResult);
        }

        @Override
        public void onBindViewHolder(SearchResultViewHolder holder, int position) {
            Track track = searchResult.tracks.get(position);
            holder.lineOne.setText(track.name);
            if (track.artists == null || track.artists.isEmpty()) {
                holder.lineTwo.setVisibility(View.INVISIBLE);
            } else {
                holder.lineTwo.setVisibility(View.VISIBLE);
                holder.lineTwo.setText(track.artists.get(0).name);
            }
            bindImage(holder, track.smallestImage());
        }

        @Override
        public int getItemCount() {
            return searchResult.tracks.size();
        }
    }

    private static class SearchResultViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView lineOne;
        TextView lineTwo;

        SearchResultViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.result_image);
            lineOne = (TextView) itemView.findViewById(R.id.line_one);
            lineTwo = (TextView) itemView.findViewById(R.id.line_two);
        }
    }
}

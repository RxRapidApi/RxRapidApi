package com.gatebuzz.rapidapi.rx.example.spotify.search.model;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class SearchStatus {
    public static final int NOT_RUNNING = 0;
    public static final int SKIPPED = 1;
    public static final int STARTED = 2;
    public static final int FINISHED = 3;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({NOT_RUNNING, SKIPPED, STARTED, FINISHED})
    @interface SearchStatusValue {}

    public @SearchStatusValue int albums;
    public @SearchStatusValue int artists;
    public @SearchStatusValue int playlists;
    public @SearchStatusValue int tracks;

    public SearchStatus(@SearchStatusValue int albums, @SearchStatusValue int artists,
                        @SearchStatusValue int playlists, @SearchStatusValue int tracks) {
        this.albums = albums;
        this.artists = artists;
        this.playlists = playlists;
        this.tracks = tracks;
    }

    public SearchStatus() {
        this(NOT_RUNNING, NOT_RUNNING, NOT_RUNNING, NOT_RUNNING);
    }

    public void finishAlbums() {
        albums = FINISHED;
    }

    public void finishArtists() {
        artists = FINISHED;
    }

    public void finishPlaylists() {
        playlists = FINISHED;
    }

    public void finishTracks() {
        tracks = FINISHED;
    }

    public boolean isRunning() {
        return albums + artists + playlists + tracks > 0;
    }
}

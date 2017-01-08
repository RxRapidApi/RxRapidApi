package com.gatebuzz.rapidapi.rx.example.spotify.search;

import com.gatebuzz.rapidapi.rx.ApiPackage;
import com.gatebuzz.rapidapi.rx.Named;
import com.gatebuzz.rapidapi.rx.Required;
import com.gatebuzz.rapidapi.rx.example.spotify.search.model.Album;
import com.gatebuzz.rapidapi.rx.example.spotify.search.model.Artist;
import com.gatebuzz.rapidapi.rx.example.spotify.search.model.Playlist;
import com.gatebuzz.rapidapi.rx.example.spotify.search.model.Track;

import java.util.List;

import rx.Observable;

@ApiPackage("SpotifyPublicAPI")
public interface SpotifySearchApi {

    //region Type-safe searches skipping the optional parameters
    Observable<Albums> searchAlbums(@Required @Named("query") String query);

    Observable<Artists> searchArtists(@Required @Named("query") String query);

    Observable<Playlists> searchPlaylists(@Required @Named("query") String query);

    Observable<Tracks> searchTracks(@Required @Named("query") String query);
    //endregion

    //region Search Responses
    abstract class SearchResponseEnvelope<T> {
        public abstract List<T> getData();
    }

    class Albums extends SearchResponseEnvelope<Album> {
        Wrapper albums;

        @Override
        public List<Album> getData() {
            return albums.items;
        }

        private class Wrapper {
            List<Album> items;
        }
    }

    class Artists extends SearchResponseEnvelope<Artist> {
        Wrapper artists;

        @Override
        public List<Artist> getData() {
            return artists.items;
        }

        private class Wrapper {
            List<Artist> items;
        }
    }

    class Playlists extends SearchResponseEnvelope<Playlist> {
        Wrapper playlists;

        @Override
        public List<Playlist> getData() {
            return playlists.items;
        }

        private class Wrapper {
            List<Playlist> items;
        }
    }

    class Tracks extends SearchResponseEnvelope<Track> {
        Wrapper tracks;

        @Override
        public List<Track> getData() {
            return tracks.items;
        }

        private class Wrapper {
            List<Track> items;
        }
    }
    //endregion
}

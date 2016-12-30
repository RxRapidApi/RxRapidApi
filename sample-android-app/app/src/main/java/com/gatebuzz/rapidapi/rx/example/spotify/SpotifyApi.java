package com.gatebuzz.rapidapi.rx.example.spotify;

import com.gatebuzz.rapidapi.rx.ApiPackage;
import com.gatebuzz.rapidapi.rx.Named;
import com.gatebuzz.rapidapi.rx.Required;
import com.gatebuzz.rapidapi.rx.example.spotify.model.Album;
import com.gatebuzz.rapidapi.rx.example.spotify.model.Artist;
import com.gatebuzz.rapidapi.rx.example.spotify.model.Playlist;
import com.gatebuzz.rapidapi.rx.example.spotify.model.Track;

import java.util.List;
import java.util.Map;

import rx.Observable;

@ApiPackage("SpotifyPublicAPI")
public interface SpotifyApi {

    //region Quick searches
    @Named("searchAlbums")
    Observable<Albums> quickSearchAlbums(@Required @Named("query") String query);

    @Named("searchArtists")
    Observable<Artists> quickSearchArtists(@Required @Named("query") String query);

    @Named("searchPlaylists")
    Observable<Playlists> quickSearchPlaylists(@Required @Named("query") String query);

    @Named("searchTracks")
    Observable<Tracks> quickSearchTracks(@Required @Named("query") String query);
    //endregion

    //region Searching
    Observable<Map<String, Object>> searchAlbums(
            @Required @Named("query") String query,
            @Named("market") String market,
            @Named("limit") Integer limit,
            @Named("offset") Integer offset
    );

    Observable<Map<String, Object>> searchArtists(
            @Required @Named("query") String query,
            @Named("market") String market,
            @Named("limit") Integer limit,
            @Named("offset") Integer offset
    );

    Observable<Map<String, Object>> searchPlaylists(
            @Required @Named("query") String query,
            @Named("market") String market,
            @Named("limit") Integer limit,
            @Named("offset") Integer offset
    );

    Observable<Map<String, Object>> searchTracks(
            @Required @Named("query") String query,
            @Named("market") String market,
            @Named("limit") Integer limit,
            @Named("offset") Integer offset
    );
    //endregion

    //region drill-down into data
    Observable<Map<String, Object>> getAlbum(
            @Required @Named("id") String albumId
    );

    Observable<Map<String, Object>> getAlbumTracks(
            @Required @Named("id") String albumId
    );

    Observable<Map<String, Object>> getArtist(
            @Required @Named("id") String artistId
    );

    Observable<Map<String, Object>> getArtistAlbums(
            @Required @Named("id") String artistId,
            @Required @Named("album_type") String albumType,
            @Required @Named("market") String market,
            @Required @Named("limit") Integer limit,
            @Required @Named("offset") Integer offset
    );

    Observable<Map<String, Object>> getArtistTopTracks(
            @Required @Named("id") String artistId,
            @Required @Named("country") String country
    );

    Observable<Map<String, Object>> getArtistRelatedArtists(
            @Required @Named("id") String artistId
    );

    Observable<Map<String, Object>> getTrack(
            @Required @Named("id") String trackId
    );

    Observable<Map<String, Object>> getUserProfile(
            @Required @Named("id") String userId
    );
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

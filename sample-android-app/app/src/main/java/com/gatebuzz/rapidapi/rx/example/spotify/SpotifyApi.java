package com.gatebuzz.rapidapi.rx.example.spotify;

import com.gatebuzz.rapidapi.rx.ApiPackage;
import com.gatebuzz.rapidapi.rx.Named;
import com.gatebuzz.rapidapi.rx.Required;

import java.util.Map;

import rx.Observable;

@ApiPackage("SpotifyPublicAPI")
public interface SpotifyApi {

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
}

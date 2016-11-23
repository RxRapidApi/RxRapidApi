package com.gatebuzz.rapidapi.rx.example;

import com.gatebuzz.rapidapi.rx.ApiPackage;
import com.gatebuzz.rapidapi.rx.Application;
import com.gatebuzz.rapidapi.rx.BuildConfig;
import com.gatebuzz.rapidapi.rx.Named;

import java.util.Map;

import rx.Observable;

@Application(project = BuildConfig.PROJECT, key = BuildConfig.API_KEY)
public interface SpotifyApi {

    @ApiPackage("SpotifyPublicAPI")
    Observable<Map<String, Object>> searchAlbums(
            @Named("query") String query,
            @Named("market") String market,
            @Named("limit") String limit,
            @Named("offset") String offset
    );

}

package com.gatebuzz.rapidapi.rx.example;

import com.gatebuzz.rapidapi.rx.ApiPackage;
import com.gatebuzz.rapidapi.rx.Application;
import com.gatebuzz.rapidapi.rx.Named;

import java.util.Map;

import rx.Observable;

@Application(project = BuildConfig.PROJECT, key = BuildConfig.API_KEY)
@ApiPackage("NasaAPI")
public interface NasaApi {

    Observable<Map<String, Object>> getPictureOfTheDay(
            @Named("date") String date,
            @Named("highResolution") String highResolution,
            @Named("apiKey") String apiKey);

}

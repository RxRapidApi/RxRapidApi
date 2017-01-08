package com.gatebuzz.rapidapi.rx.example.nasa;

import com.gatebuzz.rapidapi.rx.ApiPackage;
import com.gatebuzz.rapidapi.rx.DefaultParameters;
import com.gatebuzz.rapidapi.rx.Named;
import com.gatebuzz.rapidapi.rx.Required;

import java.util.Map;

import rx.Observable;

@ApiPackage("NasaAPI")
@DefaultParameters("apiKey")
public interface NasaApi {

    Observable<Map<String, Object>> getPictureOfTheDay(
            @Named("date") String date,
            @Named("highResolution") Boolean highResolution
    );

    Observable<Map<String, Object>> getClosestAsteroids(
            @Named("startDate") String startDate,
            @Named("endDate") String endDate
    );

    Observable<Map<String, Object>> getSingleAsteroid(
            @Named("asteroidId") String asteroidId
    );

    Observable<Map<String, Object>> getAsteroidStats();

    Observable<Map<String, Object>> getAsteroids(
            @Named("page") Integer page,
            @Named("size") Integer size
    );

    Observable<Map<String, Object>> getEPICEarthImagery(
            @Named("date") String date,
            @Named("availableDates") String availableDates
    );

    Observable<Map<String, Object>> getPatents(
            @Named("query") String query,
            @Named("conceptTags") String conceptTags,
            @Named("limit") Integer limit
    );

    Observable<Map<String, Object>> getSpaceSounds(
            @Named("query") String query,
            @Named("limit") Integer limit
    );

    Observable<Map<String, Object>> getEarthImagery(
            @Required @Named("latitude") String latitude,
            @Required @Named("longitude") String longitude,
            @Named("dimension") String dimension,
            @Named("date") Integer date,
            @Named("cloudScore") Boolean cloudScore
    );

    Observable<Map<String, Object>> getEarthAssets(
            @Required @Named("latitude") String latitude,
            @Required @Named("longitude") String longitude,
            @Named("begin") String begin,
            @Named("end") String end
    );

    Observable<Map<String, Object>> getMarsRoverPhotos(
            @Named("sol") Integer sol,
            @Named("camera") String cameraAbbreviation,
            @Named("page") Integer page
    );

    Observable<Map<String, Object>> getEONETEvents(
            @Named("source") String source,
            @Named("status") String status,
            @Named("days") Integer days
    );

    Observable<Map<String, Object>> getEONETCategories(
            @Named("categoryId") String categoryId,
            @Named("source") String source,
            @Named("status") String status,
            @Named("days") Integer limit,
            @Named("days") Integer days
    );

    Observable<Map<String, Object>> getEONETLayers(
            @Named("categoryId") String categoryId
    );
}

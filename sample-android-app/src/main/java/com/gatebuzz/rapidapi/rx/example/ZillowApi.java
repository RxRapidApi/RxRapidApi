package com.gatebuzz.rapidapi.rx.example;

import com.gatebuzz.rapidapi.rx.ApiPackage;
import com.gatebuzz.rapidapi.rx.Application;
import com.gatebuzz.rapidapi.rx.Named;
import com.gatebuzz.rapidapi.rx.UrlEncoded;

import java.util.Map;

import rx.Observable;

@Application(project = BuildConfig.PROJECT, key = BuildConfig.API_KEY)
public interface ZillowApi {

    @ApiPackage("Zillow")
    Observable<Map<String, Object>> getSearchResults(
            @Named("rentzestimate") String rentEstimate,
            @Named("zwsId") String zillowWebServiceId,
            @Named("address") @UrlEncoded String address,
            @Named("citystatezip") String cityStateZip
    );

}

package com.gatebuzz.rapidapi.rx.example;

import com.gatebuzz.rapidapi.rx.ApiPackage;
import com.gatebuzz.rapidapi.rx.DefaultParameters;
import com.gatebuzz.rapidapi.rx.Named;
import com.gatebuzz.rapidapi.rx.UrlEncoded;

import java.util.Map;

import rx.Observable;

@ApiPackage("Zillow")
@DefaultParameters("zwsId")
public interface ZillowApi {

    Observable<Map<String, Object>> getZestimate(
            @Named("zpid") String propertyId,
            @Named("rentzestimate") String rentEstimate
    );

    Observable<Map<String, Object>> getSearchResults(
            @Named("rentzestimate") String rentEstimate,
            @Named("address") @UrlEncoded String address,
            @Named("citystatezip") String cityStateZip
    );

    Observable<Map<String, Object>> getChart(
            @Named("zpid") String propertyId,
            @Named("unitType") String unitType,
            @Named("width") String width,
            @Named("height") String height,
            @Named("chartDuration") String chartDuration
    );

    Observable<Map<String, Object>> getComps(
            @Named("zpid") String propertyId,
            @Named("count") String count,
            @Named("rentzestimate") String rentEstimate
    );

    Observable<Map<String, Object>> getDeepComps(
            @Named("zpid") String propertyId,
            @Named("count") String count,
            @Named("rentzestimate") String rentEstimate
    );

    Observable<Map<String, Object>> getDeepSearchResults(
            @Named("rentzestimate") String rentEstimate,
            @Named("address") @UrlEncoded String address,
            @Named("citystatezip") String cityStateZip
    );

    Observable<Map<String, Object>> getUpdatedPropertyDetails(
            @Named("zpid") String propertyId
    );

    Observable<Map<String, Object>> getRegionChildren(
            @Named("regionId") String regionId,
            @Named("state") String state,
            @Named("county") String county,
            @Named("city") String city,
            @Named("childtype") String childType
    );

    Observable<Map<String, Object>> getRateSummary(
            @Named("state") String state
    );

    Observable<Map<String, Object>> getMonthlyPayments(
            @Named("price") String price,
            @Named("down") String down,
            @Named("dollarsdown") String dollarsDown,
            @Named("zip") String zip
    );

    Observable<Map<String, Object>> calculateMonthlyPaymentsAdvanced(
            @Named("price") String price,
            @Named("down") String down,
            @Named("amount") String amount,
            @Named("rate") String rate,
            @Named("schedule") String schedule,
            @Named("terminmonths") String termInMonths,
            @Named("propertytax") String propertyTax,
            @Named("hazard") String hazard,
            @Named("pmi") String pmi,
            @Named("hoa") String hoa,
            @Named("zip") String zip
    );
}
package com.gatebuzz.rapidapi.rx.example.zillow;

import com.gatebuzz.rapidapi.rx.ApiPackage;
import com.gatebuzz.rapidapi.rx.DefaultParameters;
import com.gatebuzz.rapidapi.rx.Named;
import com.gatebuzz.rapidapi.rx.Required;
import com.gatebuzz.rapidapi.rx.UrlEncoded;

import java.util.Map;

import rx.Observable;

@ApiPackage("Zillow")
@DefaultParameters("zwsId")
public interface ZillowApi {

    Observable<Map<String, Object>> getZestimate(
            @Named("zpid") String propertyId,
            @Named("rentzestimate") Boolean rentEstimate
    );

    Observable<Map<String, Object>> getSearchResults(
            @Required @Named("address") @UrlEncoded String address,
            @Required @Named("citystatezip") @UrlEncoded String cityStateZip,
            @Named("rentzestimate") Boolean rentEstimate
    );

    Observable<Map<String, Object>> getChart(
            @Required @Named("zpid") String propertyId,
            @Required @Named("unitType") String unitType,
            @Named("width") Integer width,
            @Named("height") Integer height,
            @Named("chartDuration") String chartDuration
    );

    Observable<Map<String, Object>> getComps(
            @Required @Named("zpid") String propertyId,
            @Required @Named("count") String count,
            @Named("rentzestimate") Boolean rentEstimate
    );

    Observable<Map<String, Object>> getDeepComps(
            @Required @Named("zpid") String propertyId,
            @Required @Named("count") String count,
            @Named("rentzestimate") Boolean rentEstimate
    );

    Observable<Map<String, Object>> getDeepSearchResults(
            @Required @Named("address") @UrlEncoded String address,
            @Required @Named("citystatezip") @UrlEncoded String cityStateZip,
            @Named("rentzestimate") Boolean rentEstimate
    );

    Observable<Map<String, Object>> getUpdatedPropertyDetails(
            @Required @Named("zpid") String propertyId
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
            @Required @Named("price") String price,
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
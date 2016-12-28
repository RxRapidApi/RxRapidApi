package com.gatebuzz.rapidapi.rx.example.spotify;

import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.functions.Func1;

class ItemsMapper implements Func1<Map<String, Object>, Observable<Map<String, Object>>> {
    @Override
    @SuppressWarnings("unchecked")
    public Observable<Map<String, Object>> call(Map<String, Object> actual) {
        if (actual == null) {
            return Observable.empty();
        }

        List<Map<String, Object>> items = (List<Map<String, Object>>) actual.get("items");

        return items != null ? Observable.from(items) : Observable.empty();
    }
}

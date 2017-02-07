package com.gatebuzz.rapidapi.rx.utils;

import rx.functions.Func1;

import java.util.Map;

/**
 * Used as part of an Rx "map" operation to drill down into a kwy/value pair map response.
 *
 * @see com.gatebuzz.rapidapi.rx.internal.KeyValueMapProcessor
 * @see com.gatebuzz.rapidapi.rx.utils.SuccessMapper
 */
public class DrillDown implements Func1<Map<String, Object>, Map<String, Object>> {
    private final String key;

    public DrillDown(String key) {
        this.key = key;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Map<String, Object> call(Map<String, Object> stringObjectMap) {
        return (Map<String, Object>) stringObjectMap.get(key);
    }
}

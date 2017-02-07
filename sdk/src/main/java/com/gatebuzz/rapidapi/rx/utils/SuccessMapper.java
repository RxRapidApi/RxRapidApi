package com.gatebuzz.rapidapi.rx.utils;

import rx.functions.Func1;

import java.util.Map;

/**
 * Use as part of an Rx "map" operation to pull the "success" payload from a map response.
 *
 * @param <T> the type of value to be returned.
 *
 * @see com.gatebuzz.rapidapi.rx.internal.KeyValueMapProcessor
 * @see com.gatebuzz.rapidapi.rx.utils.DrillDown
 */
public class SuccessMapper<T> implements Func1<Map<String, Object>, T> {
    @Override
    @SuppressWarnings("unchecked")
    public T call(Map<String, Object> serviceResult) {
        return (T) serviceResult.get("success");
    }
}

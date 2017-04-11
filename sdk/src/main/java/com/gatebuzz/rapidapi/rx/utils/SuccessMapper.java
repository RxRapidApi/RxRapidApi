package com.gatebuzz.rapidapi.rx.utils;

import io.reactivex.functions.Function;

import java.util.Map;

/**
 * Use as part of an Rx "map" operation to pull the "success" payload from a map response.
 *
 * @param <T> the type of value to be returned.
 * @see com.gatebuzz.rapidapi.rx.internal.KeyValueMapProcessor
 * @see com.gatebuzz.rapidapi.rx.utils.DrillDown
 */
public class SuccessMapper<T> implements Function<Map<String, Object>, T> {
    @Override
    @SuppressWarnings("unchecked")
    public T apply(Map<String, Object> serviceResult) throws Exception {
        return (T) serviceResult.get("success");
    }
}

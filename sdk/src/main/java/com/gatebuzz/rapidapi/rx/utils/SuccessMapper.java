package com.gatebuzz.rapidapi.rx.utils;

import rx.functions.Func1;

import java.util.Map;

public class SuccessMapper<T> implements Func1<Map<String, Object>, T> {
    @Override
    @SuppressWarnings("unchecked")
    public T call(Map<String, Object> serviceResult) {
        return (T) serviceResult.get("success");
    }
}

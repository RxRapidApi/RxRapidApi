package com.gatebuzz.rapidapi.rx;

import android.support.annotation.Keep;

import java.util.Map;

@Keep
public class FailedCallException extends Throwable {
    private final Map<String, Object> response;

    public FailedCallException(Map<String, Object> response) {
        this.response = response;
    }

    public Map<String, Object> getResponse() {
        return response;
    }
}

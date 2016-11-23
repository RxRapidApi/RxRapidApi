package com.gatebuzz.rapidapi.rx;

import java.util.Map;

public class FailedCallException extends Throwable {
    private Map<String, Object> response;

    public FailedCallException(Map<String, Object> response) {
        this.response = response;
    }

    public Map<String, Object> getResponse() {
        return response;
    }
}

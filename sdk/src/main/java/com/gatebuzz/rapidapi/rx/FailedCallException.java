package com.gatebuzz.rapidapi.rx;

import java.util.Map;

/**
 * Indicates that a Rapid API call failed.
 */
public class FailedCallException extends Throwable {
    private final Map<String, Object> response;

    public FailedCallException(Map<String, Object> response) {
        this.response = response;
    }

    /**
     * This map contains a single element "error" with the return value from the
     * service call.  The actual data type of the "error" element will vary based
     * on the service call being made.
     *
     * @return the response from the server
     */
    public Map<String, Object> getResponse() {
        return response;
    }
}
